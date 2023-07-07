package com.aiblossom.dto;

import com.aiblossom.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoDto {
    Long id;
    String username;
    String introduction;
    boolean isAdmin;
    String imageUrl;

    public UserInfoDto(User user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.introduction = user.getIntroduction();
        this.isAdmin = false;
        this.imageUrl = user.getImageUrl();
    }
}