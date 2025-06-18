package me.changwook.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import me.changwook.domain.Member;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterMemberDTO {

    @Schema(description = "사용자 이름", example = "홍길동")
    @NotBlank
    private String name;

    @Schema(description = "사용자 이메일", example = "test@example.com")
    @NotBlank
    @Email
    private String email;

    @Schema(description = "비밀번호(비밀번호가 영어로만 이루어져야 하고 8자 이상, 특수문자 하나 이상, 숫자가 하나 이상, 16자리 이하로 설정)", example = "Testpassword1!")
    @NotBlank
    //비밀번호가 영어로만 이루어져야 하고 8자 이상, 특수문자 하나 이상, 숫자가 하나 이상, 16자리 이하로 설정
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?])(?=.*[A-Za-z])[A-Za-z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]{8,16}$")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Schema(description = "전화번호", example = "010-1234-5678")
    @NotBlank
    private String phone;

    @Schema(description = "주소", example = "서울시 강남구")
    @NotBlank
    private String address;
}
