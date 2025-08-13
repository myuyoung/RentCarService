package me.changwook.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.changwook.domain.SubmissionStatus;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarRegistrationSubmissionDTO {
    private UUID id;
    private UUID memberId;
    private String carName;
    private String rentCarNumber;
    private Integer rentPrice;
    private SubmissionStatus status;
    private List<Long> imageIds;
}


