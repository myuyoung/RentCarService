(function(){
  async function init(){
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || 'null');
    const token = apiClient.getAuthToken();
    if (!token || !userInfo || userInfo.role !== 'ADMIN') {
      notification.error('관리자 전용 페이지입니다.');
      window.location.href = '/login';
      return;
    }

    const id = location.pathname.split('/').pop();
    try{
      const res = await apiClient.get(`/api/admin/car-submissions/${id}`);
      if(!res.success){
        document.getElementById('submission-detail').innerHTML = '<div class="text-gray-500">상세 정보를 불러오지 못했습니다.</div>';
        return;
      }
      const s = res.data;
      renderDetail(s);
      bindActions(s.id);
    }catch(e){
      document.getElementById('submission-detail').innerHTML = '<div class="text-gray-500">상세 정보를 불러오지 못했습니다.</div>';
    }
  }

  function renderDetail(s){
    console.log('=== API 기반 이미지 스트리밍 디버깅 ===');
    console.log('신청 데이터:', s);
    
    const imageUrls = s.imageUrls || [];
    console.log('이미지 URL 배열:', imageUrls);
    
    let images = '';
    if (imageUrls.length > 0) {
      // API 스트리밍 URL에서 이미지 ID 추출하고 이미지 엘리먼트 생성
      const imageElements = imageUrls.map((url, index) => {
        console.log('처리 중인 이미지 URL:', url);
        
        // URL에서 이미지 ID 추출 (/api/files/view/{imageId})
        const imageIdMatch = url.match(/\/api\/files\/view\/(\d+)/);
        if (!imageIdMatch) {
          console.warn('유효하지 않은 이미지 URL 형식:', url);
          return `<div class="w-40 h-28 bg-gray-200 rounded border flex items-center justify-center text-gray-500 text-xs">유효하지 않은 URL</div>`;
        }
        
        const imageId = parseInt(imageIdMatch[1]);
        const uniqueId = `submission-image-${imageId}-${index}`;
        
        console.log(`이미지 ID: ${imageId}, 고유 ID: ${uniqueId}`);
        
        return `
          <div class="relative">
            <img 
              id="${uniqueId}" 
              class="w-40 h-28 object-cover rounded border cursor-pointer hover:opacity-80 transition-opacity" 
              alt="차량 이미지 로딩 중..." 
              data-image-id="${imageId}"
              onclick="openImageModal(${imageId}, '${uniqueId}')"
            />
            <div class="absolute top-1 right-1 bg-black bg-opacity-50 text-white text-xs px-1 py-0.5 rounded">
              ${index + 1}
            </div>
          </div>
        `;
      });
      
      images = imageElements.join('');
    } else {
      images = '<div class="text-gray-400">이미지가 없습니다.</div>';
    }
    
    console.log('생성된 이미지 HTML 엘리먼트 개수:', imageUrls.length);
    
    const html = `
      <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <div class="text-sm text-gray-500">신청 ID</div>
          <div class="font-mono">${s.id}</div>
        </div>
        <div>
          <div class="text-sm text-gray-500">회원</div>
          <div>${s.memberName || '-'} <span class="text-gray-400 text-xs">(${s.memberId || '-'})</span></div>
        </div>
        <div>
          <div class="text-sm text-gray-500">차량명</div>
          <div>${s.carName || '-'}</div>
        </div>
        <div>
          <div class="text-sm text-gray-500">차량번호</div>
          <div>${s.rentCarNumber || '-'}</div>
        </div>
        <div>
          <div class="text-sm text-gray-500">요금</div>
          <div>${s.rentPrice ?? '-'}</div>
        </div>
        <div>
          <div class="text-sm text-gray-500">상태</div>
          <div>${s.status}</div>
        </div>
      </div>
      <div class="mt-6">
        <div class="text-sm text-gray-500 mb-2">업로드 이미지</div>
        <div class="flex flex-wrap gap-3">${images}</div>
      </div>`;
    document.getElementById('submission-detail').innerHTML = html;
    
    // HTML 삽입 후 API 기반 스트리밍으로 이미지 로드
    if (imageUrls.length > 0) {
      loadSubmissionImages(imageUrls);
    }
  }

  function bindActions(id){
    document.getElementById('approve-btn').addEventListener('click', async ()=>{
      if(!confirm('이 신청을 승인하시겠습니까?')) return;
      const r = await apiClient.post(`/api/admin/car-submissions/${id}/approve`, {});
      if(r.success){
        notification.success('승인되었습니다.');
        location.reload();
      }else{
        notification.error('승인에 실패했습니다.');
      }
    });
    document.getElementById('reject-btn').addEventListener('click', async ()=>{
      if(!confirm('이 신청을 반려하시겠습니까?')) return;
      const r = await apiClient.post(`/api/admin/car-submissions/${id}/reject`, {});
      if(r.success){
        notification.success('반려되었습니다.');
        location.reload();
      }else{
        notification.error('반려에 실패했습니다.');
      }
    });
  }

  /**
   * API 기반 스트리밍으로 신청 이미지들을 로드
   * @param {Array} imageUrls - 이미지 URL 배열
   */
  async function loadSubmissionImages(imageUrls) {
    console.log('=== API 기반 이미지 스트리밍 시작 ===');
    
    const imageConfigs = [];
    
    imageUrls.forEach((url, index) => {
      const imageIdMatch = url.match(/\/api\/files\/view\/(\d+)/);
      if (imageIdMatch) {
        const imageId = parseInt(imageIdMatch[1]);
        const uniqueId = `submission-image-${imageId}-${index}`;
        const imgElement = document.getElementById(uniqueId);
        
        if (imgElement) {
          imageConfigs.push({
            element: imgElement,
            imageId: imageId,
            options: {
              fallback: 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMTYwIiBoZWlnaHQ9IjExMiIgdmlld0JveD0iMCAwIDE2MCAxMTIiIGZpbGw9Im5vbmUiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+CjxyZWN0IHdpZHRoPSIxNjAiIGhlaWdodD0iMTEyIiBmaWxsPSIjRjNGNEY2Ii8+CjxwYXRoIGQ9Ik04MCA0NkM4My4zMTM3IDQ2IDg2IDQ4LjY4NjMgODYgNTJDODYgNTUuMzEzNyA4My4zMTM3IDU4IDgwIDU4Qzc2LjY4NjMgNTggNzQgNTUuMzEzNyA3NCA1MkM3NCA0OC42ODYzIDc2LjY4NjMgNDYgODAgNDZaIiBmaWxsPSIjOUNBM0FGIi8+CjxwYXRoIGQ9Ik02OCA2NEw3MiA2MEw4MCA2OEw4OCA2MEw5MiA2NEw5MiA3Nkg2OFY2NFoiIGZpbGw9IiM5Q0EzQUYiLz4KPC9zdmc+',
              onLoad: () => console.log(`이미지 로드 성공: ID ${imageId}`),
              onError: (error) => console.error(`이미지 로드 실패: ID ${imageId}`, error)
            }
          });
        }
      }
    });
    
    if (imageConfigs.length > 0) {
      try {
        await imageStreaming.loadMultipleImages(imageConfigs);
        console.log(`${imageConfigs.length}개 이미지 로드 완료`);
      } catch (error) {
        console.error('이미지 로드 중 오류:', error);
      }
    }
  }

  document.addEventListener('DOMContentLoaded', init);
})();

