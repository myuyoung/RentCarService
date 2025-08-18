// Dream Drive - 공통 JavaScript 기능

/**
 * API 호출을 위한 공통 함수
 */
class ApiClient {
    constructor() {
        this.baseURL = '';
        this.defaultHeaders = {
            'Content-Type': 'application/json'
        };
    }

    // JWT 토큰을 Authorization 헤더에 추가
    setAuthToken(token) {
        if (token) {
            this.defaultHeaders['Authorization'] = `Bearer ${token}`;
        } else {
            delete this.defaultHeaders['Authorization'];
        }
    }

    // 로컬 스토리지에서 토큰 가져오기
    getAuthToken() {
        return localStorage.getItem('accessToken');
    }

    // 토큰 저장
    saveAuthToken(token) {
        localStorage.setItem('accessToken', token);
        this.setAuthToken(token);
        // 쿠키에도 저장하여 페이지 네비게이션 시 자동 전송되도록
        try {
            document.cookie = `accessToken=${token}; Path=/; SameSite=Lax`;
        } catch (_) {}
    }

    // 토큰 삭제
    removeAuthToken() {
        localStorage.removeItem('accessToken');
        delete this.defaultHeaders['Authorization'];
        try {
            document.cookie = 'accessToken=; Max-Age=0; Path=/;';
        } catch (_) {}
    }

    // GET 요청
    async get(url, options = {}) {
        return this.request(url, {
            method: 'GET',
            ...options
        });
    }

    // POST 요청
    async post(url, data, options = {}) {
        return this.request(url, {
            method: 'POST',
            body: JSON.stringify(data),
            ...options
        });
    }

  // multipart/form-data POST (FormData)
  async postForm(url, formData, options = {}) {
    const headers = { ...this.defaultHeaders, ...options.headers };
    // FormData 사용 시 브라우저가 boundary가 포함된 Content-Type을 설정하도록 제거
    if (headers['Content-Type']) {
      delete headers['Content-Type'];
    }
    return this.request(url, {
      method: 'POST',
      body: formData,
      headers,
      ...options
    });
  }

    // PUT 요청
    async put(url, data, options = {}) {
        return this.request(url, {
            method: 'PUT',
            body: JSON.stringify(data),
            ...options
        });
    }

    // DELETE 요청
    async delete(url, options = {}) {
        return this.request(url, {
            method: 'DELETE',
            ...options
        });
    }

    // 기본 request 메소드
    async request(url, options = {}) {
    // 헤더 병합. FormData 사용 시 Content-Type 제거하여 브라우저가 boundary를 포함해 자동 설정하도록 함
    const mergedHeaders = { ...this.defaultHeaders, ...options.headers };
    const isFormData = typeof FormData !== 'undefined' && options.body instanceof FormData;
    if (isFormData && mergedHeaders['Content-Type']) {
      delete mergedHeaders['Content-Type'];
    }

    const config = {
      headers: mergedHeaders,
            credentials: 'include', // 쿠키 포함
            ...options
        };

        try {
            const response = await fetch(this.baseURL + url, config);
            const result = await response.json();

            // 토큰 만료 시 refresh token으로 갱신 시도
            if (response.status === 401 && result.message?.includes('토큰')) {
                const refreshResult = await this.refreshToken();
                if (refreshResult.success) {
                    // 새 토큰으로 재요청
                    config.headers['Authorization'] = `Bearer ${refreshResult.data.token}`;
                    const retryResponse = await fetch(this.baseURL + url, config);
                    return await retryResponse.json();
                } else {
                    // 리프레시 실패 시 로그인 페이지로
                    this.redirectToLogin();
                    throw new Error('인증이 만료되었습니다. 다시 로그인해주세요.');
                }
            }

            return result;
        } catch (error) {
            console.error('API 요청 오류:', error);
            throw error;
        }
    }

    // 토큰 갱신
    async refreshToken() {
        try {
            const response = await fetch('/auth/refresh-token', {
                method: 'POST',
                credentials: 'include'
            });
            const result = await response.json();

            if (result.success) {
                this.saveAuthToken(result.data.token);
            }

            return result;
        } catch (error) {
            console.error('토큰 갱신 실패:', error);
            return { success: false };
        }
    }

    // 로그인 페이지로 리다이렉트
    redirectToLogin() {
        this.removeAuthToken();
        window.location.href = '/login';
    }
}

// 전역 API 클라이언트 인스턴스
const apiClient = new ApiClient();

/**
 * 알림 메시지 표시 함수
 */
class NotificationManager {
    constructor() {
        this.container = this.createContainer();
    }

    createContainer() {
        let container = document.getElementById('notification-container');
        if (!container) {
            container = document.createElement('div');
            container.id = 'notification-container';
            container.style.cssText = `
                position: fixed;
                top: 20px;
                right: 20px;
                z-index: 1000;
                max-width: 400px;
            `;
            document.body.appendChild(container);
        }
        return container;
    }

