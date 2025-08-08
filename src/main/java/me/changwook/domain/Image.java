package me.changwook.domain;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String originalFileName;

    /**
     * 저장된 파일명
     * - 서버에 실제 저장된 고유 파일명
     * - UUID + 확장자 형태로 중복 방지
     * - 예: "550e8400-e29b-41d4-a716-446655440000.jpg"
     */
    @Column(nullable = false)
    private String storedFileName;

    /**
     * 파일 시스템 절대 경로
     * - 온프레미스 서버의 물리적 저장 위치
     * - 예: "/home/user/uploads/images/2024/01/15/uuid.jpg"
     * - 파일 삭제 시 사용
     */

    @Column(nullable = false)
    private String filePath;

    /**
     * 파일 크기 (bytes)
     * - 용량 관리 및 통계 분석에 사용
     * - 업로드 제한 검증에 활용
     */
    @Column(nullable = false)
    private Long fileSize;

    /**
     * MIME 타입
     * - 예: "image/jpeg", "image/png", "image/gif"
     * - 브라우저 렌더링 및 보안 검증에 사용
     */
    @Column(nullable = false)
    private String contentType;

    /**
     * 업로드한 사용자 식별자
     * - JWT 토큰에서 추출한 사용자 정보
     * - 업로드 이력 추적 및 권한 관리
     */
    @Column(nullable = false)
    private String uploadedBy;

    /**
     * 이미지를 소유한 회원
     * - Member와의 다대일 관계
     * - 회원별 이미지 조회 시 사용
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;



}