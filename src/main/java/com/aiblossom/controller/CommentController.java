package com.aiblossom.controller;

import com.aiblossom.common.Exception.BlossomErrorCode;
import com.aiblossom.common.Exception.BlossomException;
import com.aiblossom.common.security.UserDetailsImpl;
import com.aiblossom.dto.CommentRequestDto;
import com.aiblossom.dto.CommentUpdateRequestDto;
import com.aiblossom.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blossom")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment")
    public ResponseEntity<?> save(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody CommentRequestDto requestDto){
        checkToken(userDetails);
        return ResponseEntity.ok(commentService.save(userDetails, requestDto));

    }

    @PutMapping("/comment/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody CommentUpdateRequestDto requestDto){
        checkToken(userDetails);
        return ResponseEntity.ok(commentService.update(id, userDetails, requestDto));
    }

    @DeleteMapping("/comment/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        checkToken(userDetails);
        commentService.delete(id, userDetails);
        return ResponseEntity.status(HttpStatus.OK).body("댓글 삭제 성공");
    }

    private void checkToken(UserDetailsImpl userDetails) {
        if (userDetails == null) throw new BlossomException(BlossomErrorCode.NOT_FOUND_USER,null);
    }

}
