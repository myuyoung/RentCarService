package me.changwook.alert;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailNotificationService implements NotificationService {

    private final JavaMailSender mailSender;

    @Value("${admin.notification.email}")
    private String adminEmail;

    @Async
    @Override
    public void notifyAdminOfTokenTheft(String username, String clientIp) {
        String subject = "Refresh Token 탈취 의심 시도 감지";
        String message = String.format("""
                Refresh Token 탈취 의심 시도가 감지되었습니다.
                - 사용자 계정: %s
                - 의심 IP 주소: %s
                - 감지 시각: %s
        보안 조치로 해당 사용자의 모든 세션을 강제 종료했습니다. 즉시 확인 및 조치가 필요합니다.""",
                                    username, clientIp, LocalDateTime.now()
                );
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(adminEmail);
        mailMessage.setTo(adminEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        try{
            mailSender.send(mailMessage);
            log.info("관리자에게 토큰 탈취 의심으로 인한 알림 이메일을 성공적으로 발송하였습니다. (수신{})",adminEmail);
        }catch (MailException e){
            log.error("관리자에게 토큰 이메일 발송에 실패하였습니다. (수신{})",adminEmail);
        }
    }
}
