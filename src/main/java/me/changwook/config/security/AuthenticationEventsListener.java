package me.changwook.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.changwook.member.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationEventsListener {

    private final MemberRepository memberRepository;

    @Value("${security.login.max-attempts}")
    private int maxAttempts;

    @Value("${security.login.lockout-duration-minutes}")
    private int lockoutDurationMinutes;

    @EventListener
    @Transactional
    public void handleAuthenticationFailure(AuthenticationFailureBadCredentialsEvent event) {
        String username = event.getAuthentication().getName();
        log.warn("Login failure for user:{}", username);

        memberRepository.findByEmail(username).ifPresent(member -> {
            member.incrementFailedLoginAttempts();
            log.info("User{} failed login attempts:{}", username,member.getFailedLoginAttempts());

            if(member.getFailedLoginAttempts() >= maxAttempts) {
                LocalDateTime unlockTime = LocalDateTime.now().plusMinutes(lockoutDurationMinutes);
                member.lockAccount(unlockTime);
                log.warn("User{} account locked until{}",username,unlockTime);
            }
            memberRepository.save(member);
        });
    }

    @EventListener
    @Transactional
    public void handleAuthenticationSuccess(AuthenticationSuccessEvent event) {
        String username = event.getAuthentication().getName();
        log.info("Login success for user:{}", username);

        memberRepository.findByEmail(username).ifPresent(member -> {
           if(member.getFailedLoginAttempts() > 0 || member.getAccountLockedUntil() != null){
               member.resetLoginAttempts();
               memberRepository.save(member);
               log.info("Reset failed login attempts for user: {}",username);
           }
        });
    }
}
