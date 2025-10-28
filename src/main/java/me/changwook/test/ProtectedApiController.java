package me.changwook.test;

import me.changwook.common.ApiResponse;
import me.changwook.common.ResponseFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/protected")
@RequiredArgsConstructor
public class ProtectedApiController {

    private final ResponseFactory responseFactory;


    //테스트용 컨트롤러
    @GetMapping
//    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<String>> protectedEndpoint(Authentication authentication) {
        String message = "JWT 인증 성공! 유저: " + authentication.getName();
        return responseFactory.success("JWT 인증 성공", message);
    }
    
    // 토큰 유효성 검사용 테스트 엔드포인트
    @GetMapping("/test")
//    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<String>> tokenValidityTest(Authentication authentication) {
        String message = "토큰 유효성 검사 성공! 유저: " + authentication.getName();
        return responseFactory.success("토큰 유효", message);
    }
}
