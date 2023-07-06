package com.aiblossom.entity;

import com.aiblossom.dto.ProfileRequestDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column
    private String email;

    @Column
    private String imageUrl;

    @Column
    private String introduction;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @OneToMany(mappedBy = "followingUser")
    private List<Follow> followingList = new ArrayList<>();

    @OneToMany(mappedBy = "followerUser")
    private List<Follow> followerList = new ArrayList<>();


    @Builder
    public User(String username, String password,String imageUrl, String email, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.imageUrl = imageUrl;
        this.email = email;
        this.role = role;
    }

    public User(String username, String password, String email, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public void update(ProfileRequestDto requestDto) {
        this.username = requestDto.getUsername();
        this.password = requestDto.getPassword();
        this.imageUrl = requestDto.getImageUrl();
        this.introduction = requestDto.getIntroduction();
    }

    public User update(String name, String profileImageUrl) {
        this.username = name;
        this.imageUrl = profileImageUrl;
        return this;
    }
}
