package com.aiblossom.controller;

import com.aiblossom.common.dto.ApiResult;
import com.aiblossom.common.security.UserDetailsImpl;
import com.aiblossom.dto.*;
import com.aiblossom.entity.User;
import com.aiblossom.entity.UserRoleEnum;
import com.aiblossom.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/blossom")
public class UserController {
    private final UserService userService;

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

    @ResponseBody
    @PostMapping("/user/email-auth")
    public String sendMail(@RequestBody EmailAuthRequestDto requestDto) throws MessagingException {

        return  userService.sendMail(requestDto.getEmail());
    }

    @GetMapping("/user/login")
    public String viewLogin(){
        return "login";
    }

    @GetMapping("/user-info")
    @ResponseBody
    public UserInfoDto getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        Long id = user.getId();
        String username = user.getUsername();
        String introduction = user.getIntroduction();
        UserRoleEnum role = user.getRole();
        String imageUrl = user.getImageUrl();
        boolean isAdmin = (role == UserRoleEnum.ADMIN);
        return new UserInfoDto(id, username, introduction, isAdmin, imageUrl);
    }

    @GetMapping("/users")
    @ResponseBody
    public List<UserInfoDto> getUsers(){
        return userService.findAll();
    }

    @GetMapping("/user/profile/manage")
    public String viewProfileManage(){
        return "profile";
    }

    @GetMapping("/user/profile")
    @ResponseBody
    public ProfileResponseDto getProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.getProfile(userDetails);
    }

    @PostMapping("/user/profile")
    @ResponseBody
    public PasswordResponseDto checkPassword(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody PasswordRequestDto requestDto) {
        return new PasswordResponseDto(userService.checkPassword(userDetails, requestDto));
    }

    @PutMapping(value = "/user/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ApiResult updateProfile(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                   @RequestParam(value = "image", required = false) MultipartFile image,
                                   @RequestParam(value = "username") String username,
                                   @RequestParam(value = "password") String password,
                                   @RequestParam(value = "introduction") String introduction) throws IOException {
        ProfileRequestDto requestDto = new ProfileRequestDto();
        requestDto.setUsername(username);
        requestDto.setPassword(password);
        requestDto.setIntroduction(introduction);

        return userService.updateProfile(userDetails, requestDto, image);
    }
}
