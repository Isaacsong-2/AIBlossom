package com.aiblossom.service;

import com.aiblossom.common.Exception.BlossomErrorCode;
import com.aiblossom.common.Exception.BlossomException;
import com.aiblossom.common.constant.ProjConst;
import com.aiblossom.common.dto.ApiResult;
import com.aiblossom.common.jwt.JwtUtil;
import com.aiblossom.common.security.UserDetailsImpl;
import com.aiblossom.dto.CommentResponseDto;
import com.aiblossom.entity.Comment;
import com.aiblossom.entity.HeartComment;
import com.aiblossom.entity.User;
import com.aiblossom.repository.CommentRepository;
import com.aiblossom.repository.HeartCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HeartCommentService {

    private final HeartCommentRepository heartCommentRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public CommentResponseDto onClickCommentHeart(UserDetailsImpl userDetails, Long commentId) {
        // 토큰 체크
        User user = userDetails.getUser();
        if (user == null) {
            throw new BlossomException(BlossomErrorCode.NOT_FOUND_USER, null);
        }
        // 좋아요 누른 댓글 find
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new BlossomException(BlossomErrorCode.NOT_FOUND_COMMENT, null)
        );
        // 좋아요 누른 댓글이 본인 댓글이면 좋아요 불가능
        if (user.getId().equals(comment.getUser().getId())) {
            throw new BlossomException(BlossomErrorCode.CAN_NOT_MINE, null);
        }
        // 중복 좋아요 방지
        HeartComment heartComment = heartCommentRepository.findByComment_IdAndUser_Id(comment.getId(), user.getId());
        if (heartComment != null){
            throw new BlossomException(BlossomErrorCode.OVERLAP_HEART, null);
        }
        // HeartCommentRepository DB저장
        heartCommentRepository.save(new HeartComment(comment, user));
        return new CommentResponseDto(comment);
    }

    public ApiResult deleteCommentHeart(UserDetailsImpl userDetails, Long commentId) {
        // 토큰 체크
        User user = userDetails.getUser();

        if (user == null) {
            throw new BlossomException(BlossomErrorCode.NOT_FOUND_USER, null);
        }

        // HeartComment entity find
        HeartComment heartComment = heartCommentRepository.findByComment_IdAndUser_Id(commentId, user.getId());
        if (heartComment == null){
            throw new BlossomException(BlossomErrorCode.NOT_FOUND_HEART, null);
        }

        // 좋아요 누른 본인이거나 admin일경우만 삭제가능하도록 체크
        if (this.checkValidUser(user, heartComment)) {
            throw new BlossomException(BlossomErrorCode.UNAUTHORIZED_USER, null);
        }

        heartCommentRepository.delete(heartComment);
        return new ApiResult("좋아요 취소 성공", HttpStatus.OK.value());
    }

    /**
     * Check valid user.
     */
    private boolean checkValidUser(User user, HeartComment heartComment) {
        boolean result = !(user.getId().equals(heartComment.getUser().getId()))
                && !(user.getRole().equals(ProjConst.ADMIN_ROLE));  // 작성자와 로그인사용자가 같지 않으면서 관리자계정도 아닌것이 true.
        return result;
    }
}
