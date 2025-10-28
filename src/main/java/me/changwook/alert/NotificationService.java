package me.changwook.alert;

public interface NotificationService {
    void notifyAdminOfTokenTheft(String username, String clientIp);
}
