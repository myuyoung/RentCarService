package me.changwook.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.changwook.repository.RefreshTokenRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenCleanupService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void deleteExpiredTokens() {
        log.info("만료된 Refresh 토큰 삭제 작업 시작...");

        long now = System.currentTimeMillis();
        refreshTokenRepository.deleteByExpiryDateLessThan(now);
        log.info("만료된 Refresh 토큰 삭제 작업 삭제");
    }
}
