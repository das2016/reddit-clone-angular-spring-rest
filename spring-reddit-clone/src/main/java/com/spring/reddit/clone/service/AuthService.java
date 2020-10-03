package com.spring.reddit.clone.service;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.reddit.clone.domain.User;
import com.spring.reddit.clone.domain.VerificationToken;
import com.spring.reddit.clone.dto.AuthenticationResponse;
import com.spring.reddit.clone.dto.LoginRequest;
import com.spring.reddit.clone.dto.RefreshTokenRequest;
import com.spring.reddit.clone.dto.RegisterRequest;
import com.spring.reddit.clone.exception.SpringRedditException;
import com.spring.reddit.clone.repository.UserRepository;
import com.spring.reddit.clone.repository.VerificationTokenRepository;
import com.spring.reddit.clone.service.security.JwtProvider;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class AuthService {

	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final VerificationTokenRepository verificationTokenRepository;
	private final MailService mailService;
	private final AuthenticationManager authenticationManager;
	private final JwtProvider jwtProvider;
	private final RefreshTokenService refreshTokenService;

	public void signup(RegisterRequest registerRequest) {
		User user = new User();
		user.setUsername(registerRequest.getUsername());
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		user.setEmail(registerRequest.getEmail());
		user.setCreated(Instant.now());
		user.setEnabled(true);

		userRepository.save(user);

		String token = generateVerificationToken(user);
//		mailService.sendMail(new NotificationEmail("Please Activate Your Account", user.getEmail(),
//				" Thank you for signing up to spring reddit, "
//						+ "please click on the below link to activate your account : "
//						+ "http://localhost:8080/api/auth/accountVerification/" + token));
	}

	private String generateVerificationToken(User user) {
		String verificationToken = UUID.randomUUID().toString();
		VerificationToken token = new VerificationToken();
		token.setToken(verificationToken);
		token.setUser(user);
		verificationTokenRepository.save(token);
		return verificationToken;
	}

	public void verifyAccount(String token) {
		Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
		verificationToken.orElseThrow(() -> new SpringRedditException("Invalid Token"));
		fecthUserAndEnable(verificationToken.get());

	}

	private void fecthUserAndEnable(VerificationToken verificationToken) {
		// TODO Auto-generated method stub
		String username = verificationToken.getUser().getUsername();
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new SpringRedditException(" user not found with name " + username));
		user.setEnabled(true);
		userRepository.save(user);
	}

	@Transactional(readOnly = true)
	public User getCurrentUser() {
		org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		return userRepository.findByUsername(principal.getUsername())
				.orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
	}

	
	 public AuthenticationResponse login(LoginRequest loginRequest) {
		 try {
	        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
	                loginRequest.getPassword()));
	        SecurityContextHolder.getContext().setAuthentication(authenticate);
	        String token = jwtProvider.generateToken(authenticate);
	        return AuthenticationResponse.builder()
	                .authenticationToken(token)
	                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
	                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
	                .username(loginRequest.getUsername())
	                .build();
		 } catch (Throwable e) {
				return null;
			}
	    }

	    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
	        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
	        String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());
	        return AuthenticationResponse.builder()
	                .authenticationToken(token)
	                .refreshToken(refreshTokenRequest.getRefreshToken())
	                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
	                .username(refreshTokenRequest.getUsername())
	                .build();
	    }

	    public boolean isLoggedIn() {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
	    }
}
