package com.aiblossom.controller;

import com.aiblossom.common.Exception.BlossomErrorCode;
import com.aiblossom.common.Exception.BlossomException;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/blossom")
public class FeedApiController {

    private final FeedService feedService;

    @PostMapping("/feed")
    @ResponseBody
    public ResponseEntity<?> save(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                  @RequestParam(value = "image", required = false) MultipartFile image,
                                  @RequestParam(value = "title") String title,
                                  @RequestParam(value = "content") String content) throws IOException {
        FeedRequestDto requestDto = new FeedRequestDto();
        requestDto.setTitle(title);
        requestDto.setContent(content);

        checkToken(userDetails);
        return ResponseEntity.ok(feedService.save(userDetails, requestDto, image));
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
    public ResponseEntity<?> update(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id,
                                    @RequestParam(value = "image", required = false) MultipartFile image,
                                    @RequestParam(value = "title") String title,
                                    @RequestParam(value = "content") String content) throws IOException {
        FeedRequestDto requestDto = new FeedRequestDto();
        requestDto.setTitle(title);
        requestDto.setContent(content);

        checkToken(userDetails);
        return ResponseEntity.ok(feedService.update(userDetails, id, requestDto, image));
    }

    @DeleteMapping("/feed/{id}")
    @ResponseBody
    public ResponseEntity<String> delete(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id) {
        checkToken(userDetails);
        feedService.delete(userDetails, id);
        return ResponseEntity.status(HttpStatus.OK).body("게시글 삭제 성공");
    }

    private void checkToken(UserDetailsImpl userDetails) {
        if (userDetails == null) throw new BlossomException(BlossomErrorCode.NOT_FOUND_USER, null);
    }
}
