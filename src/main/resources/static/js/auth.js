// Dream Drive - 인증 관리 JavaScript

/**
 * 인증 관리 클래스
 */
class AuthManager {
    constructor() {
        this.currentUser = null;
        this.init();
    }

    init() {
        this.loadCurrentUser();
        this.setupTokenRefresh();
    }

    loadCurrentUser() {
        const token = apiClient.getAuthToken();
        const userInfo = localStorage.getItem('userInfo');
        
        if (token && userInfo) {
            try {
                this.currentUser = JSON.parse(userInfo);
            } catch (e) {
                console.error('사용자 정보 파싱 오류:', e);
                this.logout();
            }
        }
    }

    isAuthenticated() {
        return !!this.currentUser && !!apiClient.getAuthToken();
    }

    getCurrentUser() {
        return this.currentUser;
    }

    login(authData) {
        // 토큰 저장
        apiClient.saveAuthToken(authData.token);
        
        // 사용자 정보 저장
        const userInfo = {
            email: authData.email,
            name: authData.name,
            role: authData.role
        };
        
        localStorage.setItem('userInfo', JSON.stringify(userInfo));
        this.currentUser = userInfo;
        
        // 로그인 성공 이벤트 발생
        this.dispatchAuthEvent('login', userInfo);
    }

    logout() {
        // 토큰 및 사용자 정보 삭제
        apiClient.removeAuthToken();
        localStorage.removeItem('userInfo');
        this.currentUser = null;
        
        // 로그아웃 이벤트 발생
        this.dispatchAuthEvent('logout');
        
        // 서버에 로그아웃 요청
        return apiClient.post('/auth/logout').catch(() => {
            // 로그아웃 실패해도 클라이언트는 이미 정리됨
        });
    }

    async requireAuth() {
        if (!this.isAuthenticated()) {
            const currentPath = encodeURIComponent(window.location.pathname + window.location.search);
            window.location.href = `/login?redirect=${currentPath}`;
            return false;
        }
        return true;
    }

    setupTokenRefresh() {
        // 5분마다 토큰 유효성 체크
        setInterval(() => {
            this.checkTokenValidity();
        }, 5 * 60 * 1000);
    }

    async checkTokenValidity() {
        if (!this.isAuthenticated()) return;

        try {
            // 보호된 엔드포인트로 토큰 유효성 확인
            const response = await apiClient.get('/api/protected/test');
            
            if (!response.success && response.message?.includes('토큰')) {
                // 토큰이 만료된 경우 자동 로그아웃
                this.logout();
                notification.warning('세션이 만료되었습니다. 다시 로그인해주세요.');
                setTimeout(() => {
                    window.location.href = '/login';
                }, 2000);
            }
        } catch (error) {
            console.error('토큰 유효성 검사 오류:', error);
        }
    }

    dispatchAuthEvent(type, data = null) {
        const event = new CustomEvent(`auth:${type}`, {
            detail: data
        });
        document.dispatchEvent(event);
    }

    // 관리자 권한 확인
    isAdmin() {
        return this.currentUser?.role === 'ADMIN';
    }

    // 사용자 역할 확인
    hasRole(role) {
        return this.currentUser?.role === role;
    }
}

// 전역 인증 관리자 인스턴스
const authManager = new AuthManager();

// 인증 이벤트 리스너 설정
document.addEventListener('auth:login', (e) => {
    console.log('로그인됨:', e.detail);
    // 필요시 UI 업데이트
});

document.addEventListener('auth:logout', () => {
    console.log('로그아웃됨');
    // 필요시 UI 업데이트
});

// 전역 객체로 내보내기
window.authManager = authManager;