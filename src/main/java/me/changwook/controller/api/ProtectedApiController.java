package me.changwook.controller.api;

import me.changwook.DTO.ApiResponseDTO;
import me.changwook.util.ResponseFactory;
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
    public ResponseEntity<ApiResponseDTO<String>> protectedEndpoint(Authentication authentication) {
        String message = "JWT 인증 성공! 유저: " + authentication.getName();
        return responseFactory.success("JWT 인증 성공", message);
    }
}
