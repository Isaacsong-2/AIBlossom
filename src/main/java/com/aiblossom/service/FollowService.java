package com.aiblossom.service;

import com.aiblossom.common.jwt.JwtUtil;
import com.aiblossom.common.security.UserDetailsImpl;
import com.aiblossom.entity.Follow;
import com.aiblossom.entity.HeartComment;
import com.aiblossom.entity.User;
import com.aiblossom.repository.FollowRepository;
import com.aiblossom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final JwtUtil jwtUtil;


    @Transactional
    public void following(@AuthenticationPrincipal UserDetailsImpl userDetails, Long userId) {
        // 토큰 체크
        User followerUser = userDetails.getUser();

        if (followerUser == null) {
            throw new IllegalArgumentException("로그인을 해주세요");
        }

        // 팔로우 할 유저 조회
        User followingUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));

        // 본인을 팔로우 할 경우 예외 발생
        if(followerUser.getId().equals(followerUser.getId())) {
            throw new IllegalArgumentException("본인을 팔로우 할 수 없습니다.");
        }

        // 중복 팔로우 예외 발생
        // followRepository 에서 두 개의 Id 값이 존재하는지 확인
        if (followRepository.findByFollowerUserAndFollowingUser(followerUser,followingUser).isPresent()) {
            throw new IllegalArgumentException("팔로우가 중복되었습니다.");
        }

        // HeartCommentRepository DB저장
        followRepository.save(new Follow(followingUser, followerUser));
    }

    @Transactional
    public void unfollowing(@AuthenticationPrincipal UserDetailsImpl userDetails, Long userId) {
        // 토큰 체크
        User followerUser = userDetails.getUser();

        if (followerUser == null) {
            throw new IllegalArgumentException("로그인을 해주세요");
        }

        // 팔로우 할 유저 조회
        User followingUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));

        // 팔로우 관계인지 확인
        if (followRepository.findByFollowerUserAndFollowingUser(followerUser, followingUser).isPresent()) {
            // HeartCommentRepository DB삭제
            followRepository.delete(new Follow(followingUser, followerUser));
        } else {
            throw new IllegalArgumentException("팔로우 관계가 아닙니다");
        }
    }
}
