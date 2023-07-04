package com.aiblossom.controller;

import com.aiblossom.common.dto.ApiResult;
import com.aiblossom.common.security.UserDetailsImpl;
import com.aiblossom.dto.CommentResponseDto;
import com.aiblossom.service.HeartCommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blossom")
public class HeartCommentController {
    private final HeartCommentService heartCommentService;

    @PostMapping("/heart-comment/{commentId}")
    public CommentResponseDto onClickCommentHeart(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long commentId) {
        return heartCommentService.onClickCommentHeart(userDetails, commentId);
    }

    @DeleteMapping("/heart-comment/{heartCommentId}")
    public ApiResult deleteCommentHeart(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long heartCommentId){
        return heartCommentService.deleteCommentHeart(userDetails, heartCommentId);
    }
}
