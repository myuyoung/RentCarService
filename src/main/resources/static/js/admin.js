// 관리자 대시보드 스크립트
(function () {
  class AdminPage {
    constructor() {
      this.init();
    }

    init() {
      this.guardAdmin();
      this.setupTabs();
      this.loadStatistics();
      this.loadMembers();
      this.loadCars();
      this.loadRentals();
      this.injectCreateCarUI();
      this.setupCreateCarForm();
      this.setupSubmissionsTab();
    }

    guardAdmin() {
      const userInfo = JSON.parse(localStorage.getItem('userInfo') || 'null');
      const token = apiClient.getAuthToken();
      if (!token || !userInfo || userInfo.role !== 'ADMIN') {
        notification.error('관리자 전용 페이지입니다.');
        window.location.href = '/login';
        return;
      }
      // 토큰을 apiClient에 적용
      apiClient.setAuthToken(token);
    }

    setupTabs() {
      document.querySelectorAll('.admin-tab-btn').forEach((btn) => {
        btn.addEventListener('click', (e) => {
          const tab = e.currentTarget.getAttribute('data-tab');
          document.querySelectorAll('.admin-tab-btn span').forEach((span) => {
            span.className = 'py-2 px-1 border-b-2 border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300 font-medium text-sm';
          });
          e.currentTarget.querySelector('span').className = 'py-2 px-1 border-b-2 border-indigo-500 text-indigo-600 font-medium text-sm';

          document.querySelectorAll('.admin-tab-panel').forEach((panel) => panel.classList.add('hidden'));
          document.getElementById(`${tab}-tab`).classList.remove('hidden');
          
          // 탭에 따른 데이터 로드
          if (tab === 'submissions') {
            this.loadSubmissions();
          }
        });
      });

      // 해시로 특정 탭 열기 지원 (#submissions 등)
      const openByHash = () => {
        const hash = (location.hash || '').replace('#', '');
        if (!hash) return;
        const btn = document.querySelector(`.admin-tab-btn[data-tab="${hash}"]`);
        if (btn) btn.click();
      };
      window.addEventListener('hashchange', openByHash);
      // 초기 로드 시 한번 실행
      openByHash();
    }

    async loadStatistics() {
      try {
        const res = await apiClient.get('/api/admin/statistics');
        if (res.success) {
          const s = res.data;
          document.getElementById('stat-total-members').textContent = s.totalMembers;
          document.getElementById('stat-total-rentals').textContent = s.totalRentals;
          document.getElementById('stat-total-cars').textContent = s.totalCars;
          document.getElementById('stat-active-rentals').textContent = s.activeRentals;
        }
      } catch (e) {
        console.error(e);
      }
    }

    async loadMembers(page = 0, size = 10) {
      try {
        const res = await apiClient.get(`/api/admin/members?page=${page}&size=${size}`);
        const container = document.getElementById('admin-members-container');
        if (res.success) {
          const pageData = res.data;
          container.innerHTML = this.renderTable(
            ['이름', '이메일', '권한', '액션'],
            pageData.content.map((m) => [
              m.name || '-',
              m.email || '-',
              m.role || '-',
              `<button data-email="${m.email}" class="text-indigo-600 hover:text-indigo-800 text-sm admin-promote">승격(ADMIN)</button>`
            ])
          );

          container.querySelectorAll('.admin-promote').forEach((btn) => {
            btn.addEventListener('click', async (e) => {
              const email = e.currentTarget.getAttribute('data-email');
              const memberRes = await apiClient.get(`/api/admin/members?page=0&size=1&email=${encodeURIComponent(email)}`);
              // 단순 예시: 목록에서 찾거나 별도 검색 API가 있다면 사용
              notification.warning('백엔드에 단일 회원 검색 API가 없으면 직접 ID를 전달하도록 UI를 바꿔야 합니다.');
            });
          });
        } else {
          container.innerHTML = '<div class="p-4 text-gray-500">회원 정보를 불러오지 못했습니다.</div>';
        }
      } catch (e) {
        console.error(e);
      }
    }

    async loadCars(page = 0, size = 10) {
      try {
        const res = await apiClient.get(`/api/admin/cars?page=${page}&size=${size}`);
        const container = document.getElementById('admin-cars-container');
        if (res.success) {
          const pageData = res.data;
          container.innerHTML = this.renderTable(
            ['차량명', '차량번호', '일일요금', '추천점수', '액션'],
            pageData.content.map((c) => [
              c.name,
              c.rentCarNumber,
              utils.formatCurrency(c.rentPrice),
              c.recommend ?? 0,
              `<button data-id="${c.id ?? ''}" class="text-red-600 hover:text-red-800 text-sm admin-delete-car">삭제</button>`
            ])
          );

          container.querySelectorAll('.admin-delete-car').forEach((btn) => {
            btn.addEventListener('click', async (e) => {
              const id = e.currentTarget.getAttribute('data-id');
              if (!id) return notification.warning('삭제할 차량 ID가 없습니다. DTO에 id 추가가 필요합니다.');
              if (!confirm('이 차량을 삭제하시겠습니까?')) return;
              const del = await apiClient.delete(`/api/admin/cars/${id}`);
              if (del.success) {
                notification.success('차량이 삭제되었습니다.');
                this.loadCars(page, size);
              } else {
                notification.error('차량 삭제에 실패했습니다.');
              }
            });
          });
        } else {
          container.innerHTML = '<div class="p-4 text-gray-500">차량 정보를 불러오지 못했습니다.</div>';
        }
      } catch (e) {
        console.error(e);
      }
    }

    injectCreateCarUI() {
      const carsTab = document.getElementById('cars-tab');
      if (!carsTab) return;
      const header = carsTab.querySelector('.flex.justify-between');
      const toolbar = document.createElement('div');
      toolbar.className = 'mt-3 flex justify-end';
      toolbar.innerHTML = '<button id="open-create-car" class="bg-indigo-600 text-white px-3 py-1 rounded text-sm">차량 추가</button>';
      carsTab.appendChild(toolbar);

      const modal = document.createElement('div');
      modal.id = 'create-car-modal';
      modal.className = 'fixed inset-0 bg-black bg-opacity-30 hidden items-center justify-center';
      modal.innerHTML = `
        <div class="bg-white p-6 rounded shadow w-full max-w-md">
          <h4 class="text-lg font-semibold mb-4">차량 추가</h4>
          <form id="create-car-form" class="space-y-3">
            <input name="name" class="w-full border p-2 rounded" placeholder="차량명" required />
            <input name="rentCarNumber" class="w-full border p-2 rounded" placeholder="차량번호" required />
            <input name="rentPrice" type="number" class="w-full border p-2 rounded" placeholder="일일요금" required />
            <select name="reservationStatus" class="w-full border p-2 rounded">
              <option value="AVAILABLE">AVAILABLE</option>
              <option value="RESERVED">RESERVED</option>
            </select>
            <div class="flex gap-2 justify-end">
              <button type="button" id="cancel-create-car" class="px-3 py-1 border rounded">취소</button>
              <button type="submit" class="px-3 py-1 bg-indigo-600 text-white rounded">등록</button>
            </div>
          </form>
        </div>`;
      document.body.appendChild(modal);
    }

    setupCreateCarForm() {
      const openBtn = document.getElementById('open-create-car');
      const modal = document.getElementById('create-car-modal');
      const cancelBtn = document.getElementById('cancel-create-car');
      const form = document.getElementById('create-car-form');

      if (!openBtn || !modal || !form) return;

      openBtn.addEventListener('click', () => {
        modal.classList.remove('hidden');
        modal.classList.add('flex');
      });
      if (cancelBtn) {
        cancelBtn.addEventListener('click', () => {
          modal.classList.add('hidden');
          modal.classList.remove('flex');
          form.reset();
        });
      }
      form.addEventListener('submit', async (e) => {
        e.preventDefault();
        const data = Object.fromEntries(new FormData(form).entries());
        data.rentPrice = Number(data.rentPrice);
        try {
          const res = await apiClient.post('/api/admin/cars', data);
          if (res.success) {
            notification.success('차량 등록 성공');
            this.loadCars();
            if (cancelBtn) cancelBtn.click();
          } else {
            notification.error(res.message || '차량 등록 실패');
          }
        } catch (err) {
          notification.error('차량 등록 중 오류가 발생했습니다.');
        }
      });
    }

    async loadRentals(page = 0, size = 10) {
      try {
        const res = await apiClient.get(`/api/admin/rentals?page=${page}&size=${size}`);
        const container = document.getElementById('admin-rentals-container');
        if (res.success) {
          const pageData = res.data;
          container.innerHTML = this.renderTable(
            ['예약ID', '차량', '대여일', '반납일', '기간(일)'],
            pageData.content.map((r) => [
              r.rent_id || '-',
              r.rentCars?.name || '-',
              utils.formatDate(r.rentTime, true),
              utils.formatDate(r.endTime, true),
              r.duration
            ])
          );
        } else {
          container.innerHTML = '<div class="p-4 text-gray-500">예약 정보를 불러오지 못했습니다.</div>';
        }
      } catch (e) {
        console.error(e);
      }
    }

    renderTable(headers, rows) {
      const thead = `<thead><tr>${headers.map((h) => `<th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">${h}</th>`).join('')}</tr></thead>`;
      const tbody = `<tbody class="bg-white divide-y divide-gray-200">${rows
        .map(
          (cols) => `<tr>${cols
            .map((c) => `<td class="px-4 py-2 whitespace-nowrap text-sm text-gray-700">${c}</td>`) 
            .join('')}</tr>`
        )
        .join('')}</tbody>`;
      return `<table class="min-w-full divide-y divide-gray-200">${thead}${tbody}</table>`;
    }

    // --- 차량 등록 신청 탭 설정 ---
    setupSubmissionsTab() {
      // HTML에 이미 존재하는 새로고침 버튼에 이벤트 리스너 추가
      const refreshBtn = document.getElementById('refresh-submissions');
      if (refreshBtn) {
        refreshBtn.addEventListener('click', () => {
          this.loadSubmissions();
        });
      }
    }

    async loadSubmissions(page = 0, size = 10) {
      try {
        const res = await apiClient.get(`/api/admin/car-submissions?page=${page}&size=${size}`);
        const container = document.getElementById('admin-submissions-container');
        if (!container) return;
        if (res.success) {
          const pageData = res.data;
          const rows = (pageData.content || []).map((s) => [
            `<a href="/admin/car-submissions/${s.id}" class="text-indigo-600 hover:text-indigo-800 font-mono text-xs">${s.id}</a>`,
            (s.memberName || '-') + (s.memberId ? ` <span class="text-gray-400 text-xs">(${s.memberId})</span>` : ''),
            s.carName || '-',
            s.rentCarNumber || '-',
            s.rentPrice ?? '-',
            `<span class="px-2 py-1 rounded text-xs font-semibold ${s.status === 'PENDING' ? 'bg-yellow-100 text-yellow-800' : s.status === 'APPROVED' ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'}">${s.status}</span>`,
            // API 기반 이미지 미리보기
            s.imageUrls && s.imageUrls.length > 0 ? 
              `<div class="flex gap-1" data-submission-id="${s.id}">${this.generateImagePreviews(s.imageUrls, s.id)}</div>` 
              : '<span class="text-gray-400 text-xs">이미지 없음</span>',
            `<div class="space-x-2">
               <a href="/admin/car-submissions/${s.id}" class="text-blue-600 hover:text-blue-800 text-sm">상세</a>
               <button data-id="${s.id}" class="text-green-600 hover:text-green-800 text-sm admin-approve-sub">승인</button>
               <button data-id="${s.id}" class="text-red-600 hover:text-red-800 text-sm admin-reject-sub">반려</button>
             </div>`
          ]);
          container.innerHTML = this.renderTable(['ID','회원','차량명','차량번호','요금','상태','이미지','액션'], rows);
          container.querySelectorAll('.admin-approve-sub').forEach((btn) => {
            btn.addEventListener('click', async (e) => {
              const id = e.currentTarget.getAttribute('data-id');
              if (!confirm('이 신청을 승인하시겠습니까?')) return;
              const r = await apiClient.post(`/api/admin/car-submissions/${id}/approve`, {});
              if (r.success) {
                notification.success('승인되었습니다.');
                this.loadSubmissions(page,size);
              } else {
                notification.error('승인에 실패했습니다.');
              }
            });
          });
          container.querySelectorAll('.admin-reject-sub').forEach((btn) => {
            btn.addEventListener('click', async (e) => {
              const id = e.currentTarget.getAttribute('data-id');
              if (!confirm('이 신청을 반려하시겠습니까?')) return;
              const r = await apiClient.post(`/api/admin/car-submissions/${id}/reject`, {});
              if (r.success) {
                notification.success('반려되었습니다.');
                this.loadSubmissions(page,size);
              } else {
                notification.error('반려에 실패했습니다.');
              }
            });
          });
          
          // 테이블 렌더링 후 API 기반 이미지 로드
          this.loadSubmissionImages(data.data.content);
        } else {
          container.innerHTML = '<div class="p-4 text-gray-500">신청 목록을 불러오지 못했습니다.</div>';
        }
      } catch (e) {
        console.error(e);
      }
    }

    /**
     * API 기반 이미지 미리보기 HTML 생성
     * @param {Array} imageUrls - 이미지 URL 배열
     * @param {string} submissionId - 신청 ID
     * @returns {string} 이미지 미리보기 HTML
     */
    generateImagePreviews(imageUrls, submissionId) {
      const previews = imageUrls.slice(0, 3).map((url, index) => {
        // URL에서 이미지 ID 추출
        const imageIdMatch = url.match(/\/api\/files\/view\/(\d+)/);
        if (!imageIdMatch) {
          return `<div class="w-12 h-8 bg-gray-200 rounded border flex items-center justify-center text-xs text-gray-500">?</div>`;
        }
        
        const imageId = parseInt(imageIdMatch[1]);
        const uniqueId = `admin-preview-${submissionId}-${imageId}-${index}`;
        
        return `
          <img 
            id="${uniqueId}" 
            class="w-12 h-8 object-cover rounded border cursor-pointer hover:opacity-80 transition-opacity" 
            alt="미리보기 로딩 중..." 
            data-image-id="${imageId}"
            onclick="openAdminImageModal(${imageId})"
          />
        `;
      }).join('');
      
      const moreCount = imageUrls.length > 3 ? imageUrls.length - 3 : 0;
      const moreIndicator = moreCount > 0 ? `<span class="text-xs text-gray-500 ml-1">+${moreCount}</span>` : '';
      
      return previews + moreIndicator;
    }

    /**
     * 신청 목록의 이미지들을 API 기반으로 로드
     * @param {Array} submissions - 신청 목록
     */
    async loadSubmissionImages(submissions) {
      console.log('=== 관리자 페이지 이미지 스트리밍 시작 ===');
      
      const imageConfigs = [];
      
      submissions.forEach(submission => {
        if (submission.imageUrls && submission.imageUrls.length > 0) {
          submission.imageUrls.slice(0, 3).forEach((url, index) => {
            const imageIdMatch = url.match(/\/api\/files\/view\/(\d+)/);
            if (imageIdMatch) {
              const imageId = parseInt(imageIdMatch[1]);
              const uniqueId = `admin-preview-${submission.id}-${imageId}-${index}`;
              const imgElement = document.getElementById(uniqueId);
              
              if (imgElement) {
                imageConfigs.push({
                  element: imgElement,
                  imageId: imageId,
                  options: {
                    fallback: 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNDgiIGhlaWdodD0iMzIiIHZpZXdCb3g9IjAgMCA0OCAzMiIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPHJlY3Qgd2lkdGg9IjQ4IiBoZWlnaHQ9IjMyIiBmaWxsPSIjRjNGNEY2Ii8+CjxwYXRoIGQ9Ik0yNCAyMEMyNS4xMDQ2IDIwIDI2IDE5LjEwNDYgMjYgMThDMjYgMTYuODk1NCAyNS4xMDQ2IDE2IDI0IDE2QzIyLjg5NTQgMTYgMjIgMTYuODk1NCAyMiAxOEMyMiAxOS4xMDQ2IDIyLjg5NTQgMjAgMjQgMjBaIiBmaWxsPSIjOUNBM0FGIi8+CjxwYXRoIGQ9Ik0yMCAyMkwyMiAyMEwyNCAyMkwyNiAyMEwyOCAyMkwyOCAyNkgyMFYyMloiIGZpbGw9IiM5Q0EzQUYiLz4KPC9zdmc+',
                    onLoad: () => console.log(`관리자 미리보기 로드 성공: ID ${imageId}`),
                    onError: (error) => console.warn(`관리자 미리보기 로드 실패: ID ${imageId}`, error)
                  }
                });
              }
            }
          });
        }
      });
      
      if (imageConfigs.length > 0) {
        try {
          await imageStreaming.loadMultipleImages(imageConfigs);
          console.log(`관리자 페이지: ${imageConfigs.length}개 미리보기 이미지 로드 완료`);
        } catch (error) {
          console.error('관리자 페이지 이미지 로드 중 오류:', error);
        }
      }
    }
  }

  document.addEventListener('DOMContentLoaded', () => {
    window.adminPage = new AdminPage();
  });
})();

