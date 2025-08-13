package me.changwook.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.changwook.DTO.CarRegistrationSubmissionDTO;
import me.changwook.DTO.CarRegistrationSubmissionViewDTO;
import me.changwook.domain.*;
import me.changwook.repository.CarRegistrationSubmissionRepository;
import me.changwook.repository.MemberRepository;
import me.changwook.repository.RentCarsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarRegistrationSubmissionService {

    private final CarRegistrationSubmissionRepository submissionRepository;
    // private final ImageRepository imageRepository; // 향후 이미지 연동 고도화 시 사용
    private final RentCarsRepository rentCarsRepository;
    private final MemberRepository memberRepository;
    private final FileUploadService fileUploadService;

    @Transactional
    public CarRegistrationSubmission createSubmission(CarRegistrationSubmissionDTO dto) {
        CarRegistrationSubmission submission = CarRegistrationSubmission.builder()
                .memberId(dto.getMemberId())
                .carName(dto.getCarName())
                .rentCarNumber(dto.getRentCarNumber())
                .rentPrice(dto.getRentPrice())
                .build();
        return submissionRepository.save(submission);
    }

    @Transactional(readOnly = true)
    public Page<CarRegistrationSubmissionViewDTO> listPending(Pageable pageable) {
        log.info("=== 관리자 신청 목록 조회 시작 ===");
        Page<CarRegistrationSubmission> submissions = submissionRepository.findByStatus(SubmissionStatus.PENDING, pageable);
        log.info("PENDING 상태 신청 개수: {}", submissions.getTotalElements());
        
        return submissions.map(sub -> {
            log.info("신청 처리 중 - ID: {}, 이미지 개수: {}", sub.getId(), sub.getImages().size());
            
            var imageUrls = sub.getImages().stream()
                    .map(img -> {
                        log.info("목록-이미지 정보 - ID: {}, relativePath: {}, filePath: {}, storedFileName: {}", 
                                 img.getId(), img.getRelativePath(), img.getFilePath(), img.getStoredFileName());
                        
                        String generatedUrl = null;
                        if (img.getRelativePath() != null) {
                            generatedUrl = fileUploadService.getImageUrl(img.getRelativePath());
                            log.info("목록-relativePath로 생성된 URL: {}", generatedUrl);
                        } else if (img.getFilePath() != null) {
                            generatedUrl = fileUploadService.getImageUrlFromAbsolute(img.getFilePath());
                            log.info("목록-filePath로 생성된 URL: {}", generatedUrl);
                        } else if (img.getStoredFileName() != null) {
                            generatedUrl = fileUploadService.getImageUrl(img.getStoredFileName());
                            log.info("목록-storedFileName으로 생성된 URL: {}", generatedUrl);
                        }
                        
                        log.info("목록-최종 생성된 URL: {}", generatedUrl);
                        return generatedUrl;
                    })
                    .filter(Objects::nonNull)
                    .toList();
                    
            log.info("목록-신청 ID {} - 필터링 후 이미지 URL 개수: {}", sub.getId(), imageUrls.size());
            
            return CarRegistrationSubmissionViewDTO.builder()
                    .id(sub.getId())
                    .memberId(sub.getMemberId())
                    .memberName(memberRepository.findById(sub.getMemberId()).map(Member::getName).orElse("-"))
                    .carName(sub.getCarName())
                    .rentCarNumber(sub.getRentCarNumber())
                    .rentPrice(sub.getRentPrice())
                    .status(sub.getStatus())
                    .imageUrls(imageUrls)
                    .build();
        });
    }

    @Transactional(readOnly = true)
    public CarRegistrationSubmissionViewDTO getSubmissionDetail(java.util.UUID submissionId) {
        CarRegistrationSubmission sub = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new IllegalArgumentException("신청을 찾을 수 없습니다."));
        String memberName = memberRepository.findById(sub.getMemberId()).map(Member::getName).orElse("-");
        
        log.info("=== 이미지 URL 생성 디버깅 시작 ===");
        log.info("신청 ID: {}, 연결된 이미지 개수: {}", submissionId, sub.getImages().size());
        
        var imageUrls = sub.getImages().stream()
                .map(img -> {
                    log.info("이미지 정보 - ID: {}, relativePath: {}, filePath: {}, storedFileName: {}", 
                             img.getId(), img.getRelativePath(), img.getFilePath(), img.getStoredFileName());
                    
                    String generatedUrl = null;
                    if (img.getRelativePath() != null) {
                        generatedUrl = fileUploadService.getImageUrl(img.getRelativePath());
                        log.info("relativePath로 생성된 URL: {}", generatedUrl);
                    } else if (img.getFilePath() != null) {
                        generatedUrl = fileUploadService.getImageUrlFromAbsolute(img.getFilePath());
                        log.info("filePath로 생성된 URL: {}", generatedUrl);
                    } else if (img.getStoredFileName() != null) {
                        generatedUrl = fileUploadService.getImageUrl(img.getStoredFileName());
                        log.info("storedFileName으로 생성된 URL: {}", generatedUrl);
                    }
                    
                    log.info("최종 생성된 URL: {}", generatedUrl);
                    return generatedUrl;
                })
                .filter(Objects::nonNull)
                .toList();
                
        log.info("필터링 후 이미지 URL 목록: {}", imageUrls);
        log.info("=== 이미지 URL 생성 디버깅 종료 ===");
        
        return CarRegistrationSubmissionViewDTO.builder()
                .id(sub.getId())
                .memberId(sub.getMemberId())
                .memberName(memberName)
                .carName(sub.getCarName())
                .rentCarNumber(sub.getRentCarNumber())
                .rentPrice(sub.getRentPrice())
                .status(sub.getStatus())
                .imageUrls(imageUrls)
                .build();
    }

    @Transactional
    public void approveSubmission(java.util.UUID submissionId) {
        CarRegistrationSubmission sub = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new IllegalArgumentException("신청을 찾을 수 없습니다."));
        sub.approve();

        // 승인 시 차량으로 등록
        RentCars rentCar = RentCars.builder()
                .name(sub.getCarName())
                .rentCarNumber(sub.getRentCarNumber())
                .rentPrice(sub.getRentPrice() != null ? sub.getRentPrice() : 0)
                .build();
        rentCarsRepository.save(rentCar);
        submissionRepository.save(sub);
    }

    @Transactional
    public void rejectSubmission(java.util.UUID submissionId) {
        CarRegistrationSubmission sub = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new IllegalArgumentException("신청을 찾을 수 없습니다."));
        sub.reject();
        submissionRepository.save(sub);
    }
}


