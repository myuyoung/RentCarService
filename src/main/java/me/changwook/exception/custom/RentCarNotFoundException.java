package me.changwook.exception.custom;

/**
 * 렌트카를 찾을 수 없을 때 발생하는 예외
 */
public class RentCarNotFoundException extends RuntimeException {
    
    public RentCarNotFoundException() {
        super("요청한 렌트카를 찾을 수 없습니다.");
    }
    
    public RentCarNotFoundException(String message) {
        super(message);
    }
    
    public RentCarNotFoundException(Long carId) {
        super("ID " + carId + "인 렌트카를 찾을 수 없습니다.");
    }
    
    public static RentCarNotFoundException withCarNumber(String carNumber) {
        return new RentCarNotFoundException("차량번호 " + carNumber + "인 렌트카를 찾을 수 없습니다.");
    }
}
