package me.changwook.controller.api;

import lombok.RequiredArgsConstructor;
import me.changwook.DTO.ApiResponseDTO;
import me.changwook.DTO.RentDTO;
import me.changwook.DTO.ReservationDTO;
import me.changwook.configuration.config.security.CustomUserDetails;
import me.changwook.service.impl.RentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController("api/rent")
@RequiredArgsConstructor
public class RentController {

    private final RentService rentService;

    @PostMapping("/reservation")
    public ResponseEntity<ApiResponseDTO<RentDTO>> reserveCar(
            @Validated @RequestBody ReservationDTO reservationDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        String username = userDetails.getUsername();

        RentDTO createDTO = rentService.rentInformation(reservationDTO,username);

        ApiResponseDTO<RentDTO> response = new ApiResponseDTO<>(true,"차량 예약 성공하였습니다",createDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
