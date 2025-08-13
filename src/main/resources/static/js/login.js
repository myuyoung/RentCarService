// Dream Drive - 로그인 페이지 JavaScript

class LoginPage {
    constructor() {
        this.form = document.getElementById('login-form');
        this.submitButton = document.getElementById('submit-button');
        this.buttonText = this.submitButton.querySelector('.btn-text');
        this.loader = this.submitButton.querySelector('.loader');
        this.apiResponseMessage = document.getElementById('api-response-message');
        
        this.init();
    }

    init() {
        this.setupEventListeners();
        this.checkExistingLogin();
    }

    setupEventListeners() {
        this.form.addEventListener('submit', (e) => this.handleSubmit(e));
    }

    // 이미 로그인된 상태인지 확인
    checkExistingLogin() {
        const token = apiClient.getAuthToken();
        const userInfoRaw = localStorage.getItem('userInfo');
        if (!token || !userInfoRaw) return;

        try {
            const user = JSON.parse(userInfoRaw);
            if (user?.role === 'ADMIN') {
                window.location.href = '/admin';
            } else {
                window.location.href = '/';
            }
        } catch (_) {
            // 파싱 에러 시 기본 동작
            window.location.href = '/';
        }
    }

    async handleSubmit(e) {
        e.preventDefault();
        
        this.clearMessages();
        this.setLoadingState(true);

        const formData = utils.formToObject(this.form);

        try {
            const result = await apiClient.post('/auth/login', formData);

            if (result.success) {
                this.handleLoginSuccess(result);
            } else {
                this.handleLoginError(result);
            }
        } catch (error) {
            console.error('로그인 오류:', error);
            this.showErrorMessage('서버와 통신할 수 없습니다. 잠시 후 다시 시도해주세요.');
        } finally {
            this.setLoadingState(false);
        }
    }

    handleLoginSuccess(result) {
        // Access Token을 localStorage에 저장 및 API 클라이언트에 설정
        apiClient.saveAuthToken(result.data.token);
        
        // 사용자 정보도 저장 (선택적)
        localStorage.setItem('userInfo', JSON.stringify({
            email: result.data.email,
            name: result.data.name,
            role: result.data.role
        }));

        this.showSuccessMessage(result.message);
        
        // 1초 후 역할 기반 리디렉션 (관리자는 /admin, 일반 사용자는 / 혹은 redirect 파라미터)
        const role = result.data.role;
        const params = new URLSearchParams(window.location.search);
        const redirectParam = params.get('redirect');

        let target = '/';
        if (role === 'ADMIN') {
            target = '/admin';
        } else if (redirectParam) {
            target = redirectParam;
        }

        setTimeout(() => {
            window.location.href = target;
        }, 1000);
    }

    handleLoginError(result) {
        const errorMessage = result.message || '이메일 또는 비밀번호를 확인해주세요.';
        this.showErrorMessage(errorMessage);
    }

    setLoadingState(isLoading) {
        if (isLoading) {
            this.buttonText.classList.add('hidden');
            this.loader.classList.remove('hidden');
            this.submitButton.disabled = true;
        } else {
            this.buttonText.classList.remove('hidden');
            this.loader.classList.add('hidden');
            this.submitButton.disabled = false;
        }
    }

    showSuccessMessage(message) {
        this.apiResponseMessage.innerHTML = `<p class="text-green-600 font-semibold">${message}</p>`;
    }

    showErrorMessage(message) {
        this.apiResponseMessage.innerHTML = `<p class="text-red-500">${message}</p>`;
    }

    clearMessages() {
        this.apiResponseMessage.innerHTML = '';
    }
}

// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', () => {
    new LoginPage();
});