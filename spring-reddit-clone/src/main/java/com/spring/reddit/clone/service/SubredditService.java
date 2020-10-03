package com.spring.reddit.clone.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.reddit.clone.domain.Subreddit;
import com.spring.reddit.clone.dto.SubredditDto;
import com.spring.reddit.clone.exception.SpringRedditException;
import com.spring.reddit.clone.mapper.SubredditMapper;
import com.spring.reddit.clone.repository.SubredditRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {

	private final SubredditRepository subredditRepository;
	private final SubredditMapper subredditMapper;

	@Transactional
	public SubredditDto save(SubredditDto subredditDto) {
		Subreddit subreddit = subredditRepository.save(subredditMapper.dtoToModel(subredditDto));
		subredditDto.setId(subreddit.getId());
		return subredditDto;
	}

	@Transactional(readOnly = true)
	public List<SubredditDto> getAll() {
		return subredditRepository.findAll().stream().map(subredditMapper::modelToDto).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public SubredditDto getSubreddit(Long id) {
		Subreddit subreddit = subredditRepository.findById(id)
				.orElseThrow(() -> new SpringRedditException("No Subreddit Foundfor id : " + id));
		return subredditMapper.modelToDto(subreddit);

	}

}
