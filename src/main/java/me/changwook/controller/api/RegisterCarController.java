package me.changwook.controller.api;

import lombok.RequiredArgsConstructor;
import me.changwook.DTO.ApiResponseDTO;
import me.changwook.DTO.RentCarsDTO;
import me.changwook.service.impl.RentCarService;
import me.changwook.util.ResponseFactory;
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
    private final ResponseFactory responseFactory;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<RentCarsDTO>> registerCar (@Validated @RequestBody RentCarsDTO rentCarsDTO) {
        rentCarService.registerCar(rentCarsDTO);

        return responseFactory.created("차량이 등록되었습니다.", rentCarsDTO);
    }
}
