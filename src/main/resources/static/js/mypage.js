// Dream Drive - 마이페이지 JavaScript

class MyPage {
    constructor() {
        this.currentUser = null;
        this.isEditing = false;
        
        this.init();
    }

    init() {
        this.checkAuthentication();
        this.setupEventListeners();
        this.loadUserProfile();
    }

    checkAuthentication() {
        const token = apiClient.getAuthToken();
        const userInfo = localStorage.getItem('userInfo');
        
        if (!token || !userInfo) {
            // 인증되지 않은 경우 로그인 페이지로 리다이렉트
            window.location.href = '/login';
            return;
        }

        this.currentUser = JSON.parse(userInfo);
        this.displayUserName();
    }

    displayUserName() {
        const userNameElement = document.getElementById('user-name');
        if (userNameElement && this.currentUser) {
            userNameElement.textContent = `${this.currentUser.name}님`;
        }
    }

    setupEventListeners() {
        // 탭 전환
        document.querySelectorAll('.tab-btn').forEach(btn => {
            btn.addEventListener('click', (e) => this.handleTabClick(e));
        });

        // 프로필 수정
        document.getElementById('edit-btn').addEventListener('click', () => this.toggleEdit(true));
        document.getElementById('cancel-btn').addEventListener('click', () => this.toggleEdit(false));
        document.getElementById('profile-form').addEventListener('submit', (e) => this.handleProfileSubmit(e));

        // 로그아웃
        document.getElementById('logout-btn').addEventListener('click', () => this.handleLogout());

        // 모달 닫기
        document.querySelectorAll('.close-modal').forEach(btn => {
            btn.addEventListener('click', () => this.closeModal());
        });
        // 차량 등록 신청: 통합된 신청 제출 (차량 정보 + 이미지)
        const submissionForm = document.getElementById('car-submission-form');
        const submitBtn = document.getElementById('submit-btn');
        
        if (submissionForm) {
            submissionForm.addEventListener('submit', async (e) => {
                e.preventDefault();
                
                // 버튼 비활성화 및 로딩 표시
                const btnText = submitBtn.querySelector('.btn-text');
                const loader = submitBtn.querySelector('.loader');
                
                submitBtn.disabled = true;
                btnText.classList.add('hidden');
                loader.classList.remove('hidden');
                
                try {
                    // FormData로 차량 정보 + 이미지 파일들 함께 전송
                    const formData = new FormData();
                    
                    // 차량 정보 추가
                    const carName = submissionForm.querySelector('[name="carName"]').value;
                    const rentCarNumber = submissionForm.querySelector('[name="rentCarNumber"]').value;
                    const rentPrice = submissionForm.querySelector('[name="rentPrice"]').value;
                    
                    formData.append('carName', carName);
                    formData.append('rentCarNumber', rentCarNumber);
                    formData.append('rentPrice', rentPrice);
                    
                    // 이미지 파일들 추가
                    const imagesInput = submissionForm.querySelector('[name="images"]');
                    if (imagesInput && imagesInput.files.length > 0) {
                        for (let i = 0; i < imagesInput.files.length; i++) {
                            formData.append('images', imagesInput.files[i]);
                        }
                    }
                    
                    // API 호출
                    const response = await fetch('/api/MyPage/car-submission', {
                        method: 'POST',
                        headers: {
                            'Authorization': 'Bearer ' + apiClient.getAuthToken()
                        },
                        body: formData
                    });
                    
                    const result = await response.json();
                    
                    if (result.success) {
                        notification.success('차량 등록 신청이 완료되었습니다!');
                        submissionForm.reset();
                        document.getElementById('submission-message').innerHTML = 
                            '<div class="text-green-600">✓ 신청이 완료되었습니다. 관리자 승인을 기다려주세요.</div>';
                    } else {
                        notification.error(result.message || '신청에 실패했습니다.');
                        document.getElementById('submission-message').innerHTML = 
                            '<div class="text-red-600">✗ ' + (result.message || '신청에 실패했습니다.') + '</div>';
                    }
                    
                } catch (err) {
                    console.error('차량 등록 신청 오류:', err);
                    notification.error('신청 처리 중 오류가 발생했습니다.');
                    document.getElementById('submission-message').innerHTML = 
                        '<div class="text-red-600">✗ 신청 처리 중 오류가 발생했습니다.</div>';
                } finally {
                    // 버튼 상태 복원
                    submitBtn.disabled = false;
                    btnText.classList.remove('hidden');
                    loader.classList.add('hidden');
                }
            });
        }

        // 모달 배경 클릭 시 닫기
        document.getElementById('reservation-detail-modal').addEventListener('click', (e) => {
            if (e.target.classList.contains('modal-backdrop')) {
                this.closeModal();
            }
        });
    }

