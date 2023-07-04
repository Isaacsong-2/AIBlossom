package com.aiblossom.dto;

import lombok.Getter;

@Getter
public class ProfileRequestDto {
    private String username;
    private String password;
    private String introduction;

    public void setPassword(String password) {
        this.password = password;
    }
}