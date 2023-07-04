package com.aiblossom.dto;

import lombok.Getter;

@Getter
public class EmailAuthRequestDto {

    private String email;
    private String authCode;
}
