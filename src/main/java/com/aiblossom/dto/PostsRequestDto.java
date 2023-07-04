package com.aiblossom.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostsRequestDto {
    private String title;
    private String content;

    @Builder
    public PostsRequestDto(String title, String content){
        this.title = title;
        this.content = content;
    }

}
