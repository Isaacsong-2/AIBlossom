package com.aiblossom.controller;

import com.aiblossom.common.dto.ApiResult;
import com.aiblossom.common.security.UserDetailsImpl;
import com.aiblossom.dto.FeedResponseDto;
import com.aiblossom.service.HeartFeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blossom")
public class HeartFeedController {
    private final HeartFeedService heartFeedService;

    @PostMapping("/heart-feed/{feedId}")
    public FeedResponseDto onClickFeedHeart(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long feedId) {
        return heartFeedService.onClickFeedHeart(userDetails, feedId);
    }

    @DeleteMapping("/heart-feed/{feedId}")
    public ApiResult deleteFeedHeart(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long feedId) {
        return heartFeedService.deleteFeedHeart(userDetails, feedId);
    }
}