/**
 * 이미지 모달 열기 (전역 함수)
 * @param {number} imageId - 이미지 ID
 * @param {string} elementId - 클릭된 이미지 엘리먼트 ID
 */
function openImageModal(imageId, elementId) {
  console.log(`이미지 모달 열기: ID ${imageId}, Element ${elementId}`);
  
  // 모달이 이미 있으면 제거
  const existingModal = document.getElementById('image-modal');
  if (existingModal) {
    existingModal.remove();
  }
  
  // 모달 HTML 생성
  const modal = document.createElement('div');
  modal.id = 'image-modal';
  modal.className = 'fixed inset-0 bg-black bg-opacity-75 flex items-center justify-center z-50';
  modal.innerHTML = `
    <div class="relative max-w-4xl max-h-full p-4">
      <button 
        onclick="closeImageModal()" 
        class="absolute top-2 right-2 text-white bg-black bg-opacity-50 rounded-full w-8 h-8 flex items-center justify-center hover:bg-opacity-75 z-10"
      >
        ✕
      </button>
      <img 
        id="modal-image" 
        class="max-w-full max-h-full object-contain rounded" 
        alt="확대된 이미지"
      />
      <div class="absolute bottom-2 left-2 bg-black bg-opacity-50 text-white px-2 py-1 rounded text-sm">
        이미지 ID: ${imageId}
      </div>
      <div class="absolute bottom-2 right-2 space-x-2">
        <button 
          onclick="downloadModalImage(${imageId})" 
          class="bg-blue-600 hover:bg-blue-700 text-white px-3 py-1 rounded text-sm"
        >
          다운로드
        </button>
      </div>
    </div>
  `;
  
  document.body.appendChild(modal);
  
  // 모달 이미지 로드
  const modalImg = document.getElementById('modal-image');
  imageStreaming.loadImage(modalImg, imageId, {
    fallback: 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNDAwIiBoZWlnaHQ9IjMwMCIgdmlld0JveD0iMCAwIDQwMCAzMDAiIGZpbGw9Im5vbmUiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+CjxyZWN0IHdpZHRoPSI0MDAiIGhlaWdodD0iMzAwIiBmaWxsPSIjRjNGNEY2Ii8+Cjx0ZXh0IHg9IjIwMCIgeT0iMTUwIiB0ZXh0LWFuY2hvcj0ibWlkZGxlIiBmaWxsPSIjOUNBM0FGIiBmb250LWZhbWlseT0ic2Fucy1zZXJpZiIgZm9udC1zaXplPSIxNiI+7J207Jq47KeA66W8IOyVieuhnOyImO2VoCDsiJjsl4bsirXri4jri6QuPC90ZXh0Pgo8L3N2Zz4=',
    onLoad: () => console.log(`모달 이미지 로드 성공: ID ${imageId}`),
    onError: (error) => {
      console.error(`모달 이미지 로드 실패: ID ${imageId}`, error);
      notification.error('이미지를 불러올 수 없습니다.');
    }
  });
  
  // ESC 키로 모달 닫기
  const handleEscKey = (e) => {
    if (e.key === 'Escape') {
      closeImageModal();
      document.removeEventListener('keydown', handleEscKey);
    }
  };
  document.addEventListener('keydown', handleEscKey);
  
  // 모달 배경 클릭으로 닫기
  modal.addEventListener('click', (e) => {
    if (e.target === modal) {
      closeImageModal();
    }
  });
}

/**
 * 이미지 모달 닫기 (전역 함수)
 */
function closeImageModal() {
  const modal = document.getElementById('image-modal');
  if (modal) {
    modal.remove();
  }
}

/**
 * 모달에서 이미지 다운로드 (전역 함수)
 * @param {number} imageId - 이미지 ID
 */
function downloadModalImage(imageId) {
  imageStreaming.downloadImage(imageId, `submission_image_${imageId}.jpg`);
}