/**
 * 관리자 페이지 이미지 모달 열기 (전역 함수)
 * @param {number} imageId - 이미지 ID
 */
function openAdminImageModal(imageId) {
  console.log(`관리자 이미지 모달 열기: ID ${imageId}`);
  
  // 기존 모달 제거
  const existingModal = document.getElementById('admin-image-modal');
  if (existingModal) {
    existingModal.remove();
  }
  
  // 모달 HTML 생성
  const modal = document.createElement('div');
  modal.id = 'admin-image-modal';
  modal.className = 'fixed inset-0 bg-black bg-opacity-75 flex items-center justify-center z-50';
  modal.innerHTML = `
    <div class="relative max-w-3xl max-h-full p-4">
      <button 
        onclick="closeAdminImageModal()" 
        class="absolute top-2 right-2 text-white bg-black bg-opacity-50 rounded-full w-8 h-8 flex items-center justify-center hover:bg-opacity-75 z-10"
      >
        ✕
      </button>
      <img 
        id="admin-modal-image" 
        class="max-w-full max-h-full object-contain rounded" 
        alt="확대된 이미지"
      />
      <div class="absolute bottom-2 left-2 bg-black bg-opacity-50 text-white px-2 py-1 rounded text-sm">
        관리자 모드 - 이미지 ID: ${imageId}
      </div>
    </div>
  `;
  
  document.body.appendChild(modal);
  
  // 모달 이미지 로드
  const modalImg = document.getElementById('admin-modal-image');
  imageStreaming.loadImage(modalImg, imageId, {
    fallback: 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNDAwIiBoZWlnaHQ9IjMwMCIgdmlld0JveD0iMCAwIDQwMCAzMDAiIGZpbGw9Im5vbmUiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+CjxyZWN0IHdpZHRoPSI0MDAiIGhlaWdodD0iMzAwIiBmaWxsPSIjRjNGNEY2Ii8+Cjx0ZXh0IHg9IjIwMCIgeT0iMTUwIiB0ZXh0LWFuY2hvcj0ibWlkZGxlIiBmaWxsPSIjOUNBM0FGIiBmb250LWZhbWlseT0ic2Fucy1zZXJpZiIgZm9udC1zaXplPSIxNiI+7J207Jq47KeA66W8IOyVieuhnOyImO2VoCDsiJjsl4bsirXri4jri6QuPC90ZXh0Pgo8L3N2Zz4=',
    onLoad: () => console.log(`관리자 모달 이미지 로드 성공: ID ${imageId}`),
    onError: (error) => {
      console.error(`관리자 모달 이미지 로드 실패: ID ${imageId}`, error);
      notification.error('이미지를 불러올 수 없습니다.');
    }
  });
  
  // ESC 키로 모달 닫기
  const handleEscKey = (e) => {
    if (e.key === 'Escape') {
      closeAdminImageModal();
      document.removeEventListener('keydown', handleEscKey);
    }
  };
  document.addEventListener('keydown', handleEscKey);
  
  // 모달 배경 클릭으로 닫기
  modal.addEventListener('click', (e) => {
    if (e.target === modal) {
      closeAdminImageModal();
    }
  });
}

/**
 * 관리자 이미지 모달 닫기 (전역 함수)
 */
function closeAdminImageModal() {
  const modal = document.getElementById('admin-image-modal');
  if (modal) {
    modal.remove();
  }
}


