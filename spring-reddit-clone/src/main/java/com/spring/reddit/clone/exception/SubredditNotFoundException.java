package com.spring.reddit.clone.exception;

public class SubredditNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1669282394641057356L;

	public SubredditNotFoundException(String message) {
		super(message);
	}
}
