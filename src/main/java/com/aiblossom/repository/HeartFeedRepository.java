package com.aiblossom.repository;

import com.aiblossom.entity.HeartFeed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeartFeedRepository extends JpaRepository<HeartFeed, Long> {
}