    show(message, type = 'info', duration = 3000) {
        const notification = document.createElement('div');
        notification.className = `alert alert-${type}`;
        notification.innerHTML = `
            <span>${message}</span>
            <button type="button" class="ml-auto text-sm" onclick="this.parentElement.remove()">
                ✕
            </button>
        `;

        this.container.appendChild(notification);

        // 자동 제거
        if (duration > 0) {
            setTimeout(() => {
                if (notification.parentElement) {
                    notification.remove();
                }
            }, duration);
        }

        return notification;
    }

    success(message, duration = 3000) {
        return this.show(message, 'success', duration);
    }

    error(message, duration = 5000) {
        return this.show(message, 'error', duration);
    }

    warning(message, duration = 4000) {
        return this.show(message, 'warning', duration);
    }
}

// 전역 알림 관리자 인스턴스
const notification = new NotificationManager();

/**
 * 로딩 상태 관리
 */
class LoadingManager {
    constructor() {
        this.loadingCount = 0;
        this.overlay = this.createOverlay();
    }

    createOverlay() {
        const overlay = document.createElement('div');
        overlay.id = 'loading-overlay';
        overlay.style.cssText = `
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
            display: none;
            justify-content: center;
            align-items: center;
            z-index: 9999;
        `;
        
        overlay.innerHTML = `
            <div style="background: white; padding: 20px; border-radius: 8px; text-align: center;">
                <div class="loading-spinner"></div>
                <div style="margin-top: 10px; color: #374151;">로딩 중...</div>
            </div>
        `;
        
        document.body.appendChild(overlay);
        return overlay;
    }

    show() {
        this.loadingCount++;
        this.overlay.style.display = 'flex';
    }

    hide() {
        this.loadingCount = Math.max(0, this.loadingCount - 1);
        if (this.loadingCount === 0) {
            this.overlay.style.display = 'none';
        }
    }
}

// 전역 로딩 관리자 인스턴스
const loading = new LoadingManager();

/**
 * 유틸리티 함수들
 */
const utils = {
    // 숫자를 한국 통화 형식으로 포맷
    formatCurrency(amount) {
        return new Intl.NumberFormat('ko-KR', {
            style: 'currency',
            currency: 'KRW'
        }).format(amount);
    },

    // 날짜를 한국 형식으로 포맷
    formatDate(date, includeTime = false) {
        const options = {
            year: 'numeric',
            month: 'long',
            day: 'numeric'
        };

        if (includeTime) {
            options.hour = '2-digit';
            options.minute = '2-digit';
        }

        return new Date(date).toLocaleDateString('ko-KR', options);
    },

    // 폼 데이터를 객체로 변환
    formToObject(form) {
        const formData = new FormData(form);
        const object = {};
        formData.forEach((value, key) => {
            object[key] = value;
        });
        return object;
    },

    // URL 파라미터 파싱
    parseURLParams() {
        const params = new URLSearchParams(window.location.search);
        const result = {};
        for (const [key, value] of params) {
            result[key] = value;
        }
        return result;
    },

    // 디바운싱 함수
    debounce(func, wait) {
        let timeout;
        return function executedFunction(...args) {
            const later = () => {
                clearTimeout(timeout);
                func(...args);
            };
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
        };
    },

    // 이메일 유효성 검증
    isValidEmail(email) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    },

    // 전화번호 유효성 검증 (한국)
    isValidPhoneNumber(phone) {
        const phoneRegex = /^01[0-9]-?[0-9]{3,4}-?[0-9]{4}$/;
        return phoneRegex.test(phone);
    }
};

/**
 * 【 통합 이미지 관리 】이미지 관리 클래스
 * API 스트리밍과 정적 리소스 방식을 모두 지원
 */
class ImageStreamingManager {
    constructor() {
        this.imageCache = new Map(); // 이미지 URL 캐시
        this.loadingImages = new Set(); // 로딩 중인 이미지 ID 추적
        this.preferStaticUrls = true; // 기본적으로 정적 URL 우선 사용 (빠름, 캐싱)
    }

    /**
     * 이미지 ID를 API 스트리밍 URL로 변환 (인증 필요)
     * @param {number} imageId - 이미지 ID
     * @returns {string} API 스트리밍 URL
     */
    getImageStreamUrl(imageId) {
        if (!imageId) return null;
        return `/api/files/view/${imageId}`;
    }

    /**
     * 이미지 ID를 정적 리소스 URL로 변환 (공개 접근, 빠름)
     * 실제로는 relativePath가 필요하므로 서버에서 제공받아야 함
     * @param {string} relativePath - 이미지 상대 경로 (yyyy/MM/dd/filename.ext)
     * @returns {string} 정적 리소스 URL
     */
    getStaticImageUrl(relativePath) {
        if (!relativePath) return null;
        return `/images/${relativePath}`;
    }

