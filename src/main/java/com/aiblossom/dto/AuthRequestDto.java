package com.aiblossom.dto;

import com.aiblossom.entity.UserRoleEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequestDto {
    @Pattern(regexp = "^[a-z0-9]{4,10}$",
            message = "최소 4자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9) 로 구성되어야 합니다.")
    private String username;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9-_.!?^~]+@[a-zA-Z0-9]+[.]{1}[a-z]+$", message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+{}:\"<>?,.\\\\/]{8,15}$",
            message = "최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자 로 구성되어야 합니다.")
    private String password;

    private UserRoleEnum role = UserRoleEnum.USER; // 회원 권한 (ADMIN, USER)
}