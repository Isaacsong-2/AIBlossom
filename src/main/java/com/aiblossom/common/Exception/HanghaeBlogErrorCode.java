package com.aiblossom.common.Exception;

import lombok.Getter;

/**
 * HanghaeBlogErrorCode.
 */
@Getter
public enum HanghaeBlogErrorCode {


    INVALID_TOKEN(400, "토큰이 유효하지 않습니다."),
    UNAUTHORIZED_USER(400, "작성자(본인)만 수정/삭제/취소 할 수 있습니다."),
    IN_USED_ID(400, "중복된 ID 입니다."),
    NOT_FOUND_USER(400, "회원을 찾을 수 없습니다."),
    NOT_FOUND_POST(400, "요청한 게시글이 존재하지 않습니다."),
    WRONG_PASSWORD(400, "비밀번호가 틀렸습니다."),
    NOT_FOUND_COMMENT(400, "작성한 댓글을 찾을 수 없습니다."),
    WRONG_POSTID(400, "댓글이 위치한 게시글(postID)을 재확인 하십시오."),
    OVERLAP_HEART(400, "!중복 좋아요 누름!"),
    CAN_NOT_MINE(400, "본인이 작성한 게시글/댓글 좋아요 누를 수 없음!"),
    NOT_FOUND_HEART(400, "좋아요를 누르지 않았습니다." ),
    ;


    private int errorCode;
    private String errorMessage;

    HanghaeBlogErrorCode(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}