    /**
     * 인증된 이미지 요청을 위한 헤더 생성
     * @returns {Object} 요청 헤더
     */
    getAuthHeaders() {
        const token = apiClient.getAuthToken();
        return token ? { 'Authorization': `Bearer ${token}` } : {};
    }

    /**
     * 이미지를 안전하게 로드하고 img 엘리먼트에 설정
     * @param {HTMLImageElement} imgElement - 이미지 엘리먼트
     * @param {number|string} imageIdOrUrl - 이미지 ID 또는 URL
     * @param {Object} options - 옵션 (fallback, onLoad, onError, useStatic 등)
     */
    async loadImage(imgElement, imageIdOrUrl, options = {}) {
        if (!imgElement || !imageIdOrUrl) return;

        const {
            fallback = '/images/placeholder.png',
            onLoad = null,
            onError = null,
            showLoading = true,
            useStatic = this.preferStaticUrls,  // 정적 리소스 우선 사용 여부
            relativePath = null                 // 정적 URL 생성용 상대 경로
        } = options;

        // 로딩 상태 표시
        if (showLoading) {
            imgElement.src = 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNDAiIGhlaWdodD0iNDAiIHZpZXdCb3g9IjAgMCA0MCA0MCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIiBzdHJva2U9IiM5MzMiPjxnIGZpbGw9Im5vbmUiIGZpbGwtcnVsZT0iZXZlbm9kZCI+PGcgdHJhbnNmb3JtPSJ0cmFuc2xhdGUoMSAxKSIgc3Ryb2tlLXdpZHRoPSIyIj48Y2lyY2xlIHN0cm9rZS1vcGFjaXR5PSIuNSIgY3g9IjE4IiBjeT0iMTgiIHI9IjE4Ii8+PHBhdGggZD0ibTM5IDE4YzAtOS45NC04LjA2LTE4LTE4LTE4IiBzdHJva2Utb3BhY2l0eT0iMSI+PGFuaW1hdGVUcmFuc2Zvcm0gYXR0cmlidXRlTmFtZT0idHJhbnNmb3JtIiB0eXBlPSJyb3RhdGUiIGZyb209IjAgMTggMTgiIHRvPSIzNjAgMTggMTgiIGR1cj0iMXMiIHJlcGVhdENvdW50PSJpbmRlZmluaXRlIi8+PC9wYXRoPjwvZz48L2c+PC9zdmc+';
            imgElement.alt = '로딩 중...';
        }

        // URL 타입 감지 (문자열이면 직접 URL, 숫자면 ID)
        const isDirectUrl = typeof imageIdOrUrl === 'string' && imageIdOrUrl.startsWith('/');
        const imageKey = imageIdOrUrl.toString();

        try {
            // 이미 로딩 중인 경우 중복 요청 방지
            if (this.loadingImages.has(imageKey)) {
                return;
            }

            this.loadingImages.add(imageKey);

            // 캐시된 URL이 있는지 확인
            if (this.imageCache.has(imageKey)) {
                const cachedUrl = this.imageCache.get(imageKey);
                imgElement.src = cachedUrl;
                if (onLoad) onLoad();
                return;
            }

            let imageUrl;

            if (isDirectUrl) {
                // 직접 URL이 제공된 경우
                imageUrl = imageIdOrUrl;
            } else if (useStatic && relativePath) {
                // 정적 리소스 사용 (빠름, 인증 불필요)
                imageUrl = this.getStaticImageUrl(relativePath);
                imgElement.src = imageUrl;
                imgElement.alt = `이미지 ${imageIdOrUrl}`;
                if (onLoad) onLoad();
                return;
            } else {
                // API 스트리밍 사용 (인증 필요)
                imageUrl = this.getImageStreamUrl(imageIdOrUrl);
                
                // 인증된 fetch로 이미지 데이터 가져오기
                const response = await fetch(imageUrl, {
                    method: 'GET',
                    headers: this.getAuthHeaders(),
                    credentials: 'include'
                });

                if (!response.ok) {
                    throw new Error(`HTTP ${response.status}: ${response.statusText}`);
                }

                // Blob으로 변환하고 Object URL 생성
                const blob = await response.blob();
                const objectUrl = URL.createObjectURL(blob);

                // 캐시에 저장
                this.imageCache.set(imageKey, objectUrl);

                // 이미지 설정
                imgElement.src = objectUrl;
                imgElement.alt = `이미지 ${imageIdOrUrl}`;

                if (onLoad) onLoad();
                return;
            }

        } catch (error) {
            console.error(`이미지 로드 실패 (${imageKey}):`, error);
            
            // 정적 URL 실패 시 API 스트리밍으로 폴백 시도
            if (useStatic && !isDirectUrl && typeof imageIdOrUrl === 'number') {
                console.warn('정적 리소스 실패, API 스트리밍으로 재시도합니다.');
                return this.loadImage(imgElement, imageIdOrUrl, {
                    ...options,
                    useStatic: false,
                    relativePath: null
                });
            }
            
            // 폴백 이미지 설정
            imgElement.src = fallback;
            imgElement.alt = '이미지를 불러올 수 없습니다';
            
            if (onError) onError(error);
        } finally {
            this.loadingImages.delete(imageKey);
        }
    }

