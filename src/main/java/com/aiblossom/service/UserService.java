package com.aiblossom.service;

import com.aiblossom.common.Exception.HanghaeBlogErrorCode;
import com.aiblossom.common.Exception.HanghaeBlogException;
import com.aiblossom.common.dto.ApiResult;
import com.aiblossom.common.jwt.JwtUtil;
import com.aiblossom.common.security.UserDetailsImpl;
import com.aiblossom.dto.PasswordRequestDto;
import com.aiblossom.dto.AuthRequestDto;
import com.aiblossom.dto.ProfileRequestDto;
import com.aiblossom.dto.ProfileResponseDto;
import com.aiblossom.entity.User;
import com.aiblossom.entity.UserRoleEnum;
import com.aiblossom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public void signup(AuthRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());
        UserRoleEnum role = requestDto.getRole();

        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 회원입니다.");
        }

        User user = new User(username, password, role);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public ProfileResponseDto getProfile(UserDetailsImpl userDetails) {
        User user = userDetails.getUser(); // 로그인 된 유저에 맞는 정보 담기

        return new ProfileResponseDto(user.getUsername(), user.getIntroduction()); // 해당 유저 정보 반환 / 메세지반환 필요없다고 하심
    }

    @Transactional
    public ApiResult checkPassword(UserDetailsImpl userDetails, PasswordRequestDto requestDto) {
        User user = userDetails.getUser();
        // 비밀번호 확인
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new HanghaeBlogException(HanghaeBlogErrorCode.WRONG_PASSWORD, null);
        }
        return new ApiResult("프로필 수정으로 넘어가기", HttpStatus.OK.value()); // 수정 페이지로 넘어가기 전 비밀번호 확인
    }

    @Transactional
    public ApiResult updateProfile(UserDetailsImpl userDetails, ProfileRequestDto requestDto) {
        User user = userDetails.getUser(); // 로그인 된 유저에 맞는 정보 담기
        requestDto.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        User targetUser = userRepository.findById(user.getId()).orElseThrow(() ->
                new HanghaeBlogException(HanghaeBlogErrorCode.NOT_FOUND_USER, null));
        targetUser.update(requestDto);
        return new ApiResult("정보 수정 완료", HttpStatus.OK.value());
    }
}
