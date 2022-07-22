package com.teamharmony.newscommunity.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamharmony.newscommunity.auth.entity.Tokens;
import com.teamharmony.newscommunity.auth.repository.TokensRepository;
import com.teamharmony.newscommunity.exception.TokenException;
import com.teamharmony.newscommunity.users.entity.Role;
import com.teamharmony.newscommunity.users.entity.User;
import com.teamharmony.newscommunity.users.entity.UserRole;
import com.teamharmony.newscommunity.users.repository.UserRepository;
import com.teamharmony.newscommunity.users.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static com.teamharmony.newscommunity.auth.util.CookieUtil.getRefCookie;
import static com.teamharmony.newscommunity.auth.util.CookieUtil.removeRefCookie;
import static org.springframework.http.HttpHeaders.SET_COOKIE;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
	private final TokensRepository tokensRepository;
	private final UserRepository userRepository;
	private final UserRoleRepository userRoleRepository;
	
	public String refreshToken(HttpServletRequest request, HttpServletResponse response) throws TokenException {
		
		try {
			// 클라이언트가 쿠키에 리프레쉬 토큰을 갖고 있는지 확인
			String refCookie = getRefCookie(request);
			
			// 토큰이 있으면 풀어서 DB에 있는지 확인
			Algorithm algorithm = Algorithm.HMAC256("secretKey".getBytes());
			JWTVerifier verifier = JWT.require(algorithm)
			                          .build();
			DecodedJWT decodedJWT = verifier.verify(refCookie);
			String username = decodedJWT.getSubject();
			Tokens tokens = getTokens(username);
			String allowedToken = tokens.getRefreshToken();
			if (!allowedToken.equals(refCookie)) throw TokenException.builder().message("허용된 갱신 토큰이 아닙니다.").invalidValue("갱신 토큰: " + refCookie).code("A404").build();
			
			// 해당 정보로 토큰 재발급, DB 저장
			User user = userRepository.findByUsername(username);
			if (user == null) throw TokenException.builder().message("토큰 정보에 해당하는 사용자를 찾을 수 없습니다.").invalidValue("사용자 ID: "+username).code("A406").build();
			Collection<UserRole> userRole = userRoleRepository.findByUser(user);
			Collection<Role> roles = new ArrayList<>();
			userRole.forEach(r -> roles.add(r.getRole()));
			String access_token = JWT.create()
			                         .withSubject(user.getUsername())
			                         .withExpiresAt(new Date(System.currentTimeMillis() + 60*60*1000))
			                         .withClaim("roles", roles.stream().map(Role::getName).map(Enum::toString).collect(Collectors.toList()))
			                         .sign(algorithm);
			String refresh_token = JWT.create()
			                          .withSubject(user.getUsername())
			                          .withExpiresAt(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000))
			                          .sign(algorithm);
			updateTokens(username, access_token, refresh_token);
			
			ResponseCookie refresh = ResponseCookie.from("ref_uid", refresh_token)
			                                       .maxAge(7 * 24 * 60 * 60)
			                                       .httpOnly(true)
			                                       .secure(true)
			                                       .sameSite("None")
			                                       .path("/")
			                                       .build();
			response.setHeader(SET_COOKIE, refresh.toString());
			response.setHeader("token", access_token);
			byte[] usernameHeader = username.getBytes(StandardCharsets.UTF_8);
			response.setHeader("username", Base64.getEncoder().encodeToString(usernameHeader));
			response.setContentType(APPLICATION_JSON_VALUE);
		} catch (TokenExpiredException e) {
			throw TokenException.builder().message("갱신 토큰이 만료되었습니다.").cause(e.getCause()).code("A402").build();
		} catch (JWTVerificationException e) {
			throw TokenException.builder().message("올바른 토큰이 아닙니다.").cause(e.getCause()).code("A403").build();
		} catch (TokenException e) {
			throw TokenException.builder().message(e.getMessage()).cause(e.getCause()).code(e.getCode()).build();
		} catch (Exception e) {
			throw TokenException.builder().message(e.getMessage()).cause(e.getCause()).code("A407").build();
		}
		return "success";
	}
	
	// 에러 발생시 헤더와 output 설정
	private void setError(HttpServletResponse response, String msg) throws IOException {
		response.setHeader("error", msg);
		response.setStatus(FORBIDDEN.value());
		Map<String, String> error = new HashMap<>();
		error.put("error", msg);
		response.setContentType(APPLICATION_JSON_VALUE);
		new ObjectMapper().writeValue(response.getOutputStream(), error);
	}
	
	/**
	 * 로그아웃
	 *
	 * @param 		username 인증된 사용자 ID
	 */
	public String signOut(HttpServletRequest request, HttpServletResponse response, String username) throws TokenException {
		// DB에 저장된 허용 토큰 공백 처리, 쿠키 삭제
		updateTokens(username, "", "");
		response.setHeader(SET_COOKIE, removeRefCookie());
		return "success";
	}
	
	/**
	 * 사용자의 허용 토큰 정보 조회
	 *
	 * @param 		username 조회할 사용자 ID
	 * @return 		사용자의 허용 토큰 정보
	 */
	private Tokens getTokens(String username) throws TokenException {
		log.info("Fetching tokens of user {}", username);
		Tokens tokens = tokensRepository.findByUsername(username);
		if (tokens == null) throw TokenException.builder().message("허용 토큰 정보를 찾을 수 없습니다.").invalidValue("사용자 ID: " + username).code("A404").build();
		return tokens;
	}
	
	/**
	 * 토큰 값 변경
	 *
	 * @param username      해당 사용자 ID
	 * @param access_token  허용된 접근 토큰 값
	 * @param refresh_token 허용된 갱신 토큰 값
	 */
	private void updateTokens(String username, String access_token, String refresh_token) throws TokenException {
		Tokens tokens = getTokens(username);
		tokens.update(access_token, refresh_token);
		tokensRepository.save(tokens);
	}
}
