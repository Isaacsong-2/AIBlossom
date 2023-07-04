package com.aiblossom.entity;

import com.aiblossom.dto.FeedRequestDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Feed extends TimeStamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "feed", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "feed", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<HeartFeed> heartFeedList = new ArrayList<>();

    @Builder
    public Feed(String title, String content, User user){
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public void update(FeedRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }
}
