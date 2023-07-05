package com.aiblossom.dto;

import lombok.Getter;

@Getter
public class ProfileRequestDto {
    private String username;
    private String password;
    private String imageUrl;
    private String introduction;

    public void setPassword(String password) {
        this.password = password;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}