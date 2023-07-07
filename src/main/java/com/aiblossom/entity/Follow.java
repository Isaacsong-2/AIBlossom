package com.aiblossom.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import lombok.Setter;


@Entity
@Getter
@NoArgsConstructor
@Table(name = "follow")
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @ManyToOne(fetch = FetchType.LAZY)
    User followingUser;

    @ManyToOne(fetch = FetchType.LAZY)
    User followerUser;

    public Follow(User followingUser, User followerUser){
        this.followingUser = followingUser;
        this.followerUser = followerUser;
    }
}