    handleTabClick(e) {
        const tabName = e.currentTarget.getAttribute('data-tab');
        
        // 탭 버튼 활성화 상태 변경
        document.querySelectorAll('.tab-btn span').forEach(span => {
            span.className = 'py-2 px-1 border-b-2 border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300 font-medium text-sm';
        });
        e.currentTarget.querySelector('span').className = 'py-2 px-1 border-b-2 border-indigo-500 text-indigo-600 font-medium text-sm';

        // 탭 패널 전환
        document.querySelectorAll('.tab-panel').forEach(panel => {
            panel.classList.add('hidden');
        });
        document.getElementById(`${tabName}-tab`).classList.remove('hidden');

        // 탭에 따른 데이터 로드
        switch (tabName) {
            case 'reservations':
                this.loadReservations();
                break;
            case 'history':
                // 이용 기록 로드 (추후 구현)
                break;
        }
    }

    async loadUserProfile() {
        if (!this.currentUser) return;

        try {
            loading.show();
            
            // 현재 localStorage의 사용자 정보를 폼에 표시
            // 실제로는 API를 호출하여 최신 정보를 가져와야 함
            const form = document.getElementById('profile-form');
            form.name.value = this.currentUser.name || '';
            form.email.value = this.currentUser.email || '';
            
            // TODO: 실제 API 호출로 전체 프로필 정보 가져오기
            // const response = await apiClient.get(`/api/MyPage/${userId}`);
            
        } catch (error) {
            console.error('프로필 로드 오류:', error);
            notification.error('프로필 정보를 불러오는데 실패했습니다.');
        } finally {
            loading.hide();
        }
    }

    toggleEdit(enable) {
        this.isEditing = enable;
        const form = document.getElementById('profile-form');
        const editBtn = document.getElementById('edit-btn');
        const cancelBtn = document.getElementById('cancel-btn');
        const saveBtn = document.getElementById('save-btn');

        const fields = ['name', 'phone', 'address']; // email은 수정 불가

        fields.forEach(fieldName => {
            const field = form[fieldName];
            if (enable) {
                field.removeAttribute('readonly');
                field.classList.remove('bg-gray-50', 'text-gray-500');
                field.classList.add('bg-white', 'text-gray-900', 'focus:ring-indigo-500', 'focus:border-indigo-500');
            } else {
                field.setAttribute('readonly', true);
                field.classList.add('bg-gray-50', 'text-gray-500');
                field.classList.remove('bg-white', 'text-gray-900', 'focus:ring-indigo-500', 'focus:border-indigo-500');
            }
        });

        if (enable) {
            editBtn.classList.add('hidden');
            cancelBtn.classList.remove('hidden');
            saveBtn.classList.remove('hidden');
        } else {
            editBtn.classList.remove('hidden');
            cancelBtn.classList.add('hidden');
            saveBtn.classList.add('hidden');
            
            // 수정 취소 시 원래 값으로 복원
            this.loadUserProfile();
        }
    }

    async handleProfileSubmit(e) {
        e.preventDefault();
        
        if (!this.isEditing) return;

        const form = e.target;
        const formData = utils.formToObject(form);

        try {
            loading.show();
            
            // TODO: 실제 API 호출로 프로필 업데이트
            // const response = await apiClient.post(`/api/MyPage/${userId}/change`, formData);
            
            // 임시로 localStorage 업데이트
            const updatedUserInfo = { ...this.currentUser, ...formData };
            localStorage.setItem('userInfo', JSON.stringify(updatedUserInfo));
            this.currentUser = updatedUserInfo;
            
            notification.success('프로필이 성공적으로 업데이트되었습니다.');
            this.toggleEdit(false);
            
        } catch (error) {
            console.error('프로필 업데이트 오류:', error);
            notification.error('프로필 업데이트에 실패했습니다.');
        } finally {
            loading.hide();
        }
    }

