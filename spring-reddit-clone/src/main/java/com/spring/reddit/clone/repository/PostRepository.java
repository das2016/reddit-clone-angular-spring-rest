package com.spring.reddit.clone.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.reddit.clone.domain.Post;
import com.spring.reddit.clone.domain.Subreddit;
import com.spring.reddit.clone.domain.User;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

	List<Post> findAllBySubreddit(Subreddit subreddit);

	List<Post> findByUser(User user);
}
