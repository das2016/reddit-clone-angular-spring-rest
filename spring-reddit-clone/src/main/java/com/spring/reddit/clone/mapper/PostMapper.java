package com.spring.reddit.clone.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.spring.reddit.clone.domain.Post;
import com.spring.reddit.clone.domain.Subreddit;
import com.spring.reddit.clone.domain.User;
import com.spring.reddit.clone.dto.PostRequest;
import com.spring.reddit.clone.dto.PostResponse;

@Mapper(componentModel = "spring")
public interface PostMapper {

	    @Mapping(target = "description", source = "postRequest.description")
	    @Mapping(target = "subreddit", source = "subreddit")
//	    @Mapping(target = "voteCount", constant = "0")
	    @Mapping(target = "user", source = "user")
	    Post map(PostRequest postRequest, Subreddit subreddit, User user);

	    
//	    @Mapping(target = "commentCount", expression = "java(commentCount(post))")
//	    @Mapping(target = "duration", expression = "java(getDuration(post))")
//	    @Mapping(target = "upVote", expression = "java(isPostUpVoted(post))")
//	    @Mapping(target = "downVote", expression = "java(isPostDownVoted(post))")
	    @Mapping(target = "id", source = "postId")
	    @Mapping(target = "subredditName", source = "subreddit.name")
	    @Mapping(target = "userName", source = "user.username")
	    PostResponse mapToDto(Post post);

//	    Integer commentCount(Post post) {
//	        return commentRepository.findByPost(post).size();
//	    }
//
//	    String getDuration(Post post) {
//	        return TimeAgo.using(post.getCreatedDate().toEpochMilli());
//	    }
//
//	    boolean isPostUpVoted(Post post) {
//	        return checkVoteType(post, UPVOTE);
//	    }
//
//	    boolean isPostDownVoted(Post post) {
//	        return checkVoteType(post, DOWNVOTE);
//	    }

//	    private boolean checkVoteType(Post post, VoteType voteType) {
//	        if (authService.isLoggedIn()) {
//	            Optional<Vote> voteForPostByUser =
//	                    voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post,
//	                            authService.getCurrentUser());
//	            return voteForPostByUser.filter(vote -> vote.getVoteType().equals(voteType))
//	                    .isPresent();
//	        }
//	        return false;
//	    }
//
//	}
}
