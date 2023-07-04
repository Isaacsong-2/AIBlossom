package com.aiblossom.service;

import com.aiblossom.common.Exception.HanghaeBlogErrorCode;
import com.aiblossom.common.Exception.HanghaeBlogException;
import com.aiblossom.common.constant.ProjConst;
import com.aiblossom.common.dto.ApiResult;
import com.aiblossom.common.jwt.JwtUtil;
import com.aiblossom.common.security.UserDetailsImpl;
import com.aiblossom.dto.FeedResponseDto;
import com.aiblossom.entity.Feed;
import com.aiblossom.entity.HeartFeed;
import com.aiblossom.entity.User;
import com.aiblossom.repository.FeedRepository;
import com.aiblossom.repository.HeartFeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HeartFeedService {

    private final FeedRepository postRepository;
    private final HeartFeedRepository heartFeedRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public FeedResponseDto onClickFeedkHeart(UserDetailsImpl userDetails, Long postId) {
        // 토큰 체크
        User user = userDetails.getUser();

        if (user == null) {
            throw new HanghaeBlogException(HanghaeBlogErrorCode.NOT_FOUND_USER, null);
        }

        // 좋아요 누른 게시글 find
        Feed feed = postRepository.findById(postId)
                .orElseThrow(() -> new HanghaeBlogException(HanghaeBlogErrorCode.NOT_FOUND_POST, null));

        // 좋아요누른게시글이 로그인사용자 본인게시글이면 좋아요 불가능
        if (user.getId().equals(feed.getUser().getId())) {
            throw new HanghaeBlogException(HanghaeBlogErrorCode.CAN_NOT_MINE, null);
        }

        // 중복 좋아요 방지
        List<HeartFeed> heartFeedList = heartFeedRepository.findAll();
        for (HeartFeed heartFeeds : heartFeedList) {
            if (feed.getId().equals(heartFeeds.getFeed().getId())
                    && user.getId().equals(heartFeeds.getUser().getId())) {
                throw new HanghaeBlogException(HanghaeBlogErrorCode.OVERLAP_HEART, null);
            }
        }

        // HeartFeedRepository DB저장
        heartFeedRepository.save(new HeartFeed(feed, user));

        return new FeedResponseDto(feed);
    }

    @Transactional
    public ApiResult deleteFeedHeart(UserDetailsImpl userDetails, Long heartFeedId) {
        // 토큰 체크
        User user = userDetails.getUser();

        if (user == null) {
            throw new HanghaeBlogException(HanghaeBlogErrorCode.NOT_FOUND_USER, null);
        }

        // HeartFeed entity find
        HeartFeed heartFeed = heartFeedRepository.findById(heartFeedId).orElseThrow(
                () -> new HanghaeBlogException(HanghaeBlogErrorCode.NOT_FOUND_HEART, null)
        );

        // 좋아요 누른 본인이거나 admin일경우만 삭제가능하도록 체크
        if (this.checkValidUser(user, heartFeed)) {
            throw new HanghaeBlogException(HanghaeBlogErrorCode.UNAUTHORIZED_USER, null);
        }

        heartFeedRepository.delete(heartFeed);

        return new ApiResult("좋아요 취소 성공", HttpStatus.OK.value());
    }

    /**
     * Check valid user.
     */
    private boolean checkValidUser(User user, HeartFeed heartFeed) {
        boolean result = !(user.getId().equals(heartFeed.getUser().getId()))
                && !(user.getRole().equals(ProjConst.ADMIN_ROLE));  // 작성자와 로그인사용자가 같지 않으면서 관리자계정도 아닌것이 true.
        return result;
    }
}
