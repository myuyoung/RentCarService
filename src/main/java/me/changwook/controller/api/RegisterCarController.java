package me.changwook.controller.api;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import me.changwook.DTO.ApiResponseDTO;
import me.changwook.DTO.RentCarsDTO;
import me.changwook.service.impl.RegisterService;
import me.changwook.service.impl.RentCarService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/registerCar")
@RequiredArgsConstructor
public class RegisterCarController {

    private final RentCarService rentCarService;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<RentCarsDTO>> registerCar (@Validated @RequestBody RentCarsDTO rentCarsDTO) {
        rentCarService.registerCar(rentCarsDTO);

        ApiResponseDTO<RentCarsDTO> responseDTO = new ApiResponseDTO<>(true,"차량이 등록되었습니다.", rentCarsDTO);

        return ResponseEntity.ok(responseDTO);
    }
}
