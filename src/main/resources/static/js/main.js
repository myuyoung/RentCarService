// Dream Drive - 메인 페이지 JavaScript

/**
 * 메인 페이지 관리 클래스
 */
class MainPage {
    constructor() {
        this.carListContainer = document.getElementById('car-list-container');
        this.searchForm = document.getElementById('car-search-form');
        this.init();
    }

    init() {
        this.updateHeaderForAuth();
        this.setupEventListeners();
        this.loadRecommendedCars();
    }

    updateHeaderForAuth() {
        const token = apiClient.getAuthToken();
        const userInfo = JSON.parse(localStorage.getItem('userInfo') || 'null');
        
        const authButtonsContainer = document.querySelector('.hidden.md\\:block .ml-4.flex.items-center');
        const mobileAuthContainer = document.querySelector('#mobile-menu .pt-4.pb-3');
        
        if (token && userInfo) {
            // 로그인된 상태
            const isAdmin = userInfo.role === 'ADMIN';
            if (authButtonsContainer) {
                authButtonsContainer.innerHTML = isAdmin
                    ? `
                        <span class="text-gray-600 text-sm mr-4">${userInfo.name}님</span>
                        <button id="logout-btn" class="bg-red-100 text-red-600 hover:bg-red-200 ml-2 px-4 py-2 rounded-md text-sm font-medium">로그아웃</button>
                      `
                    : `
                        <span class="text-gray-600 text-sm mr-4">${userInfo.name}님</span>
                        <a href="/mypage" class="bg-gray-100 text-gray-600 hover:bg-gray-200 px-4 py-2 rounded-md text-sm font-medium">마이페이지</a>
                        <button id="logout-btn" class="bg-red-100 text-red-600 hover:bg-red-200 ml-2 px-4 py-2 rounded-md text-sm font-medium">로그아웃</button>
                      `;
            }

            if (mobileAuthContainer) {
                mobileAuthContainer.innerHTML = isAdmin
                    ? `
                        <div class="flex items-center px-5 mb-3">
                            <span class="text-gray-600 text-sm">${userInfo.name}님</span>
                        </div>
                        <div class="px-5">
                            <button id="mobile-logout-btn" class="w-full bg-red-100 text-red-600 hover:bg-red-200 text-center block px-4 py-2 rounded-md text-sm font-medium">로그아웃</button>
                        </div>
                      `
                    : `
                        <div class="flex items-center px-5 mb-3">
                            <span class="text-gray-600 text-sm">${userInfo.name}님</span>
                        </div>
                        <div class="px-5">
                            <a href="/mypage" class="w-full bg-gray-100 text-gray-600 hover:bg-gray-200 text-center block px-4 py-2 rounded-md text-sm font-medium mb-2">마이페이지</a>
                            <button id="mobile-logout-btn" class="w-full bg-red-100 text-red-600 hover:bg-red-200 text-center block px-4 py-2 rounded-md text-sm font-medium">로그아웃</button>
                        </div>
                      `;
            }

            // 로그아웃 버튼 이벤트 추가
            document.getElementById('logout-btn')?.addEventListener('click', this.handleLogout);
            document.getElementById('mobile-logout-btn')?.addEventListener('click', this.handleLogout);

            // 관리자 전용 별도 링크 추가는 하지 않음 (전역에서 마이페이지 → 관리자 메뉴로 치환)
        }
    }

    handleLogout() {
        if (confirm('정말 로그아웃하시겠습니까?')) {
            // 로컬 데이터 삭제
            apiClient.removeAuthToken();
            localStorage.removeItem('userInfo');
            
            // 서버에 로그아웃 요청
            apiClient.post('/auth/logout').finally(() => {
                location.reload(); // 페이지 새로고침
            });
        }
    }

    setupEventListeners() {
        // 차량 검색 폼 제출 이벤트
        if (this.searchForm) {
            this.searchForm.addEventListener('submit', (e) => {
                e.preventDefault();
                this.handleCarSearch();
            });
        }
    }

    /**
     * 추천 차량 목록 로드
     */
    async loadRecommendedCars() {
        if (!this.carListContainer) return;

        try {
            loading.show();
            
            // API 호출: 랭킹 기준 차량 목록
            const response = await apiClient.get('/api/rentcars/rank?page=0&size=4');
            
            if (response.success && response.data && response.data.content) {
                this.renderCarList(response.data.content);
            } else {
                this.showCarListError('추천 차량 정보를 불러오는 데 실패했습니다.');
            }
        } catch (error) {
            console.error('차량 정보 로드 오류:', error);
            this.showCarListError('차량 정보를 불러올 수 없습니다.');
        } finally {
            loading.hide();
        }
    }

    /**
     * 차량 목록 렌더링
     */
    renderCarList(cars) {
        if (!cars || cars.length === 0) {
            this.carListContainer.innerHTML = `
                <div class="col-span-full text-center py-12">
                    <div class="text-gray-500 text-lg">현재 이용 가능한 차량이 없습니다.</div>
                </div>
            `;
            return;
        }

        this.carListContainer.innerHTML = '';
        
        cars.forEach(car => {
            const carElement = this.createCarElement(car);
            this.carListContainer.appendChild(carElement);
        });
    }

