package com.aiblossom.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoDto {
    String username;
    String introduction;
    boolean isAdmin;
    String imageUrl;
}