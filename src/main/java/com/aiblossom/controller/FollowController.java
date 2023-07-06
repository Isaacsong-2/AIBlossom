package com.aiblossom.controller;

import com.aiblossom.common.security.UserDetailsImpl;
import com.aiblossom.dto.UserResponseDto;
import com.aiblossom.service.FollowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/blossom/follow")
public class FollowController {

    private final FollowService followService;

    @PostMapping("/{id}")
    public void following(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id) {
        followService.following(userDetails, id);
    }

    @DeleteMapping("/{id}")
    public void unfollowing(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id) {
        followService.unfollowing(userDetails, id);
    }
}
