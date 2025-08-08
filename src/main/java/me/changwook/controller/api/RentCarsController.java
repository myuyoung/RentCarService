package me.changwook.controller.api;

import lombok.RequiredArgsConstructor;
import me.changwook.DTO.ApiResponseDTO;
import me.changwook.DTO.RentCarsDTO;
import me.changwook.service.impl.RentCarService;
import me.changwook.util.ResponseFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rentcars")
@RequiredArgsConstructor
public class RentCarsController {

    private final RentCarService rentCarService;
    private final ResponseFactory responseFactory;


    //렌트카의 랭킹을 조회하는 컨트롤러
    @GetMapping("/rank")
    public ResponseEntity<ApiResponseDTO<Page<RentCarsDTO>>> getRankedRentCars(
            @PageableDefault(size = 10, sort = "recommend", direction = Sort.Direction.DESC)Pageable pageable
            ){
        Page<RentCarsDTO> rankedCarsPage = rentCarService.getRankedRentCars(pageable);
        return responseFactory.success("렌트카 랭킹 조회 성공", rankedCarsPage);
    }
}
