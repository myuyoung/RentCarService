package me.changwook.service;

public interface NotificationService {
    void notifyAdminOfTokenTheft(String username, String clientIp);
}
