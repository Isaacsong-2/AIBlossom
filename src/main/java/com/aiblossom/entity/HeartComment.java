package com.aiblossom.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class HeartComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long heartCommentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_Id")
    Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    public HeartComment(Comment comment, User user) {
        this.comment = comment;
        this.user = user;
    }
}