    async loadReservations() {
        const container = document.getElementById('reservations-container');
        
        try {
            loading.show();
            
            console.log('예약 내역 로드 시도 중...');
            
            // 토큰 상태 확인
            const token = apiClient.getAuthToken();
            console.log('토큰 상태:', token ? '토큰 존재' : '토큰 없음');
            
            const response = await apiClient.get('/api/MyPage/reservation/list');
            
            console.log('예약 내역 응답:', response);
            
            if (response.success && response.data) {
                this.renderReservations(response.data);
                console.log(`예약 내역 로드 성공: ${response.data.length}건`);
            } else if (response.success && (!response.data || response.data.length === 0)) {
                container.innerHTML = '<p class="text-gray-500 text-center py-8">예약 내역이 없습니다.</p>';
                console.log('예약 내역 없음');
            } else {
                console.error('예약 내역 로드 실패:', response.message);
                container.innerHTML = `<p class="text-red-500 text-center py-8">예약 내역을 불러오는데 실패했습니다.<br><small>${response.message || ''}</small></p>`;
                notification.error('예약 내역 로드 실패: ' + (response.message || '알 수 없는 오류'));
            }
            
        } catch (error) {
            console.error('예약 내역 로드 오류:', error);
            
            let errorMessage = '예약 내역을 불러오는데 실패했습니다.';
            
            // 상세 오류 메시지
            if (error.message) {
                errorMessage += `<br><small>오류: ${error.message}</small>`;
            }
            
            // 인증 오류 감지
            if (error.message && error.message.includes('인증')) {
                errorMessage += '<br><small>로그인을 다시 해주세요.</small>';
                setTimeout(() => {
                    window.location.href = '/login';
                }, 3000);
            }
            
            container.innerHTML = `<div class="text-red-500 text-center py-8">
                <p>${errorMessage}</p>
                <button onclick="myPage.loadReservations()" class="mt-4 bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600">
                    다시 시도
                </button>
            </div>`;
            
            notification.error('예약 내역 로드 중 오류가 발생했습니다.');
        } finally {
            loading.hide();
        }
    }

    renderReservations(reservations) {
        const container = document.getElementById('reservations-container');
        
        if (!reservations || reservations.length === 0) {
            container.innerHTML = '<p class="text-gray-500 text-center py-8">예약 내역이 없습니다.</p>';
            return;
        }

        container.innerHTML = '';
        
        reservations.forEach(reservation => {
            const reservationCard = this.createReservationCard(reservation);
            container.appendChild(reservationCard);
        });
    }

    createReservationCard(reservation) {
        const div = document.createElement('div');
        div.className = 'bg-gray-50 rounded-lg p-4 border border-gray-200';
        
        const statusClass = this.getStatusClass(reservation.status);
        const statusText = this.getStatusText(reservation.status);
        
        div.innerHTML = `
            <div class="flex justify-between items-start">
                <div class="flex-1">
                    <h4 class="font-medium text-gray-900">${reservation.carName || '차량명 없음'}</h4>
                    <p class="text-sm text-gray-600 mt-1">${reservation.rentCarNumber || ''}</p>
                    <div class="mt-2 text-sm text-gray-500">
                        <p>대여일: ${utils.formatDate(reservation.rentStart, true)}</p>
                        <p>반납일: ${utils.formatDate(reservation.rentEnd, true)}</p>
                        <p>총 금액: ${utils.formatCurrency(reservation.totalPrice)}</p>
                    </div>
                </div>
                <div class="text-right">
                    <span class="badge ${statusClass}">${statusText}</span>
                    <div class="mt-2 space-x-2">
                        <button onclick="myPage.viewReservationDetail('${reservation.id}')" 
                                class="text-indigo-600 hover:text-indigo-800 text-sm">
                            상세보기
                        </button>
                        ${this.canCancelReservation(reservation) ? 
                            `<button onclick="myPage.cancelReservation('${reservation.id}')" 
                                     class="text-red-600 hover:text-red-800 text-sm">
                                취소
                             </button>` : ''
                        }
                    </div>
                </div>
            </div>
        `;
        
        return div;
    }

    getStatusClass(status) {
        switch (status) {
            case 'CONFIRMED': return 'badge-success';
            case 'PENDING': return 'badge-warning';
            case 'CANCELLED': return 'badge-error';
            default: return 'badge-primary';
        }
    }

    getStatusText(status) {
        switch (status) {
            case 'CONFIRMED': return '확정';
            case 'PENDING': return '대기';
            case 'CANCELLED': return '취소';
            case 'COMPLETED': return '완료';
            default: return '알 수 없음';
        }
    }

