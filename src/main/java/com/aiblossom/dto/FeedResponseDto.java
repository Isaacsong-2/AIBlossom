package com.aiblossom.dto;

import com.aiblossom.entity.Feed;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class FeedResponseDto {
    private Long id;
    private String title;
    private String content;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private int heartNum;

    private List<CommentResponseDto> commentList;
    public FeedResponseDto(Feed feed) {
        this.id = feed.getId();
        this.title = feed.getTitle();
        this.content = feed.getContent();
        this.username = feed.getUser().getUsername();
        this.createdAt = feed.getCreatedAt();
        this.modifiedAt = feed.getModifiedAt();
        this.commentList = feed.getCommentList().stream()
                .map(CommentResponseDto::new)
                .sorted(Comparator.comparing(CommentResponseDto::getCreatedAt).reversed())
                .collect(Collectors.toList());
        this.heartNum = feed.getHeartFeedList().size();
    }
}
