package com.aiblossom.service;

import com.aiblossom.common.Exception.HanghaeBlogErrorCode;
import com.aiblossom.common.Exception.HanghaeBlogException;
import com.aiblossom.common.constant.ProjConst;
import com.aiblossom.common.dto.ApiResult;
import com.aiblossom.common.jwt.JwtUtil;
import com.aiblossom.common.security.UserDetailsImpl;
import com.aiblossom.dto.PostsResponseDto;
import com.aiblossom.entity.HeartFeed;
import com.aiblossom.entity.Posts;
import com.aiblossom.entity.User;
import com.aiblossom.repository.HeartFeedRepository;
import com.aiblossom.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HeartFeedService {

    private final PostsRepository postRepository;
    private final HeartFeedRepository heartFeedRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public PostsResponseDto onClickFeedkHeart(UserDetailsImpl userDetails, Long postId) {
        // 토큰 체크
        User userEntity = userDetails.getUser();

        if (userEntity == null) {
            throw new HanghaeBlogException(HanghaeBlogErrorCode.NOT_FOUND_USER, null);
        }

        // 좋아요 누른 게시글 find
        Posts postEntity = postRepository.findById(postId)
                .orElseThrow(() -> new HanghaeBlogException(HanghaeBlogErrorCode.NOT_FOUND_POST, null));

        // 좋아요누른게시글이 로그인사용자 본인게시글이면 좋아요 불가능
        if (userEntity.getId().equals(postEntity.getUser().getId())) {
            throw new HanghaeBlogException(HanghaeBlogErrorCode.CAN_NOT_MINE, null);
        }

        // 중복 좋아요 방지
        List<HeartFeed> heartFeedList = heartFeedRepository.findAll();
        for (HeartFeed heartFeeds : heartFeedList) {
            if (postEntity.getId().equals(heartFeeds.getPosts().getId())
                    && userEntity.getId().equals(heartFeeds.getUserEntity().getId())) {
                throw new HanghaeBlogException(HanghaeBlogErrorCode.OVERLAP_HEART, null);
            }
        }

        // HeartFeedRepository DB저장
        heartFeedRepository.save(new HeartFeed(postEntity, userEntity));

        return new PostsResponseDto(postEntity);
    }

    @Transactional
    public ApiResult deleteFeedHeart(UserDetailsImpl userDetails, Long heartFeedId) {
        // 토큰 체크
        User userEntity = userDetails.getUser();

        if (userEntity == null) {
            throw new HanghaeBlogException(HanghaeBlogErrorCode.NOT_FOUND_USER, null);
        }

        // HeartFeed entity find
        HeartFeed heartFeed = heartFeedRepository.findById(heartFeedId).orElseThrow(
                () -> new HanghaeBlogException(HanghaeBlogErrorCode.NOT_FOUND_HEART, null)
        );

        // 좋아요 누른 본인이거나 admin일경우만 삭제가능하도록 체크
        if (this.checkValidUser(userEntity, heartFeed)) {
            throw new HanghaeBlogException(HanghaeBlogErrorCode.UNAUTHORIZED_USER, null);
        }

        heartFeedRepository.delete(heartFeed);

        return new ApiResult("좋아요 취소 성공", HttpStatus.OK.value());
    }

    /**
     * Check valid user.
     */
    private boolean checkValidUser(User userEntity, HeartFeed heartFeed) {
        boolean result = !(userEntity.getId().equals(heartFeed.getUserEntity().getId()))
                && !(userEntity.getRole().equals(ProjConst.ADMIN_ROLE));  // 작성자와 로그인사용자가 같지 않으면서 관리자계정도 아닌것이 true.
        return result;
    }
}
