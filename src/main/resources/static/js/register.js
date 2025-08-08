// Dream Drive - 회원가입 페이지 JavaScript

class RegisterPage {
    constructor() {
        this.form = document.getElementById('register-form');
        this.submitButton = document.getElementById('submit-button');
        this.buttonText = this.submitButton.querySelector('.btn-text');
        this.loader = this.submitButton.querySelector('.loader');
        this.apiResponseMessage = document.getElementById('api-response-message');
        
        this.init();
    }

    init() {
        this.setupEventListeners();
        this.setupValidation();
    }

    setupEventListeners() {
        this.form.addEventListener('submit', (e) => this.handleSubmit(e));
    }

    setupValidation() {
        // 실시간 유효성 검사
        const inputs = this.form.querySelectorAll('input');
        inputs.forEach(input => {
            input.addEventListener('blur', () => this.validateField(input));
            input.addEventListener('input', () => this.clearFieldError(input));
        });
    }

    validateField(input) {
        const fieldName = input.name;
        const value = input.value.trim();
        let errorMessage = '';

        switch (fieldName) {
            case 'name':
                if (!value) {
                    errorMessage = '이름을 입력해주세요.';
                } else if (value.length < 2) {
                    errorMessage = '이름은 2글자 이상이어야 합니다.';
                }
                break;
                
            case 'email':
                if (!value) {
                    errorMessage = '이메일을 입력해주세요.';
                } else if (!utils.isValidEmail(value)) {
                    errorMessage = '올바른 이메일 형식이 아닙니다.';
                }
                break;
                
            case 'password':
                if (!value) {
                    errorMessage = '비밀번호를 입력해주세요.';
                } else if (!this.isValidPassword(value)) {
                    errorMessage = '8~16자의 영문 대/소문자, 숫자, 특수문자를 포함해야 합니다.';
                }
                break;
                
            case 'phone':
                if (!value) {
                    errorMessage = '전화번호를 입력해주세요.';
                } else if (!utils.isValidPhoneNumber(value)) {
                    errorMessage = '올바른 전화번호 형식이 아닙니다. (예: 010-1234-5678)';
                }
                break;
                
            case 'address':
                if (!value) {
                    errorMessage = '주소를 입력해주세요.';
                } else if (value.length < 10) {
                    errorMessage = '주소를 상세히 입력해주세요.';
                }
                break;
        }

        this.showFieldError(fieldName, errorMessage);
        return !errorMessage;
    }

    isValidPassword(password) {
        // 8~16자, 영문 대소문자, 숫자, 특수문자 포함
        const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,16}$/;
        return passwordRegex.test(password);
    }

    async handleSubmit(e) {
        e.preventDefault();
        
        this.clearAllErrors();
        
        // 폼 유효성 검사
        if (!this.validateForm()) {
            return;
        }
        
        this.setLoadingState(true);

        const formData = utils.formToObject(this.form);

        try {
            const result = await apiClient.post('/api/register/member', formData);

            if (result.success) {
                this.handleRegisterSuccess(result);
            } else {
                this.handleRegisterError(result);
            }
        } catch (error) {
            console.error('회원가입 오류:', error);
            this.showErrorMessage('서버 통신 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.');
        } finally {
            this.setLoadingState(false);
        }
    }

    validateForm() {
        const inputs = this.form.querySelectorAll('input[required]');
        let isValid = true;

        inputs.forEach(input => {
            if (!this.validateField(input)) {
                isValid = false;
            }
        });

        return isValid;
    }

    handleRegisterSuccess(result) {
        this.showSuccessMessage(`${result.message}<br><span class="text-gray-600">3초 후 로그인 페이지로 이동합니다.</span>`);
        
        setTimeout(() => {
            window.location.href = '/login';
        }, 3000);
    }

    handleRegisterError(result) {
        if (result.data && typeof result.data === 'object') {
            // 서버 유효성 검사 오류 표시
            this.displayServerErrors(result.data);
        }
        
        const errorMessage = result.message || '회원가입에 실패했습니다. 입력 내용을 확인해주세요.';
        this.showErrorMessage(errorMessage);
    }

    displayServerErrors(errors) {
        for (const field in errors) {
            this.showFieldError(field, errors[field]);
        }
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

    showFieldError(fieldName, message) {
        const errorElement = document.getElementById(`${fieldName}-error`);
        if (errorElement && message) {
            errorElement.textContent = message;
            errorElement.classList.remove('hidden');
        } else if (errorElement) {
            errorElement.textContent = '';
            errorElement.classList.add('hidden');
        }
    }

    clearFieldError(input) {
        this.showFieldError(input.name, '');
    }

    showSuccessMessage(message) {
        this.apiResponseMessage.innerHTML = `<p class="text-green-600 font-semibold">${message}</p>`;
    }

    showErrorMessage(message) {
        this.apiResponseMessage.innerHTML = `<p class="text-red-500">${message}</p>`;
    }

    clearAllErrors() {
        const errorElements = document.querySelectorAll('p[id$="-error"]');
        errorElements.forEach(el => {
            el.textContent = '';
            el.classList.add('hidden');
        });
        this.apiResponseMessage.innerHTML = '';
    }
}

// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', () => {
    new RegisterPage();
});