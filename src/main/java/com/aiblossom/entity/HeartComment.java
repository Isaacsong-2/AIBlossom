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
    @Column(name = "heartComment_id")
    private Long heartCommentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_Id")
    Comment commentEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User userEntity;

    public HeartComment(Comment commentEntity, User userEntity) {
        this.commentEntity = commentEntity;
        this.userEntity = userEntity;
    }
}