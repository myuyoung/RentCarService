package me.changwook.rentcar;

import lombok.RequiredArgsConstructor;
import me.changwook.common.ApiResponse;
import me.changwook.rentcar.dto.RentCarsDTO;
import me.changwook.common.ResponseFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<ApiResponse<Page<RentCarsDTO>>> getRankedRentCars(
            @PageableDefault(size = 10, sort = "recommend", direction = Sort.Direction.DESC)Pageable pageable
            ){
        Page<RentCarsDTO> rankedCarsPage = rentCarService.getRankedRentCars(pageable);
        return responseFactory.success("렌트카 랭킹 조회 성공", rankedCarsPage);
    }

    // 검색 API: 차종 세그먼트/연료/키워드/가격범위로 필터링
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<RentCarsDTO>>> searchCars(
            @RequestParam(value = "segment", required = false) String segment,
            @RequestParam(value = "fuelType", required = false) String fuelType,
            @RequestParam(value = "q", required = false) String keyword,
            @RequestParam(value = "minPrice", required = false) Integer minPrice,
            @RequestParam(value = "maxPrice", required = false) Integer maxPrice,
            @PageableDefault(size = 12, sort = "recommend", direction = Sort.Direction.DESC) Pageable pageable
    ){
        Page<RentCarsDTO> page = rentCarService.searchCars(segment, fuelType, keyword, minPrice, maxPrice, pageable);
        return responseFactory.success("차량 검색 성공", page);
    }
}
