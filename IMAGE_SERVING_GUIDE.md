# 🖼️ Dream Drive 이미지 서빙 가이드

## 📋 개요

Dream Drive는 **통합 이미지 서빙 시스템**을 제공합니다:
- **API 기반 스트리밍** (인증 필요, MIME 타입 정규화)  
- **정적 리소스 매핑** (빠른 접근, 브라우저 캐싱)

## 🔧 JPG 이미지 표시 문제 해결

### ❌ 문제
- JPG 이미지는 브라우저에서 표시되지 않음
- PNG 이미지는 정상 표시됨

### ✅ 원인 및 해결
**원인**: `"image/jpg"` (비표준) vs `"image/jpeg"` (표준) MIME 타입 차이

**해결**: 자동 MIME 타입 정규화 시스템 구현
- 업로드 시: `"image/jpg"` → `"image/jpeg"` 자동 변환
- 서빙 시: 안전한 MediaType 파싱 및 폴백
- 기존 데이터: 일괄 수정 도구 제공

## 🚀 사용 방법

### 1. 서버 측 (Java/Spring)

#### 파일 업로드
```java
@Autowired
private FileUploadService fileUploadService;

// 파일 업로드 (MIME 타입 자동 정규화)
fileUploadService.uploadImage(multipartFile, username, memberId, submissionId);
```

#### URL 생성
```java
// API 스트리밍 URL (인증 필요)
String apiUrl = fileUploadService.getImageStreamUrl(imageId);
// → /api/files/view/123

// 정적 리소스 URL (빠른 접근)
String staticUrl = fileUploadService.getStaticImageUrlById(imageId);
// → /images/2025/08/13/uuid.jpg
```

### 2. 클라이언트 측 (JavaScript)

#### 기본 사용법
```javascript
// 이미지 로드 (자동으로 최적 방식 선택)
await imageStreaming.loadImage(imgElement, imageId, {
    useStatic: true,      // 정적 URL 우선 사용
    relativePath: 'yyyy/MM/dd/filename.jpg',  // 정적 URL용
    onLoad: () => console.log('로드 완료'),
    onError: (error) => console.error('로드 실패', error)
});

// API 스트리밍만 사용 (인증 필요한 경우)
await imageStreaming.loadImage(imgElement, imageId, {
    useStatic: false
});

// 직접 URL로 로드
await imageStreaming.loadImage(imgElement, '/images/2025/08/13/photo.jpg');
```

#### 다중 이미지 로드
```javascript
const imageConfigs = [
    {
        element: img1,
        imageId: 123,
        options: { useStatic: true, relativePath: '2025/08/13/photo1.jpg' }
    },
    {
        element: img2,
        imageId: 124,
        options: { useStatic: false }  // API 스트리밍
    }
];

await imageStreaming.loadMultipleImages(imageConfigs);
```

## 🔀 두 방식 비교

| 특성 | API 스트리밍 | 정적 리소스 |
|------|-------------|-------------|
| **URL 형식** | `/api/files/view/{id}` | `/images/yyyy/MM/dd/file.jpg` |
| **인증** | 필요 ✅ | 불필요 ❌ |
| **속도** | 중간 🟡 | 빠름 🟢 |
| **캐싱** | 제한적 🟡 | 브라우저 캐싱 🟢 |
| **보안** | 높음 🟢 | 공개 접근 🟡 |
| **MIME 타입** | 정규화됨 🟢 | 브라우저 추론 🟡 |
| **권한 제어** | 세밀함 🟢 | 없음 ❌ |

## 🛠️ 설정

### application.yml
```yaml
file:
  upload:
    dir: ${user.home}/uploads/images
  static-serving:
    enabled: true           # 정적 리소스 서빙 활성화
    cache-duration: 3600    # 캐시 지속시간 (초)
```

### WebConfig.java
- 정적 리소스 핸들러 자동 설정
- `/images/**` → `file:/path/to/uploads/`
- 캐시 헤더 자동 설정

### SecurityConfig.java
```java
.requestMatchers("/images/**").permitAll()           // 정적 리소스 공개
.requestMatchers("/api/files/view/**").authenticated() // API 인증 필요
```

## 🎯 권장 사용 패턴

### 공개 이미지 (빠른 로딩 우선)
```javascript
// 정적 리소스 우선, 실패 시 API로 폴백
await imageStreaming.loadImage(imgElement, imageId, {
    useStatic: true,
    relativePath: imagePath
});
```

### 민감한 이미지 (보안 우선)
```javascript
// API 스트리밍만 사용
await imageStreaming.loadImage(imgElement, imageId, {
    useStatic: false
});
```

### 하이브리드 사용
```javascript
// 페이지별로 다른 전략 사용
const isPublicPage = window.location.pathname.includes('/public');
await imageStreaming.loadImage(imgElement, imageId, {
    useStatic: isPublicPage,
    relativePath: imagePath
});
```

## 🔧 유지보수 도구

### 기존 JPG 이미지 수정
```bash
# 자동화 스크립트
./fix_image_mime_types.sh

# 수동 API 호출
curl -X POST http://localhost:7950/api/admin/maintenance/fix-image-mime-types \
     -H "Content-Type: application/json" \
     -b "admin_cookies.txt"
```

### 개별 이미지 수정
```bash
curl -X POST http://localhost:7950/api/admin/maintenance/fix-image-mime-type/123 \
     -H "Content-Type: application/json" \
     -b "admin_cookies.txt"
```

## 🚨 주의사항

1. **정적 리소스 보안**: `/images/**`는 공개 접근 가능
2. **MIME 타입**: 모든 JPG는 `image/jpeg`로 정규화됨  
3. **캐싱**: 정적 리소스는 1시간 캐시됨
4. **폴백**: 정적 URL 실패 시 자동으로 API로 재시도

## 🎉 마이그레이션 완료!

✅ **기존 JPG 이미지**: 모두 정상 표시  
✅ **새로운 업로드**: 자동 MIME 타입 정규화  
✅ **브라우저 호환성**: 모든 주요 브라우저 지원  
✅ **성능**: 정적 리소스로 빠른 로딩  
✅ **보안**: API 스트리밍으로 권한 제어  

이제 JPG와 PNG 이미지 모두 완벽하게 표시됩니다! 🎯
