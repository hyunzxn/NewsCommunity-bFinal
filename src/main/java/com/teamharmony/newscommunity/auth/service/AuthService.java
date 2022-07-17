package com.teamharmony.newscommunity.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamharmony.newscommunity.auth.entity.Tokens;
import com.teamharmony.newscommunity.auth.repository.TokensRepository;
import com.teamharmony.newscommunity.users.entity.Role;
import com.teamharmony.newscommunity.users.entity.User;
import com.teamharmony.newscommunity.users.entity.UserRole;
import com.teamharmony.newscommunity.users.repository.UserRepository;
import com.teamharmony.newscommunity.users.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
	
	
	public String refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 클라이언트가 쿠키에 리프레쉬 토큰을 갖고 있는지 확인
		String refCookie = getRefCookie(request);
		
		// 쿠키가 있으면 DB에 있는지 재확인하고 새로운 쿠키를 DB 저장 후 발급
		if (refCookie != null) {
			try {
				Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
				JWTVerifier verifier = JWT.require(algorithm)
				                          .build();
				DecodedJWT decodedJWT = verifier.verify(refCookie);
				String username = decodedJWT.getSubject();
				String allowedToken = getTokens(username).getRefreshToken();
				if(!allowedToken.equals(refCookie))throw new IllegalArgumentException("Token not found");
				// Once we get the username, we need to load that user
				// use the user(loaded using the username) to create a new token( using the refresh token)
				User user = userRepository.findByUsername(username);
				Collection<UserRole> userRole = userRoleRepository.findByUser(user);
				Collection<Role> roles = new ArrayList<>();
				userRole.forEach(r -> roles.add(r.getRole()));
				String access_token = JWT.create()
				                         .withSubject(user.getUsername())
				                         .withExpiresAt(new Date(System.currentTimeMillis() + 16*60*60*1000)) // 테스트를 위해 16시간으로 설정
				                         .withClaim("roles", roles.stream().map(Role::getName).map(Enum::toString).collect(Collectors.toList()))
				                         .sign(algorithm);
				String refresh_token = JWT.create()
				                          .withSubject(user.getUsername())
				                          .withExpiresAt(new Date(System.currentTimeMillis() + 7*24*60*60*1000))
				                          .sign(algorithm);
				updateTokens(username, access_token, refresh_token);
				
				ResponseCookie refresh = ResponseCookie.from("ref_uid", refresh_token)
				                                       .maxAge(7*24*60*60*1000) // 밀리세컨인지 확인해야됨
				                                       .httpOnly(true)
				                                       .secure(true)
				                                       .sameSite("None")
				                                       .path("/")
				                                       .build();
				response.setHeader(SET_COOKIE, refresh.toString());
				response.setHeader("token", access_token);
				response.setContentType(APPLICATION_JSON_VALUE);
			} catch (Exception e) {
				removeRefCookie(response);
				setError(response, e.getMessage());
			}
		} else {
			removeRefCookie(response);
			setError(response, "Refresh token is missing");
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
	public void signOut(HttpServletRequest request, HttpServletResponse response, String username) {
		// 클라이언트가 쿠키에 리프레쉬 토큰을 갖고 있는지 확인
		String refCookie = getRefCookie(request);
		// 쿠키가 있으면 삭제
		if (refCookie != null) {
			removeRefCookie(response);
		}
		// DB에 저장된 토큰 값 공백 처리
		updateTokens(username, "", "");
	}
	
	/**
	 * 사용자의 허용 토큰 정보 조회
	 *
	 * @param 		username 조회할 사용자 ID
	 * @return 		사용자의 허용 토큰 정보
	 */
	private Tokens getTokens(String username) {
		log.info("Fetching tokens of user {}", username);
		return tokensRepository.findByUsername(username);
	}
	
	/**
	 * 토큰 값 변경
	 *
	 * @param username      해당 사용자 ID
	 * @param access_token  허용된 접근 토큰 값
	 * @param refresh_token 허용된 갱신 토큰 값
	 */
	private void updateTokens(String username, String access_token, String refresh_token) {
		Tokens tokens = getTokens(username);
		tokens.update(access_token, refresh_token);
		tokensRepository.save(tokens);
	}
}