    /**
     * 여러 이미지를 병렬로 로드
     * @param {Array} imageConfigs - [{element: HTMLImageElement, imageId: number, options: Object}]
     */
    async loadMultipleImages(imageConfigs) {
        const promises = imageConfigs.map(config => 
            this.loadImage(config.element, config.imageId, config.options)
        );
        
        try {
            await Promise.all(promises);
        } catch (error) {
            console.error('다중 이미지 로드 중 오류:', error);
        }
    }

    /**
     * 이미지 캐시 정리
     * @param {number} maxAge - 캐시 최대 보관 시간 (밀리초)
     */
    clearCache(maxAge = 30 * 60 * 1000) { // 기본 30분
        // Object URL 해제하여 메모리 정리
        for (const [imageId, objectUrl] of this.imageCache.entries()) {
            URL.revokeObjectURL(objectUrl);
        }
        
        this.imageCache.clear();
        console.log('이미지 캐시가 정리되었습니다.');
    }

    /**
     * 이미지 다운로드 (파일로 저장)
     * @param {number} imageId - 이미지 ID
     * @param {string} filename - 저장할 파일명 (선택사항)
     */
    async downloadImage(imageId, filename = null) {
        try {
            const streamUrl = this.getImageStreamUrl(imageId);
            
            const response = await fetch(streamUrl, {
                method: 'GET',
                headers: this.getAuthHeaders(),
                credentials: 'include'
            });

            if (!response.ok) {
                throw new Error(`HTTP ${response.status}: ${response.statusText}`);
            }

            const blob = await response.blob();
            const url = URL.createObjectURL(blob);

            // 다운로드 링크 생성 및 클릭
            const a = document.createElement('a');
            a.href = url;
            a.download = filename || `image_${imageId}`;
            document.body.appendChild(a);
            a.click();
            document.body.removeChild(a);

            // 메모리 정리
            URL.revokeObjectURL(url);

        } catch (error) {
            console.error(`이미지 다운로드 실패 (ID: ${imageId}):`, error);
            notification.error('이미지 다운로드에 실패했습니다.');
        }
    }
}

// 전역 이미지 스트리밍 관리자 인스턴스
const imageStreaming = new ImageStreamingManager();

/**
 * 모바일 메뉴 토글 기능
 */
function initializeMobileMenu() {
    const mobileMenuButton = document.getElementById('mobile-menu-button');
    const mobileMenu = document.getElementById('mobile-menu');
    
    if (mobileMenuButton && mobileMenu) {
        mobileMenuButton.addEventListener('click', () => {
            mobileMenu.classList.toggle('hidden');
            
            // 아이콘 변경
            const openIcon = mobileMenuButton.querySelector('svg:first-child');
            const closeIcon = mobileMenuButton.querySelector('svg:last-child');
            
            openIcon.classList.toggle('hidden');
            closeIcon.classList.toggle('hidden');
        });
    }
}

/**
 * 페이지 로드 시 초기화
 */
document.addEventListener('DOMContentLoaded', () => {
    // 저장된 토큰이 있으면 API 클라이언트에 설정
    const savedToken = apiClient.getAuthToken();
    if (savedToken) {
        apiClient.setAuthToken(savedToken);
        // 재방문 시에도 서버 렌더 페이지 접근 가능하도록 쿠키에 동기화
        try {
            document.cookie = `accessToken=${savedToken}; Path=/; SameSite=Lax`;
        } catch (_) {}
    }

    // 모바일 메뉴 초기화
    initializeMobileMenu();

    // 역할에 따라 내비게이션 문구/링크 조정
    try {
        const userInfoRaw = localStorage.getItem('userInfo');
        if (userInfoRaw) {
            const user = JSON.parse(userInfoRaw);
            const isAdmin = user?.role === 'ADMIN';
            document.querySelectorAll('a[href="/mypage"]').forEach((a) => {
                if (isAdmin) {
                    a.href = '/admin';
                    a.textContent = '관리자 메뉴';
                } else {
                    a.href = '/mypage';
                    a.textContent = '마이페이지';
                }
            });
        }
    } catch (_) {}
});

// 전역 객체로 내보내기
window.apiClient = apiClient;
window.notification = notification;
window.loading = loading;
window.utils = utils;
window.imageStreaming = imageStreaming;