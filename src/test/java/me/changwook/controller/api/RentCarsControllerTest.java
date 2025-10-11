package me.changwook.controller.api;

import me.changwook.DTO.RentCarsDTO;
import me.changwook.service.impl.RentCarService;
import me.changwook.util.ResponseFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RentCarsController.class)
@ActiveProfiles("test")
public class RentCarsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RentCarService rentCarService;

    @MockBean
    private ResponseFactory responseFactory;

    @Test
    @DisplayName("렌트카 랭킹 조회 성공 테스트")
    @WithMockUser
    void getRankedRentCars_success() throws Exception {
        RentCarsDTO carDto = RentCarsDTO.builder()
                .id(1L)
                .name("테스트 차량")
                .rentCarNumber("12가3456")
                .rentPrice(50000)
                .recommend(100L)
                .build();

        Page<RentCarsDTO> mockPage = new PageImpl<>(Arrays.asList(carDto));
        
        when(rentCarService.getRankedRentCars(any(Pageable.class))).thenReturn(mockPage);
        when(responseFactory.success(anyString(), any())).thenReturn(
                org.springframework.http.ResponseEntity.ok().build());

        mockMvc.perform(get("/api/rentcars/rank"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("차량 검색 성공 테스트")
    @WithMockUser
    void searchCars_success() throws Exception {
        RentCarsDTO carDto = RentCarsDTO.builder()
                .id(2L)
                .name("검색된 차량")
                .rentCarNumber("56나7890")
                .rentPrice(45000)
                .recommend(80L)
                .build();

        Page<RentCarsDTO> mockPage = new PageImpl<>(Arrays.asList(carDto));
        
        when(rentCarService.searchCars(anyString(), anyString(), anyString(), anyInt(), anyInt(), any(Pageable.class)))
                .thenReturn(mockPage);
        when(responseFactory.success(anyString(), any())).thenReturn(
                org.springframework.http.ResponseEntity.ok().build());

        mockMvc.perform(get("/api/rentcars/search")
                .param("segment", "SUV")
                .param("fuelType", "GASOLINE")
                .param("q", "테스트")
                .param("minPrice", "30000")
                .param("maxPrice", "60000"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("차량 검색 파라미터 없이 성공 테스트")
    @WithMockUser
    void searchCars_withoutParameters_success() throws Exception {
        Page<RentCarsDTO> mockPage = new PageImpl<>(Arrays.asList());
        
        when(rentCarService.searchCars(any(), any(), any(), any(), any(), any(Pageable.class)))
                .thenReturn(mockPage);
        when(responseFactory.success(anyString(), any())).thenReturn(
                org.springframework.http.ResponseEntity.ok().build());

        mockMvc.perform(get("/api/rentcars/search"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("랭킹 조회 페이지네이션 테스트")
    @WithMockUser
    void getRankedRentCars_withPagination_success() throws Exception {
        Page<RentCarsDTO> mockPage = new PageImpl<>(Arrays.asList());
        
        when(rentCarService.getRankedRentCars(any(Pageable.class))).thenReturn(mockPage);
        when(responseFactory.success(anyString(), any())).thenReturn(
                org.springframework.http.ResponseEntity.ok().build());

        mockMvc.perform(get("/api/rentcars/rank")
                .param("page", "0")
                .param("size", "5")
                .param("sort", "recommend,desc"))
                .andExpect(status().isOk());
    }
}