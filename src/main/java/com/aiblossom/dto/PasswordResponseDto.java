package com.aiblossom.dto;

import lombok.Getter;

@Getter
public class PasswordResponseDto {
    boolean isCorrect;

    public PasswordResponseDto(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }
}
