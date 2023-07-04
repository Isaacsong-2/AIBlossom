package com.aiblossom.controller;

import com.aiblossom.common.security.UserDetailsImpl;
import com.aiblossom.dto.FeedRequestDto;
import com.aiblossom.dto.FeedResponseDto;
import com.aiblossom.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/blossom")
public class FeedApiController {

    private final FeedService feedService;

    @PostMapping("/feed")
    @ResponseBody
    public ResponseEntity<?> save(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody FeedRequestDto requestDto) {
        checkToken(userDetails);
        return ResponseEntity.ok(feedService.save(userDetails, requestDto));
    }

    @GetMapping("/feed/manage")
    public String saveManage(){
        return "createFeed";
    }

    @GetMapping("/feed")
    @ResponseBody
    public List<FeedResponseDto> findAll() {
        return feedService.findAll();
    }

    @GetMapping("/feed/{id}")
    public String findOne(@PathVariable Long id, Model model){
        FeedResponseDto responseDto = feedService.findOne(id);
        model.addAttribute("feed", responseDto);
        return "detail-feed";
    }

    @GetMapping("/feed/manage/{id}")
    public String updateManage(@PathVariable Long id, Model model){
        FeedResponseDto responseDto = feedService.findOne(id);
        model.addAttribute("feed", responseDto);
        return "manageFeed";
    }

    @PutMapping("/feed/{id}")
    @ResponseBody
    public ResponseEntity<?> update(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id, @RequestBody FeedRequestDto requestDto) {
        checkToken(userDetails);
        return ResponseEntity.ok(feedService.update(userDetails, id, requestDto));
    }

    @DeleteMapping("/feed/{id}")
    @ResponseBody
    public ResponseEntity<String> delete(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id) {
        checkToken(userDetails);
        feedService.delete(userDetails, id);
        return ResponseEntity.status(HttpStatus.OK).body("게시글 삭제 성공");
    }

    private void checkToken(UserDetailsImpl userDetails) {
        if (userDetails == null) throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
    }
}
