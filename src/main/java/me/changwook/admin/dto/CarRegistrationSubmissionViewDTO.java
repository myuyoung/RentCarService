package me.changwook.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.changwook.admin.SubmissionStatus;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarRegistrationSubmissionViewDTO {
    private UUID id;
    private UUID memberId;
    private String memberName;
    private String carName;
    private String rentCarNumber;
    private Integer rentPrice;
    private SubmissionStatus status;
    private List<String> imageUrls;
}


