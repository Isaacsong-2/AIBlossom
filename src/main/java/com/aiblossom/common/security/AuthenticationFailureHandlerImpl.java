package com.aiblossom.common.security;

import com.aiblossom.dto.LoginResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        LoginResponseDto responseDto = new LoginResponseDto("login fail", 400);
        response.setContentType("application/json");
        response.getOutputStream().print(responseDto.toString());
    };
}
