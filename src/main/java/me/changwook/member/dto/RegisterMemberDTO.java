package me.changwook.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterMemberDTO {

    @Schema(description = "사용자 이름", example = "홍길동",requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String name;

    @Schema(description = "사용자 이메일(이메일 형식 준수)", example = "test@example.com",requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    @Email
    private String email;

    @Schema(description = "비밀번호(비밀번호가 영어로만 이루어져야 하고 8자 이상, 특수문자 하나 이상, 숫자가 하나 이상, 16자리 이하로 설정)", example = "Testpassword1!",requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    //비밀번호가 영어로만 이루어져야 하고 8자 이상, 특수문자 하나 이상, 숫자가 하나 이상, 16자리 이하로 설정
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?])(?=.*[A-Za-z])[A-Za-z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]{8,16}$")
//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Schema(description = "전화번호", example = "010-1234-5678",requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String phone;

    @Schema(description = "주소", example = "서울시 강남구",requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String address;
}
