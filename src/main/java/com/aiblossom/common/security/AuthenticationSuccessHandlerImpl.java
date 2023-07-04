package com.aiblossom.common.security;

import com.aiblossom.dto.LoginResponseDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_OK);
        LoginResponseDto responseDto = new LoginResponseDto("login success", 200);
        response.setContentType("application/json");
        response.getOutputStream().print(responseDto.toString());
    }
}
