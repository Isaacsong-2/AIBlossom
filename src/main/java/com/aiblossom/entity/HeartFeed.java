package com.aiblossom.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class HeartFeed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "heartFeed_id")
    private Long heartFeedId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    Posts posts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User userEntity;

    public HeartFeed(Posts postEntity, User userEntity) {
        this.posts = postEntity;
        this.userEntity = userEntity;
    }

}