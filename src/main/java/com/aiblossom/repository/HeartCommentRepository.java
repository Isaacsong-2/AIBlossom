package com.aiblossom.repository;

import com.aiblossom.entity.HeartComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeartCommentRepository extends JpaRepository<HeartComment, Long> {

    HeartComment findByComment_IdAndUser_Id(Long commentId, Long userId);
}
