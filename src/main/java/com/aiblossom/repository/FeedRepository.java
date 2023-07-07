package com.aiblossom.repository;

import com.aiblossom.entity.Feed;
import com.aiblossom.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedRepository extends JpaRepository<Feed, Long> {
    List<Feed> findAllByOrderByCreatedAtDesc();

    List<Feed> findAllByUser(User user);

}