    /**
     * 차량 카드 엘리먼트 생성
     */
    createCarElement(car) {
        const carDiv = document.createElement('div');
        carDiv.className = 'group relative car-card';
        
        // 차량 이미지 URL 처리
        const imageUrl = car.imageUrl || 
            `https://placehold.co/600x400/e2e8f0/4a5568?text=${encodeURIComponent(car.name)}`;
        
        carDiv.innerHTML = `
            <div class="w-full min-h-80 bg-gray-200 aspect-w-1 aspect-h-1 rounded-md overflow-hidden group-hover:opacity-75 lg:h-80 lg:aspect-none">
                <img src="${imageUrl}" 
                     alt="${car.name}" 
                     class="car-image"
                     onerror="this.src='https://placehold.co/600x400/e2e8f0/4a5568?text=${encodeURIComponent(car.name)}'">
            </div>
            <div class="mt-4 flex justify-between">
                <div class="flex-1">
                    <h3 class="text-sm text-gray-700 font-medium">
                        <a href="/car-detail?id=${car.id || ''}" class="hover:text-indigo-600">
                            <span aria-hidden="true" class="absolute inset-0"></span>
                            ${car.name}
                        </a>
                    </h3>
                    <p class="mt-1 text-sm text-gray-500">${car.rentCarNumber || '정보 없음'}</p>
                    <div class="mt-2 flex flex-wrap gap-1">
                        ${car.category ? `<span class="badge badge-primary">${car.category}</span>` : ''}
                        ${car.fuelType ? `<span class="badge badge-success">${car.fuelType}</span>` : ''}
                    </div>
                </div>
                <div class="text-right">
                    <p class="text-lg font-bold text-gray-900">${utils.formatCurrency(car.rentPrice)}</p>
                    <p class="text-xs text-gray-500">/ 일</p>
                </div>
            </div>
        `;

        return carDiv;
    }

    /**
     * 차량 목록 오류 표시
     */
    showCarListError(message) {
        this.carListContainer.innerHTML = `
            <div class="col-span-full text-center py-12">
                <div class="text-red-500 text-lg mb-4">⚠️</div>
                <div class="text-gray-500">${message}</div>
                <button onclick="mainPage.loadRecommendedCars()" 
                        class="mt-4 btn-primary px-4 py-2 rounded-md text-white">
                    다시 시도
                </button>
            </div>
        `;
    }

    /**
     * 차량 검색 처리
     */
    async handleCarSearch() {
        const formData = utils.formToObject(this.searchForm);
        const params = new URLSearchParams();

        // UI 필드명에 맞춰 파라미터 구성
        // 차종: ALL/SMALL/MEDIUM/LARGE (서버 enum과 일치)
        if (formData['car-type'] && formData['car-type'] !== 'ALL') {
            params.set('segment', formData['car-type']);
        }
        // 연료: ALL/GASOLINE/DIESEL/ELECTRIC/HYBRID (서버 enum과 일치)
        if (formData['fuel-type'] && formData['fuel-type'] !== 'ALL') {
            params.set('fuelType', formData['fuel-type']);
        }
        // 가격 필드 추가 전달 및 검증
        const minPrice = parseInt(formData['min-price'], 10);
        const maxPrice = parseInt(formData['max-price'], 10);
        if (!Number.isNaN(minPrice)) params.set('minPrice', String(minPrice));
        if (!Number.isNaN(maxPrice)) params.set('maxPrice', String(maxPrice));
        if (!Number.isNaN(minPrice) && !Number.isNaN(maxPrice) && maxPrice < minPrice) {
            notification.warning('최대 가격은 최소 가격보다 크거나 같아야 합니다.');
            return;
        }
        // 위치는 현재 서버 검색 조건에 없음 → 키워드로 포함
        if (formData['location']) {
            params.set('q', formData['location']);
        }

        try {
            // 별도 검색 결과 페이지로 이동
            window.location.href = `/search?${params.toString()}`;
        } catch (error) {
            console.error('검색 오류:', error);
            notification.error('검색 중 오류가 발생했습니다.');
        } finally {
            // no-op
        }
    }
}

/**
 * 페이지별 초기화 함수들
 */
const pageInitializers = {
    // 메인 페이지 초기화
    initMainPage() {
        window.mainPage = new MainPage();
    },

    // 로그인 페이지 초기화
    initLoginPage() {
        // 로그인 페이지 관련 로직이 있다면 여기에 추가
    },

    // 회원가입 페이지 초기화
    initRegisterPage() {
        // 회원가입 페이지 관련 로직이 있다면 여기에 추가
    }
};

/**
 * 페이지 로드 시 해당 페이지에 맞는 초기화 실행
 */
document.addEventListener('DOMContentLoaded', () => {
    const path = window.location.pathname;
    
    // 페이지별로 다른 초기화 실행
    switch (path) {
        case '/':
            pageInitializers.initMainPage();
            break;
        case '/login':
            pageInitializers.initLoginPage();
            break;
        case '/register':
            pageInitializers.initRegisterPage();
            break;
        default:
            // 기본적으로 공통 기능들만 초기화
            console.log('페이지 로드 완료:', path);
    }
});