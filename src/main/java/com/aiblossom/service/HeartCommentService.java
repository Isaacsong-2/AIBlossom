package com.aiblossom.service;

import com.aiblossom.common.Exception.HanghaeBlogErrorCode;
import com.aiblossom.common.Exception.HanghaeBlogException;
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
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HeartCommentService {

    private final HeartCommentRepository heartCommentRepository;
    private final CommentRepository commentRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public CommentResponseDto onClickCommentHeart(UserDetailsImpl userDetails, Long commentId) {
        // 토큰 체크
        User user = userDetails.getUser();

        if (user == null) {
            throw new IllegalArgumentException("로그인 하세요");
        }

        // 좋아요 누른 댓글 find
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("댓글이 없습니다.")
        );

        // 좋아요 누른 댓글이 본인 댓글이면 좋아요 불가능
        if (user.getId().equals(comment.getUser().getId())) {
            throw new IllegalArgumentException("본인이 작성한 댓글에는 좋아요가 불가능합니다.");
        }

        // 중복 좋아요 방지
        List<Comment> commentList = commentRepository.findAll();
        for (Comment comments : commentList) {
            if (comments.getId().equals(comment.getId())
                    && comments.getUser().getId().equals(user.getId())) {
                throw new IllegalArgumentException("한 댓글에 좋아요는 한 번만 가능합니다.");
            }
        }

        // HeartCommentRepository DB저장
        heartCommentRepository.save(new HeartComment(comment, user));


        return new CommentResponseDto(comment);
    }

    public ApiResult deleteCommentHeart(UserDetailsImpl userDetails, Long heartCommentId) {
        // 토큰 체크
        User user = userDetails.getUser();

        if (user == null) {
            throw new HanghaeBlogException(HanghaeBlogErrorCode.NOT_FOUND_USER, null);
        }

        // HeartComment entity find
        HeartComment heartComment = heartCommentRepository.findById(heartCommentId).orElseThrow(
                () -> new HanghaeBlogException(HanghaeBlogErrorCode.NOT_FOUND_HEART, null)
        );

        // 좋아요 누른 본인이거나 admin일경우만 삭제가능하도록 체크
        if (this.checkValidUser(user, heartComment)) {
            throw new HanghaeBlogException(HanghaeBlogErrorCode.UNAUTHORIZED_USER, null);
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
