package com.aiblossom.controller;

import com.aiblossom.common.security.UserDetailsImpl;
import com.aiblossom.dto.UserResponseDto;
import com.aiblossom.service.FollowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/follow")
public class FollowController {

    private final FollowService followService;

    @PostMapping("/{userId}")
    public void following(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long userId) {
        followService.following(userDetails,userId);

    }
}
