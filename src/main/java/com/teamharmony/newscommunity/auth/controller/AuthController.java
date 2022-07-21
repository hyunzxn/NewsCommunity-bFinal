package com.teamharmony.newscommunity.auth.controller;

import com.teamharmony.newscommunity.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AuthController {
	private final AuthService authService;
	
	/**
	 * 접근 토큰, 갱신 토큰 갱신
	 *
	 * @see				AuthService#refreshToken
	 */
	@GetMapping("/token/refresh")
	public ResponseEntity<String>refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/token/refresh").toUriString());
		return ResponseEntity.ok().body(authService.refreshToken(request, response));
	}
	
	/**
	 * 로그아웃
	 *
	 * @param 		user 인증된 사용자 정보
	 * @see				AuthService#signOut
	 */
	@GetMapping("/user/signout")
	public ResponseEntity<String> signOut(HttpServletRequest request, HttpServletResponse response, @AuthenticationPrincipal UserDetails user) {
		return ResponseEntity.ok().body(authService.signOut(request, response, user.getUsername()));
	}
}
