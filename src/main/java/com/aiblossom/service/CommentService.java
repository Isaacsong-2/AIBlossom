package com.aiblossom.service;

import com.aiblossom.common.security.UserDetailsImpl;
import com.aiblossom.dto.CommentRequestDto;
import com.aiblossom.dto.CommentResponseDto;
import com.aiblossom.dto.CommentUpdateRequestDto;
import com.aiblossom.entity.Comment;
import com.aiblossom.entity.Feed;
import com.aiblossom.entity.UserRoleEnum;
import com.aiblossom.repository.CommentRepository;
import com.aiblossom.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final FeedRepository feedRepository;
    private final CommentRepository commentRepository;

    public CommentResponseDto save(UserDetailsImpl userDetails, CommentRequestDto requestDto) {
        Feed feed = feedRepository.findById(requestDto.getFeedId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        Comment comment = new Comment(requestDto.getContent(), feed, userDetails.getUser());
        commentRepository.save(comment);
        return new CommentResponseDto(comment, userDetails.getUsername());
    }

    public CommentResponseDto update(Long id, UserDetailsImpl userDetails, CommentUpdateRequestDto requestDto) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
        if (userDetails.getUser().getId().equals(comment.getUser().getId()) || userDetails.getRole().equals(UserRoleEnum.ADMIN.toString())) {
            comment.update(requestDto);
        } else throw new IllegalArgumentException("수정 권한이 없습니다.");
        return new CommentResponseDto(comment, userDetails.getUsername());
    }

    public void delete(Long id, UserDetailsImpl userDetails) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
        if (userDetails.getUser().getId().equals(comment.getUser().getId()) || userDetails.getRole().equals(UserRoleEnum.ADMIN.toString())) {
            commentRepository.delete(comment);
        } else throw new IllegalArgumentException("삭제 권한이 없습니다.");
    }
}
