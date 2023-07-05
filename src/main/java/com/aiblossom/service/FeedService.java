package com.aiblossom.service;


import com.aiblossom.common.image.ImageUploader;
import com.aiblossom.common.security.UserDetailsImpl;
import com.aiblossom.dto.FeedRequestDto;
import com.aiblossom.dto.FeedResponseDto;
import com.aiblossom.entity.Feed;
import com.aiblossom.entity.UserRoleEnum;
import com.aiblossom.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedService {
    private final FeedRepository feedRepository;
    private final ImageUploader imageUploader;

    public FeedResponseDto save(UserDetailsImpl userDetails, FeedRequestDto requestDto, MultipartFile image) {
        if (image != null) {
            try {
                String imageUrl = imageUploader.upload(image, "image");
                requestDto.setImageUrl(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        Feed feed = Feed.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .imageUrl(requestDto.getImageUrl())
                .user(userDetails.getUser())
                .build();
        return new FeedResponseDto(feedRepository.save(feed));
    }

    public List<FeedResponseDto> findAll() {
        return feedRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(FeedResponseDto::new)
                .collect(Collectors.toList());
    }


    public FeedResponseDto findOne(Long id) {
        return new FeedResponseDto(feedRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 메모는 존재하지 않습니다.")));
    }

    @Transactional
    public FeedResponseDto update(UserDetailsImpl userDetails, Long id, FeedRequestDto requestDto, MultipartFile image) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Feed feed = feedRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 메모는 존재하지 않습니다."));
        if (feed.getUser().getId().equals(userDetails.getUser().getId()) ||
                userDetails.getRole().equals(UserRoleEnum.ADMIN.toString())) {
            if (image != null) {
                try {
                    String imageUrl = imageUploader.upload(image, "image");
                    requestDto.setImageUrl(imageUrl);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            feed.update(requestDto);
            return new FeedResponseDto(feed);
        } else throw new IllegalArgumentException("수정 권한이 없습니다.");
    }

    @Transactional
    public void delete(UserDetailsImpl userDetails, Long id) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Feed feed = feedRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 메모는 존재하지 않습니다."));
        if (feed.getUser().getId().equals(userDetails.getUser().getId()) ||
                userDetails.getRole().equals(UserRoleEnum.ADMIN.toString())) {
            feedRepository.delete(feed);
        } else throw new IllegalArgumentException("삭제 권한이 없습니다.");
    }
}
