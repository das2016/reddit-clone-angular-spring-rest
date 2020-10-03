package com.spring.reddit.clone.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.reddit.clone.dto.AuthenticationResponse;
import com.spring.reddit.clone.dto.LoginRequest;
import com.spring.reddit.clone.dto.RegisterRequest;
import com.spring.reddit.clone.service.AuthService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {

	private final AuthService authService;

	@PostMapping("signup")
	public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) {
		log.info(" register request is : "+registerRequest.toString());
		authService.signup(registerRequest);
		return new ResponseEntity<>("User registration  success", HttpStatus.OK);
	}
	
	@GetMapping("accountVerification/{token}")
	public ResponseEntity<String> verifyAccount(@PathVariable String token){
		authService.verifyAccount(token);
		return new ResponseEntity<String>("Account activated successfully", HttpStatus.OK);
	}
	
	@PostMapping("login")
	public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
		return authService.login(loginRequest);
	}
}
