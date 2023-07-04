package com.aiblossom.entity;

import com.aiblossom.dto.CommentUpdateRequestDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Comment extends TimeStamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "commentEntity", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<HeartComment> heartCommentList = new ArrayList<>();

    @Builder
    public Comment(String content, Feed feed, User user){
        this.content = content;
        this.feed = feed;
        this.user = user;
    }

    public void update(CommentUpdateRequestDto requestDto) {
        this.content = requestDto.getContent();
    }

}
