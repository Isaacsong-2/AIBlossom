package com.aiblossom.dto;

import com.aiblossom.entity.User;
import lombok.Getter;

@Getter
public class UserResponseDto {
    private String username;
    private String password;

    public UserResponseDto(User user){
        this.username = user.getUsername();
        this.password = user.getPassword();
    }
}
