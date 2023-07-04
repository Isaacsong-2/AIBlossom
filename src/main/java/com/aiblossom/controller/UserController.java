package com.aiblossom.controller;

import com.aiblossom.common.dto.ApiResult;
import com.aiblossom.common.jwt.JwtUtil;
import com.aiblossom.common.security.UserDetailsImpl;
import com.aiblossom.dto.PasswordRequestDto;
import com.aiblossom.dto.*;
import com.aiblossom.entity.UserRoleEnum;
import com.aiblossom.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/blossom")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @GetMapping("/user/signup")
    public String viewSignup(){
        return "signup";
    }

    @ResponseBody
    @PostMapping("/user/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid AuthRequestDto requestDto, BindingResult bindingResult){
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if(fieldErrors.size()> 0 ){
            for (FieldError fieldError : fieldErrors){
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원 저장 실패");
        }
        userService.signup(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body("회원가입 성공");
    }

    @GetMapping("/user/login")
    public String viewLogin(){
        return "login";
    }

    @GetMapping("/user/user-info")
    @ResponseBody
    public UserInfoDto getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getUser().getUsername();
        UserRoleEnum role = userDetails.getUser().getRole();
        boolean isAdmin = (role == UserRoleEnum.ADMIN);
        return new UserInfoDto(username, isAdmin);
    }

    @GetMapping("/user/profile")
    @ResponseBody
    public ProfileResponseDto getProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.getProfile(userDetails);
    }

    @PostMapping("/user/profile")
    @ResponseBody
    public ApiResult checkPassword(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody PasswordRequestDto requestDto) {
        return userService.checkPassword(userDetails, requestDto);
    }

    @PutMapping("/user/profile")
    @ResponseBody
    public ApiResult updateProfile(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody ProfileRequestDto requestDto) {
        return userService.updateProfile(userDetails, requestDto);
    }
}
