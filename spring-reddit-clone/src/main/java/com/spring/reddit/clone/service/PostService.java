package com.spring.reddit.clone.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.reddit.clone.domain.Post;
import com.spring.reddit.clone.domain.Subreddit;
import com.spring.reddit.clone.domain.User;
import com.spring.reddit.clone.dto.PostRequest;
import com.spring.reddit.clone.dto.PostResponse;
import com.spring.reddit.clone.exception.PostNotFoundException;
import com.spring.reddit.clone.exception.SubredditNotFoundException;
import com.spring.reddit.clone.mapper.PostMapper;
import com.spring.reddit.clone.repository.PostRepository;
import com.spring.reddit.clone.repository.SubredditRepository;
import com.spring.reddit.clone.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class PostService {

	private final PostRepository postRepository;
	private final SubredditRepository subredditRepository;
	private final UserRepository userRepository;
	private final AuthService authService;
	private final PostMapper postMapper;

	@Transactional
	public void save(PostRequest postRequest) {
		Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
				.orElseThrow(() -> new SubredditNotFoundException(postRequest.getSubredditName()));
		postRepository.save(postMapper.map(postRequest, subreddit, authService.getCurrentUser()));
	}

	@Transactional(readOnly = true)
	public PostResponse getPost(Long id) {
		Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id.toString()));
		return postMapper.mapToDto(post);
	}

	@Transactional(readOnly = true)
	public List<PostResponse> getAllPosts() {
		return postRepository.findAll().stream().map(postMapper::mapToDto).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<PostResponse> getPostsBySubreddit(Long subredditId) {
		Subreddit subreddit = subredditRepository.findById(subredditId)
				.orElseThrow(() -> new SubredditNotFoundException(subredditId.toString()));
		List<Post> posts = postRepository.findAllBySubreddit(subreddit);
		return posts.stream().map(postMapper::mapToDto).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<PostResponse> getPostsByUsername(String username) {
		User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
		return postRepository.findByUser(user).stream().map(postMapper::mapToDto).collect(Collectors.toList());
	}
}