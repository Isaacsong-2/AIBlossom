package com.aiblossom.service;


import com.aiblossom.common.security.UserDetailsImpl;
import com.aiblossom.dto.PostsRequestDto;
import com.aiblossom.dto.PostsResponseDto;
import com.aiblossom.entity.Posts;
import com.aiblossom.entity.UserRoleEnum;
import com.aiblossom.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PostsService {
    private final PostsRepository postsRepository;
    public PostsResponseDto save(UserDetailsImpl userDetails, PostsRequestDto requestDto) {
        Posts posts = Posts.builder()
                            .title(requestDto.getTitle())
                            .content(requestDto.getContent())
                            .user(userDetails.getUser())
                            .build();
        return new PostsResponseDto(postsRepository.save(posts));
    }

    public List<PostsResponseDto> findAll() {
        return postsRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(PostsResponseDto::new)
                .collect(Collectors.toList());
    }


    public PostsResponseDto findOne(Long id) {
        return new PostsResponseDto(postsRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 메모는 존재하지 않습니다.")));
    }

    @Transactional
    public PostsResponseDto update(UserDetailsImpl userDetails, Long id, PostsRequestDto requestDto) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Posts posts = postsRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 메모는 존재하지 않습니다."));
        if (posts.getUser().getId().equals(userDetails.getUser().getId()) ||
                userDetails.getRole().equals(UserRoleEnum.ADMIN.toString())) {
            posts.update(requestDto);
            return new PostsResponseDto(posts);
        } else throw new IllegalArgumentException("수정 권한이 없습니다.");
    }

    @Transactional
    public void delete(UserDetailsImpl userDetails, Long id) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Posts posts = postsRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 메모는 존재하지 않습니다."));
        if (posts.getUser().getId().equals(userDetails.getUser().getId()) ||
                userDetails.getRole().equals(UserRoleEnum.ADMIN.toString())) {
            postsRepository.delete(posts);
        } else throw new IllegalArgumentException("삭제 권한이 없습니다.");
    }
}
