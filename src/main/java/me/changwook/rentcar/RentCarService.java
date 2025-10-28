package me.changwook.rentcar;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.changwook.rentcar.dto.RentCarsDTO;
import me.changwook.exception.custom.DuplicateRentCarException;
import me.changwook.exception.custom.RentCarNotFoundException;
import me.changwook.mapper.RentCarsMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 【 렌트카 서비스 】
 * 
 * 담당 업무:
 * - 렌트카 CRUD 작업
 * - 검색 및 필터링
 * - 관리자 전용 차량 관리
 * - 공개 API용 차량 조회
 * 
 * 비즈니스 규칙:
 * - 차량번호 중복 등록 방지
 * - 소프트 삭제 지원
 * - 검색 최적화
 */
@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class RentCarService {

    private final RentCarsRepository rentCarsRepository;
    private final RentCarsMapper rentCarsMapper;

    // =============================================================
    // AdminController에서 사용하는 기본 메서드들
    // =============================================================
    
    @Transactional
    public void save(RentCarsDTO rentCarsDTO) {
        rentCarsRepository.save(rentCarsMapper.rentCarsDTOToRent(rentCarsDTO));
    }
    


    @Transactional(readOnly = true)
    public Page<RentCarsDTO> getRankedRentCars(Pageable pageable) {
        Page<RentCars> rentCarsPage = rentCarsRepository.findRecommendedForHome(pageable);
        return rentCarsPage.map(rentCarsMapper::rentCarsToRentCarsDTO);
    }

    @Transactional(readOnly = true)
    public Page<RentCarsDTO> searchCars(String segment, String fuelType, String keyword,
                                        Integer minPrice, Integer maxPrice, Pageable pageable) {
        Page<RentCars> result = rentCarsRepository.searchCars(segment, fuelType, keyword, minPrice, maxPrice, pageable);
        return result.map(rentCarsMapper::rentCarsToRentCarsDTO);
    }

    /**
     * 【 차량 등록 】
     * 새로운 렌트카를 등록합니다.
     * 
     * @param rentCarsDTO 등록할 차량 정보
     * @return 등록된 차량 정보
     * @throws DuplicateRentCarException 차량번호 중복 시
     */
    @Transactional
    public RentCarsDTO registerCar(RentCarsDTO rentCarsDTO) {
        log.info("차량 등록 요청: {}", rentCarsDTO.getRentCarNumber());
        
        String rentCarNumber = rentCarsDTO.getRentCarNumber();
        if (rentCarsRepository.findByRentCarNumber(rentCarNumber).isPresent()) {
            log.warn("차량번호 중복: {}", rentCarNumber);
            throw DuplicateRentCarException.withCarNumber(rentCarNumber);
        }
        
        // TODO: 향후 국토교통부 API 연동 예정
        // 현재는 수동 등록 방식 사용
        RentCars savedCar = rentCarsRepository.save(rentCarsMapper.rentCarsDTOToRent(rentCarsDTO));
        RentCarsDTO result = rentCarsMapper.rentCarsToRentCarsDTO(savedCar);
        
        log.info("차량 등록 완료: {} (ID: {})", rentCarNumber, savedCar.getId());
        return result;
    }

    // =============================================================
    // 관리자 전용 메서드들
    // =============================================================
    
    /**
     * 【 모든 차량 조회 】
     * 관리자용 전체 차량 목록을 페이징하여 조회합니다.
     */
    @Transactional(readOnly = true)
    public Page<RentCarsDTO> getAllCars(Pageable pageable) {
        log.info("전체 차량 목록 조회 - 페이지: {}, 크기: {}", 
                 pageable.getPageNumber(), pageable.getPageSize());
        
        Page<RentCars> cars = rentCarsRepository.findAll(pageable);
        return cars.map(rentCarsMapper::rentCarsToRentCarsDTO);
    }

    /**
     * 【 특정 차량 조회 】
     * ID로 특정 차량을 조회합니다.
     * 
     * @param carId 차량 ID
     * @return 차량 정보
     * @throws RentCarNotFoundException 차량을 찾을 수 없을 때
     */
    @Transactional(readOnly = true)
    public RentCarsDTO getCarById(Long carId) {
        log.info("차량 조회 요청: ID = {}", carId);
        
        RentCars car = rentCarsRepository.findById(carId)
                .orElseThrow(() -> new RentCarNotFoundException(carId));
        
        return rentCarsMapper.rentCarsToRentCarsDTO(car);
    }

    /**
     * 【 차량 정보 수정 】
     * 기존 차량의 정보를 수정합니다.
     * 
     * @param carId 차량 ID
     * @param rentCarsDTO 수정할 차량 정보
     * @return 수정된 차량 정보
     * @throws RentCarNotFoundException 차량을 찾을 수 없을 때
     * @throws DuplicateRentCarException 차량번호 중복 시
     */
    @Transactional
    public RentCarsDTO updateCar(Long carId, RentCarsDTO rentCarsDTO) {
        log.info("차량 정보 수정 요청: ID = {}", carId);
        
        RentCars existingCar = rentCarsRepository.findById(carId)
                .orElseThrow(() -> new RentCarNotFoundException(carId));
        
        // 차량번호 중복 검사 (자기 자신은 제외)
        String newCarNumber = rentCarsDTO.getRentCarNumber();
        if (!existingCar.getRentCarNumber().equals(newCarNumber)) {
            if (rentCarsRepository.findByRentCarNumber(newCarNumber).isPresent()) {
                log.warn("차량번호 중복: {}", newCarNumber);
                throw DuplicateRentCarException.withCarNumber(newCarNumber);
            }
        }
        
        // 엔티티 업데이트 (더티 체킹 활용)
        existingCar.updateRentCars(rentCarsMapper.rentCarsDTOToRent(rentCarsDTO));
        
        RentCarsDTO result = rentCarsMapper.rentCarsToRentCarsDTO(existingCar);
        log.info("차량 정보 수정 완료: {}", newCarNumber);
        
        return result;
    }

    /**
     * 【 차량 삭제 】
     * 특정 차량을 삭제합니다.
     * 
     * @param carId 삭제할 차량 ID
     * @throws RentCarNotFoundException 차량을 찾을 수 없을 때
     */
    @Transactional
    public void deleteRentCar(Long carId) {
        log.info("차량 삭제 요청: ID = {}", carId);
        
        RentCars rentCar = rentCarsRepository.findById(carId)
                .orElseThrow(() -> new RentCarNotFoundException(carId));
        
        rentCarsRepository.delete(rentCar);
        log.info("차량 삭제 완료: {} (ID: {})", rentCar.getRentCarNumber(), carId);
    }

    @Transactional(readOnly = true)
    public long getTotalCarCount() {
        return rentCarsRepository.count();
    }
}
