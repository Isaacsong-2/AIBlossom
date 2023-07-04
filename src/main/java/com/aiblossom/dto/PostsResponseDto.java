package com.aiblossom.dto;

import com.aiblossom.entity.Posts;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostsResponseDto {
    private Long id;
    private String title;
    private String content;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private int heartNum;

    private List<CommentResponseDto> commentList;
    public PostsResponseDto(Posts posts) {
        this.id = posts.getId();
        this.title = posts.getTitle();
        this.content = posts.getContent();
        this.username = posts.getUser().getUsername();
        this.createdAt = posts.getCreatedAt();
        this.modifiedAt = posts.getModifiedAt();
        this.commentList = posts.getCommentList().stream()
                .map(CommentResponseDto::new)
                .sorted(Comparator.comparing(CommentResponseDto::getCreatedAt).reversed())
                .collect(Collectors.toList());
        this.heartNum = posts.getHeartFeedList().size();
    }
}
