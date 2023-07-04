package com.aiblossom.service;

import com.aiblossom.common.Exception.HanghaeBlogErrorCode;
import com.aiblossom.common.Exception.HanghaeBlogException;
import com.aiblossom.common.dto.ApiResult;
import com.aiblossom.common.jwt.JwtUtil;
import com.aiblossom.common.security.UserDetailsImpl;
import com.aiblossom.dto.*;
import com.aiblossom.entity.User;
import com.aiblossom.entity.UserRoleEnum;
import com.aiblossom.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;
    private final JwtUtil jwtUtil;

    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public void signup(AuthRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String email = requestDto.getEmail();
        UserRoleEnum role = requestDto.getRole();

        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 회원입니다.");
        }

        User user = new User(username, password, email, role); // 이메일 추가
        userRepository.save(user);
    }

    public void checkMail(EmailAuthRequestDto requestDto) {
        String authCode = sendMail(requestDto.getEmail());

        if (!authCode.equals(requestDto.getAuthCode())) {
            throw new IllegalArgumentException("인증번호가 일치하지 않습니다.");
        }
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

    private String sendMail(String email){
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        String authCode = createCode();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            mimeMessageHelper.setTo(email); // 메일 수신자
            mimeMessageHelper.setSubject("이메일 인증을 위한 인증 코드 발송"); // 메일 제목
            mimeMessageHelper.setText(authCode); // 인증 코드
            javaMailSender.send(mimeMessage);
            return authCode;

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private String createCode() {    // 이메일 인증번호 생성 메서드
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(4);

            switch (index) {
                case 0:
                    key.append((char) ((int) random.nextInt(26) + 97));
                    break;
                case 1:
                    key.append((char) ((int) random.nextInt(26) + 65));
                    break;
                default:
                    key.append(random.nextInt(9));
            }
        }
        return key.toString();
    }
}