    canCancelReservation(reservation) {
        // 예약 시작일이 현재 시간보다 미래이고, 상태가 확정 또는 대기인 경우 취소 가능
        const rentStart = new Date(reservation.rentStart);
        const now = new Date();
        return rentStart > now && (reservation.status === 'CONFIRMED' || reservation.status === 'PENDING');
    }

    async viewReservationDetail(reservationId) {
        try {
            loading.show();
            
            const response = await apiClient.get(`/api/MyPage/reservation/list/${reservationId}`);
            
            if (response.success && response.data) {
                this.showReservationDetailModal(response.data);
            } else {
                notification.error('예약 상세 정보를 불러올 수 없습니다.');
            }
            
        } catch (error) {
            console.error('예약 상세 정보 로드 오류:', error);
            notification.error('예약 상세 정보를 불러오는데 실패했습니다.');
        } finally {
            loading.hide();
        }
    }

    showReservationDetailModal(reservation) {
        const modal = document.getElementById('reservation-detail-modal');
        const content = document.getElementById('modal-content');
        
        const statusClass = this.getStatusClass(reservation.status);
        const statusText = this.getStatusText(reservation.status);
        
        content.innerHTML = `
            <div class="space-y-4">
                <div class="flex justify-between items-center">
                    <h4 class="font-semibold text-lg">${reservation.carName}</h4>
                    <span class="badge ${statusClass}">${statusText}</span>
                </div>
                
                <div class="grid grid-cols-2 gap-4 text-sm">
                    <div>
                        <span class="text-gray-600">차량번호:</span>
                        <p class="font-medium">${reservation.rentCarNumber}</p>
                    </div>
                    <div>
                        <span class="text-gray-600">예약일:</span>
                        <p class="font-medium">${utils.formatDate(reservation.createdAt)}</p>
                    </div>
                    <div>
                        <span class="text-gray-600">대여일시:</span>
                        <p class="font-medium">${utils.formatDate(reservation.rentStart, true)}</p>
                    </div>
                    <div>
                        <span class="text-gray-600">반납일시:</span>
                        <p class="font-medium">${utils.formatDate(reservation.rentEnd, true)}</p>
                    </div>
                    <div>
                        <span class="text-gray-600">대여일수:</span>
                        <p class="font-medium">${reservation.days}일</p>
                    </div>
                    <div>
                        <span class="text-gray-600">총 금액:</span>
                        <p class="font-medium text-lg text-indigo-600">${utils.formatCurrency(reservation.totalPrice)}</p>
                    </div>
                </div>
                
                ${this.canCancelReservation(reservation) ? `
                    <div class="pt-4 border-t border-gray-200">
                        <button onclick="myPage.cancelReservationFromModal('${reservation.id}')" 
                                class="w-full bg-red-600 text-white py-2 px-4 rounded-md hover:bg-red-700">
                            예약 취소
                        </button>
                    </div>
                ` : ''}
            </div>
        `;
        
        modal.classList.remove('hidden');
    }

    closeModal() {
        document.getElementById('reservation-detail-modal').classList.add('hidden');
    }

    async cancelReservation(reservationId) {
        if (!confirm('정말로 이 예약을 취소하시겠습니까?')) {
            return;
        }

        try {
            loading.show();
            
            const response = await apiClient.delete(`/api/MyPage/reservation/list/cancel/${reservationId}`);
            
            if (response.success) {
                notification.success('예약이 성공적으로 취소되었습니다.');
                this.loadReservations(); // 목록 새로고침
            } else {
                notification.error('예약 취소에 실패했습니다.');
            }
            
        } catch (error) {
            console.error('예약 취소 오류:', error);
            notification.error('예약 취소 중 오류가 발생했습니다.');
        } finally {
            loading.hide();
        }
    }

    async cancelReservationFromModal(reservationId) {
        await this.cancelReservation(reservationId);
        this.closeModal();
    }

    handleLogout() {
        if (confirm('정말 로그아웃하시겠습니까?')) {
            // 로컬 데이터 삭제
            apiClient.removeAuthToken();
            localStorage.removeItem('userInfo');
            
            // 서버에 로그아웃 요청
            apiClient.post('/auth/logout').finally(() => {
                window.location.href = '/';
            });
        }
    }
}

// 전역 인스턴스
let myPage;

// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', () => {
    myPage = new MyPage();
});