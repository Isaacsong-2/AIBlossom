package com.aiblossom.common.oauth2;

import com.aiblossom.common.jwt.JwtUtil;
import com.aiblossom.common.security.UserDetailsImpl;
import com.aiblossom.common.security.UserDetailsServiceImpl;
import com.aiblossom.dto.AuthRequestDto;
import com.aiblossom.entity.User;
import com.aiblossom.entity.UserRoleEnum;
import com.aiblossom.service.UserService;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


import java.io.IOException;
import java.net.URLEncoder;

@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println(authentication.getName());
        System.out.println(authentication.getPrincipal());
        System.out.println(authentication.getAuthorities());
        String username = (String) ((OAuth2User) authentication.getPrincipal()).getAttributes().get("name");
        System.out.println(username);
        UserRoleEnum role = UserRoleEnum.USER;
        String token = jwtUtil.createToken(username, role);
        token = URLEncoder.encode(token, "utf-8").replaceAll("\\+", "%20");
//        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);
        // 쿠키 생성
        Cookie cookie = new Cookie(jwtUtil.AUTHORIZATION_HEADER, token);
        cookie.setMaxAge(3600); // 쿠키 유효 기간 설정 (여기서는 1시간)
        cookie.setPath("/"); // 쿠키의 유효 범위 설정 (루트 경로에 모든 요청에 대해 쿠키 전송)
        // 쿠키를 응답 헤더에 추가
        response.addCookie(cookie);
        response.sendRedirect("/");

        log.info("로그인성공");
    }
}