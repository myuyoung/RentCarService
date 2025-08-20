package me.changwook.exception.custom;

/**
 * 중복된 렌트카 등록 시도 시 발생하는 예외
 */
public class DuplicateRentCarException extends RuntimeException {
    
    public DuplicateRentCarException() {
        super("이미 등록된 차량입니다.");
    }
    
    public DuplicateRentCarException(String message) {
        super(message);
    }
    
    public static DuplicateRentCarException withCarNumber(String carNumber) {
        return new DuplicateRentCarException("차량번호 " + carNumber + "는 이미 등록된 차량입니다.");
    }
}
