package me.changwook.reservation;

import lombok.RequiredArgsConstructor;
import me.changwook.common.ApiResponse;
import me.changwook.common.ResponseFactory;
import me.changwook.config.security.CustomUserDetails;
import me.changwook.reservation.dto.ReservationDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final ResponseFactory responseFactory;

    /**
     *
     * @param reservationDTO 예약정보를 받아오는 DTO
     * @return 예약정보를 반환함.
     */
    @PostMapping("/reservationDetail")
    public ResponseEntity<ApiResponse<ReservationDTO>> reservationRentCar(
            @Validated @RequestBody ReservationDTO reservationDTO,
             @AuthenticationPrincipal CustomUserDetails UserDetails) {

        UUID id = UserDetails.getMember().getId();
        ReservationDTO newReservation = reservationService.reservation(reservationDTO, id);

        return responseFactory.created("예약이 완료되었습니다.",newReservation);
    }

    /**
     *
     * @param UserDetails 로그인된 회원정보를 받아오는 로직
     * @return 회원이 현재 이후의 모든 예약을 가져오는 로직
     */
    @GetMapping("/myInform")
    public ResponseEntity<ApiResponse<List<ReservationDTO>>> reservationInform(
            @AuthenticationPrincipal CustomUserDetails UserDetails
    ) {
        UUID id = UserDetails.getMember().getId();
        List<ReservationDTO> reservationList = reservationService.findReservationList(id);
        return responseFactory.success("성공적으로 조회되었습니다.",reservationList);
    }
}
