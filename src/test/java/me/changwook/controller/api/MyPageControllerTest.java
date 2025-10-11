package me.changwook.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.changwook.DTO.*;
import me.changwook.configuration.config.security.CustomUserDetails;
import me.changwook.domain.Member;
import me.changwook.domain.Role;
import me.changwook.service.impl.CarRegistrationSubmissionService;
import me.changwook.service.impl.FileUploadService;
import me.changwook.service.impl.MemberService;
import me.changwook.service.impl.RentService;
import me.changwook.util.ResponseFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MyPageController.class)
@ActiveProfiles("test")
public class MyPageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    @MockBean
    private RentService rentService;

    @MockBean
    private CarRegistrationSubmissionService submissionService;

    @MockBean
    private FileUploadService fileUploadService;

    @MockBean
    private ResponseFactory responseFactory;

    private CustomUserDetails createMockUserDetails() {
        Member member = Member.builder()
                .id(UUID.randomUUID())
                .email("test@example.com")
                .name("테스트사용자")
                .role(Role.USER)
                .build();
        return new CustomUserDetails(member);
    }

    @Test
    @DisplayName("회원 정보 조회 성공 테스트")
    @WithMockUser
    void memberInformation_success() throws Exception {
        UUID memberId = UUID.randomUUID();
        MemberDTO memberDto = MemberDTO.builder()
                .id(memberId)
                .email("test@example.com")
                .name("테스트사용자")
                .build();

        when(memberService.findById(memberId)).thenReturn(memberDto);
        when(responseFactory.success(anyString(), any())).thenReturn(
                org.springframework.http.ResponseEntity.ok().build());

        mockMvc.perform(get("/api/MyPage/{memberId}", memberId))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원 정보 수정 성공 테스트")
    @WithMockUser
    void changeMemberInformation_success() throws Exception {
        UUID memberId = UUID.randomUUID();
        MemberDTO memberDto = MemberDTO.builder()
                .id(memberId)
                .email("updated@example.com")
                .name("수정된사용자")
                .build();

        when(responseFactory.success(anyString())).thenReturn(
                org.springframework.http.ResponseEntity.ok().build());

        mockMvc.perform(post("/api/MyPage/{memberId}/change", memberId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberDto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("차량 예약 성공 테스트")
    void reserveCar_success() throws Exception {
        CustomUserDetails userDetails = createMockUserDetails();
        
        RentDTO rentDto = RentDTO.builder()
                .rent_id(UUID.randomUUID())
                .rentTime(LocalDateTime.now().plusDays(1))
                .endTime(LocalDateTime.now().plusDays(3))
                .duration(2)
                .build();

        RentCarsDTO rentCarsDto = RentCarsDTO.builder()
                .id(1L)
                .name("테스트 차량")
                .rentPrice(50000)
                .build();

        rentDto.setRentCars(rentCarsDto);

        ReservationDTO reservationDto = new ReservationDTO();
        reservationDto.setRentDTO(rentDto);
        reservationDto.setRentCarsDTO(rentCarsDto);

        when(rentService.rentInformation(any(ReservationDTO.class), any(UUID.class))).thenReturn(rentDto);
        when(responseFactory.created(anyString(), any())).thenReturn(
                org.springframework.http.ResponseEntity.status(201).build());

        mockMvc.perform(post("/api/MyPage/reservation")
                .with(csrf())
                .with(user(userDetails))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservationDto)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("예약 목록 조회 성공 테스트")
    void getReservationList_success() throws Exception {
        CustomUserDetails userDetails = createMockUserDetails();

        RentDTO rentDto = RentDTO.builder()
                .rent_id(UUID.randomUUID())
                .rentTime(LocalDateTime.now().plusDays(1))
                .endTime(LocalDateTime.now().plusDays(3))
                .duration(2)
                .build();

        List<RentDTO> reservationList = Arrays.asList(rentDto);

        when(rentService.findReservationList(userDetails.getMember().getId())).thenReturn(reservationList);
        when(responseFactory.success(anyString(), any())).thenReturn(
                org.springframework.http.ResponseEntity.ok().build());

        mockMvc.perform(get("/api/MyPage/reservation/list")
                .with(user(userDetails)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("특정 예약 조회 성공 테스트")
    @WithMockUser
    void getReservation_success() throws Exception {
        UUID reservationId = UUID.randomUUID();
        
        RentDTO rentDto = RentDTO.builder()
                .rent_id(reservationId)
                .rentTime(LocalDateTime.now().plusDays(1))
                .endTime(LocalDateTime.now().plusDays(3))
                .duration(2)
                .build();

        when(rentService.findReservation(reservationId)).thenReturn(rentDto);
        when(responseFactory.success(anyString(), any())).thenReturn(
                org.springframework.http.ResponseEntity.ok().build());

        mockMvc.perform(get("/api/MyPage/reservation/list/{reservationId}", reservationId))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("예약 취소 성공 테스트")
    void cancelReservation_success() throws Exception {
        CustomUserDetails userDetails = createMockUserDetails();
        UUID reservationId = UUID.randomUUID();

        when(responseFactory.success(anyString())).thenReturn(
                org.springframework.http.ResponseEntity.ok().build());

        mockMvc.perform(delete("/api/MyPage/reservation/list/cancel/{reservationId}", reservationId)
                .with(csrf())
                .with(user(userDetails)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("차량 등록 신청 성공 테스트")
    void createCarSubmission_success() throws Exception {
        CustomUserDetails userDetails = createMockUserDetails();
        
        CarRegistrationSubmissionDTO submissionDto = CarRegistrationSubmissionDTO.builder()
                .carName("테스트 차량")
                .rentCarNumber("12가3456")
                .rentPrice(50000)
                .build();
        submissionDto.setMemberId(userDetails.getMember().getId());

        when(responseFactory.created(anyString(), any())).thenReturn(
                org.springframework.http.ResponseEntity.status(201).build());

        mockMvc.perform(post("/api/MyPage/car-submissions")
                .with(csrf())
                .with(user(userDetails))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(submissionDto)))
                .andExpect(status().isCreated());
    }
}