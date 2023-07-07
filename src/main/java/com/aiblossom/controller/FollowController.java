package com.aiblossom.controller;


import com.aiblossom.common.security.UserDetailsImpl;
import com.aiblossom.dto.FeedResponseDto;
import com.aiblossom.entity.Feed;
import com.aiblossom.service.FollowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/blossom/follow")
public class FollowController {

    private final FollowService followService;

    @GetMapping
    public String follow(){
        return "follow";
    }

    @PostMapping("/{id}")
    @ResponseBody
    public void following(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id) {
        followService.following(userDetails, id);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public void unfollowing(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id) {
        followService.unfollowing(userDetails, id);
    }

    @GetMapping("/following/post")
    @ResponseBody
    public List<FeedResponseDto> postList(@AuthenticationPrincipal UserDetailsImpl userDetail){
        return followService.viewFollowingPostList(userDetail.getUser());
    }
}
