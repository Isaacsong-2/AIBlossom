package com.aiblossom.service;

import com.aiblossom.common.jwt.JwtUtil;
import com.aiblossom.common.security.UserDetailsImpl;
import com.aiblossom.entity.Follow;
import com.aiblossom.entity.User;
import com.aiblossom.repository.FeedRepository;
import com.aiblossom.repository.FollowRepository;
import com.aiblossom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final FeedRepository feedRepository;
    private final JwtUtil jwtUtil;


    @Transactional
    public void following(@AuthenticationPrincipal UserDetailsImpl userDetails, Long id) {
        // 토큰 체크
        User followerUser = userDetails.getUser();

        if (followerUser == null) {
            throw new IllegalArgumentException("로그인을 해주세요");
        }

        // 팔로우 할 유저 조회
        User followingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));

        // 본인을 팔로우 할 경우 예외 발생
        if (followerUser.getId().equals(followingUser.getId())) {
            throw new IllegalArgumentException("본인을 팔로우 할 수 없습니다.");
        }

        // 중복 팔로우 예외 발생
        // followRepository 에서 두 개의 Id 값이 존재하는지 확인
        if (followRepository.findByFollowerUserAndFollowingUser(followerUser, followingUser).isPresent()) {
            throw new IllegalArgumentException("팔로우가 중복되었습니다.");
        }

        // followRepository DB 저장
        followRepository.save(new Follow(followingUser, followerUser));


    }


    @Transactional
    public void unfollowing(@AuthenticationPrincipal UserDetailsImpl userDetails, Long id) {
        // 토큰 체크
        User followerUser = userDetails.getUser();

        if (followerUser == null) {
            throw new IllegalArgumentException("로그인을 해주세요");
        }

        // 언팔로우 할 유저 조회
        User followingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));

        Follow follow = followRepository.findByFollowerUserAndFollowingUser(followerUser, followingUser)
                .orElseThrow(() -> new IllegalArgumentException("팔로우 관계가 아닙니다"));

        // followRepository DB 삭제
        followRepository.delete(follow);
        }
    }
