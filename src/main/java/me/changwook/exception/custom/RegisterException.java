package me.changwook.exception.custom;

public class RegisterException extends RuntimeException {
    public RegisterException(String message) {
        super(message);
    }
}
