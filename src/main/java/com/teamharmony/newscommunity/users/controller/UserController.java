package com.teamharmony.newscommunity.users.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamharmony.newscommunity.users.dto.response.UserResponseDto;
import com.teamharmony.newscommunity.users.vo.ProfileVO;
import com.teamharmony.newscommunity.users.dto.request.SignupRequestDto;
import com.teamharmony.newscommunity.users.entity.*;
import com.teamharmony.newscommunity.users.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.SET_COOKIE;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * 사용자 관련 요청을 처리
 *
 * @author yj
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {
	private final UserService userService;
	
	/**
	 * 전체 유저 불러오기
	 *
	 * @return		전체 유저를 담은 List
	 * @see 			UserService#getUsers
	 */
	@GetMapping("/admin/users")
	public ResponseEntity<List<UserResponseDto>>getUsers() {
		return ResponseEntity.ok().body(userService.getUsers());
	}
	
	/**
	 * 회원 가입 요청 처리
	 *
	 * @param 		dto 가입하려는 사용자 ID와 비밀번호를 담은 객체
	 * @see   		UserService#saveUser
	 * @see   		UserService#getRole
	 * @see   		UserService#saveRole
	 * @see   		UserService#addRoleToUser
	 * @see   		UserService#defaultProfile
	 */
	@PostMapping("/signup")
	public ResponseEntity<?>saveUser(SignupRequestDto dto) {
		User user = User.builder().dto(dto).build();
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/signup").toUriString());
		userService.saveUser(user);
		// 기본 사용자 권한 추가
		Role role = new Role(RoleType.USER);
		role = userService.getRole(role.getName());
		try {
			if (role == null) {
				userService.saveRole(new Role(RoleType.USER));
			}
			userService.addRoleToUser(user.getUsername(),RoleType.USER);
			// 기본 프로필 추가
			userService.defaultProfile(user);
			return ResponseEntity.ok().build();
		} catch (DataIntegrityViolationException|ConstraintViolationException e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	/**
	 * 회원 ID 중복 확인
	 *
	 * @param 		username 가입하려는 사용자 ID
	 * @return 		중복 여부
	 * @see				UserService#checkUser
	 */
	@PostMapping("/signup/checkdup")
	public ResponseEntity<?>checkUser(@RequestParam("username") String username) {
		//todo validator
		
		// 회원 ID 입력값 공백 제거
		username = username.replaceAll("\\s", "");
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/signup/checkdup").toUriString());
		return ResponseEntity.created(uri).body(userService.checkUser(username));
	}
	
	/**
	 * 현재 로그인한 유저 ID
	 *
	 * @param 		user 인증된 사용자 정보
	 * @return 		인증된 사용자 ID
	 * @see				UserDetails#getUsername
	 */
	@GetMapping("/user/me")
	public ResponseEntity<String>getUsername(@AuthenticationPrincipal UserDetails user) {
		String username = user.getUsername();
		return ResponseEntity.ok().body(username);
	}
	
	/**
	 * 권한 추가
	 *
	 * @param 		role enum 타입 권한명이 담긴 객체
	 * @return 		인증된 사용자 ID
	 * @see				UserService#saveRole
	 */
	@PostMapping("/role/save")
	public ResponseEntity<Role>saveUser(@RequestBody Role role) {
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
		return ResponseEntity.created(uri).body(userService.saveRole(role));
	}
	
	/**
	 * 사용자 정보에 권한 추가
	 *
	 * @param 		form 사용자 ID와 권한명이 담긴 객체
	 * @see				UserService#addRoleToUser
	 */
	@PostMapping("/role/addtouser")
	public ResponseEntity<?>addRoleToUser(@RequestBody RoleToUserForm form) {
		userService.addRoleToUser(form.getUsername(), form.getRoleName());
		return ResponseEntity.ok().build();
	}
	
	/**
	 * 회원 프로필 정보
	 *
	 * @param 		username 프로필 회원 ID
	 * @param 		user 인증된 사용자 정보
	 * @return 		인증된 사용자 ID와 일치 여부, 프로필 사진 url, 프로필 정보
	 * @see				UserService#getProfile
	 */
	@GetMapping("/user/profile/{username}")
	public ResponseEntity<Map<String, Object>>getProfile(@PathVariable String username, @AuthenticationPrincipal UserDetails user) {
		boolean status = username.equals(user.getUsername());
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/profile/{username}").toUriString());
		return ResponseEntity.created(uri).body(userService.getProfile(username, status));
	}
	
	/**
	 * 회원 프로필 업데이트
	 *
	 * @param 		profile 닉네임, 프로필 사진 파일, 소개글을 담은 객체
	 * @param 		user 인증된 사용자 정보
	 * @return 		성공 확인, 메시지
	 * @see				UserService#updateProfile
	 */
	@PostMapping(
			path = "/user/update_profile",
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<?>updateProfile(ProfileVO profile, @AuthenticationPrincipal UserDetails user) {
		String username = user.getUsername();
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/update_profile").toUriString());
		return ResponseEntity.created(uri).body(userService.updateProfile(username, profile));
	}
	
	/**
	 * 접근 토큰, 갱신 토큰 갱신
	 *
	 * @see				UserService#getTokens
	 * @see				UserService#getUser
	 * @see				UserService#getRoles
	 * @see				UserService#updateTokens
	 */
	@GetMapping("/token/refresh")
	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
				String allowedToken = userService.getTokens(username).getRefreshToken();
				if(!allowedToken.equals(refCookie))throw new IllegalArgumentException("Token not found");
				// Once we get the username, we need to load that user
				// use the user(loaded using the username) to create a new token( using the refresh token)
				User user = userService.getUser(username);
				String access_token = JWT.create()
				                         .withSubject(user.getUsername())
				                         .withExpiresAt(new Date(System.currentTimeMillis() + 16*60*60*1000)) // 테스트를 위해 16시간으로 설정
				                         .withClaim("roles", userService.getRoles(user).stream().map(Role::getName).map(Enum::toString).collect(Collectors.toList()))
				                         .sign(algorithm);
				String refresh_token = JWT.create()
				                          .withSubject(user.getUsername())
				                          .withExpiresAt(new Date(System.currentTimeMillis() + 7*24*60*60*1000))
				                          .sign(algorithm);
				userService.updateTokens(username, access_token, refresh_token);
				
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
				new ObjectMapper().writeValue(response.getOutputStream(), "success");
			} catch (Exception e) {
				removeRefCookie(response);
				setError(response, e.getMessage());
			}
		} else {
			removeRefCookie(response);
			setError(response, "Refresh token is missing");
		}
	}
	
	/**
	 * 로그아웃
	 *
	 * @param 		user 인증된 사용자 정보
	 * @see				UserService#updateTokens
	 */
	@GetMapping("/user/signout")
	public void signOut(HttpServletRequest request, HttpServletResponse response, @AuthenticationPrincipal UserDetails user) throws IOException {
		// 클라이언트가 쿠키에 리프레쉬 토큰을 갖고 있는지 확인
		String refCookie = getRefCookie(request);
		// 쿠키가 있으면 삭제
		if (refCookie != null) {
			removeRefCookie(response);
		}
		// DB에 저장된 토큰 값 공백 처리
		userService.updateTokens(user.getUsername(), "", "");
	}
	
	// 리프레쉬 쿠키값 가져오기
	private String getRefCookie(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		Cookie cookie = null;
		if (cookies != null && cookies.length > 0) {
			for (Cookie value : cookies) {
				if (value.getName()
				         .equals("ref_uid")) {
					cookie = value;
				}
			}
		}
		return cookie.getValue();
	}
	
	// 리프레쉬 쿠키 삭제(만료시간 0으로 설정)
	private void removeRefCookie(HttpServletResponse response) {
		ResponseCookie refresh = ResponseCookie.from("ref_uid", "")
		                                       .maxAge(0)
		                                       .path("/")
		                                       .build();
		response.setHeader(SET_COOKIE, refresh.toString());
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
}

@Data
class RoleToUserForm{
	private String username;
	private RoleType roleName;
}

