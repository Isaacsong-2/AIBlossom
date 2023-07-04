package com.aiblossom.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {
    private String msg;
    private int statusCode;

    @Override
    public String toString() {
        return "{\n" +
                "  msg='" + msg + "\'" +
                ",\n  statusCode=" + statusCode +
                '}';
    }
}
