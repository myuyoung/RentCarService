package me.changwook.config.logging;

import me.changwook.config.security.CustomUserDetails;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication; // Spring Security 사용 가정
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;

@Component
public class MdcLogInterceptor implements HandlerInterceptor {

    public static final String MDC_KEY_REQUEST_ID = "requestId";
    public static final String MDC_KEY_USER_ID = "userId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //  고유한 요청 ID 생성 및 저장
        String requestId = UUID.randomUUID().toString().substring(0, 8); // 8자리 UUID
        MDC.put(MDC_KEY_REQUEST_ID, requestId);

        // 사용자 ID 저장
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            String userId = "GUEST"; // 기본값
            if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
                // Principal이 UserDetails 객체일 수도 있고, 단순 String(username)일 수도 있습니다.
                // 예시: (User) authentication.getPrincipal()).getUsername() 또는 authentication.getName()
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
                userId = customUserDetails.getUsername();
            }
            MDC.put(MDC_KEY_USER_ID, userId);

        } catch (Exception e) {
            // SecurityContext 접근 중 예외 발생 시 (e.g., 비동기 처리 초기 단계)
            MDC.put(MDC_KEY_USER_ID, "UNKNOWN");
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        MDC.clear();
    }
}
