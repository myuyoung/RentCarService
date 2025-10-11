package me.changwook;

import lombok.extern.slf4j.Slf4j;
import me.changwook.service.NotificationService;


@Slf4j
public class MockTestEmailService implements NotificationService  {

    @Override
    public void notifyAdminOfTokenTheft(String username, String clientIp) {
        log.info("MockNotification을 호출했습니다.");
    }
}
