package me.changwook.admin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import me.changwook.common.BaseEntity;
import me.changwook.image.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table(name = "car_registration_submission", indexes = {
        @Index(name = "idx_submission_status", columnList = "status")
})
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarRegistrationSubmission extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "submission_id")
    private UUID id;

    @Column(nullable = false)
    private UUID memberId;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private SubmissionStatus status = SubmissionStatus.PENDING;

    // 사용자 입력 메타 정보
    private String carName;
    private String rentCarNumber;
    private Integer rentPrice;

    // 업로드된 이미지들과의 연관 (단순 일대다, 별도 조인컬럼로 연결)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id")
    @Builder.Default
    @JsonIgnore  // JSON 직렬화에서 제외하여 LazyInitializationException 방지
    private List<Image> images = new ArrayList<>();

    public void approve() {
        this.status = SubmissionStatus.APPROVED;
    }

    public void reject() {
        this.status = SubmissionStatus.REJECTED;
    }
}


