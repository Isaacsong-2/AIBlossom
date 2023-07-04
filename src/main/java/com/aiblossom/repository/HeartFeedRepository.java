package com.aiblossom.repository;

import com.aiblossom.entity.HeartFeed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HeartFeedRepository extends JpaRepository<HeartFeed, Long> {
    HeartFeed findByFeed_IdAndUser_Id(Long feedId, Long userId);
}
