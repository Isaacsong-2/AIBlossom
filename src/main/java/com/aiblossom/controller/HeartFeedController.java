package com.aiblossom.controller;

import com.aiblossom.common.dto.ApiResult;
import com.aiblossom.common.security.UserDetailsImpl;
import com.aiblossom.dto.PostsResponseDto;
import com.aiblossom.service.HeartFeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blossom")
public class HeartFeedController {
    private final HeartFeedService heartFeedService;

    @PostMapping("/heart-feed/{postId}")
    public PostsResponseDto onClickFeedHeart(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long postId) {
        return heartFeedService.onClickFeedkHeart(userDetails, postId);
    }

    @DeleteMapping("/heart-feed/{heartFeedId}")
    public ApiResult deleteFeedHeart(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long heartFeedId) {
        return heartFeedService.deleteFeedHeart(userDetails, heartFeedId);
    }
}