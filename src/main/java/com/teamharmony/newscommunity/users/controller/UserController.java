package com.teamharmony.newscommunity.users.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamharmony.newscommunity.users.dto.ProfileVO;
import com.teamharmony.newscommunity.users.dto.SignupDto;
import com.teamharmony.newscommunity.users.entity.*;
import com.teamharmony.newscommunity.users.filter.CustomAuthorizationFilter;
import com.teamharmony.newscommunity.users.repo.TokensRepository;
import com.teamharmony.newscommunity.users.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {
	private final UserService userService;
	
	// 전체 유저 불러오기
	@GetMapping("/admin/users")
	public ResponseEntity<List<User>>getUsers() {
		return ResponseEntity.ok().body(userService.getUsers());
	}
	
	// 회원 가입 요청 처리
	@PostMapping("/signup")
	public ResponseEntity<User>saveUser(SignupDto dto) {
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
			UserProfile profile = UserProfile.builder()
			                                 .nickname(user.getUsername())
			                                 .profile_pic("default")
			                                 .build();
			userService.defaultProfile(user, profile);
			return ResponseEntity.created(uri).body(user);
		} catch (DataIntegrityViolationException|ConstraintViolationException e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	// 회원 ID 중복 확인
	@PostMapping("/signup/checkdup")
	public ResponseEntity<?>checkUser(@RequestParam("username_give") String username) {
		//todo validator
		
		// 회원 ID 입력값 공백 제거
		username = username.replaceAll("\\s", "");
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/signup/checkdup").toUriString());
		return ResponseEntity.created(uri).body(userService.checkUser(username));
	}
	
	// 현재 로그인한 유저 ID
	@GetMapping("/user/me")
	public ResponseEntity<String>getUsername(@AuthenticationPrincipal UserDetails user) {
		String username = user.getUsername();
		return ResponseEntity.ok().body(username);
	}
	
	// 권한 추가
	@PostMapping("/role/save")
	public ResponseEntity<Role>saveUser(@RequestBody Role role) {
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
		return ResponseEntity.created(uri).body(userService.saveRole(role));
	}
	
	// 회원 권한 추가
	@PostMapping("/role/addtouser")
	public ResponseEntity<?>addRoleToUser(@RequestBody RoleToUserForm form) {
		userService.addRoleToUser(form.getUsername(), form.getRoleName());
		return ResponseEntity.ok().build();
	}
	
	// 회원 프로필 정보
	@GetMapping("/user/profile/{username}")
	public ResponseEntity<Map<String, Object>>getProfile(@PathVariable String username, @AuthenticationPrincipal UserDetails user) {
		boolean status = username.equals(user.getUsername());
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/profile/{username}").toUriString());
		return ResponseEntity.created(uri).body(userService.getProfile(username, status));
	}
	
	// 회원 프로필 업데이트
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
	
	// 토큰 리프레쉬
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
	
	private void removeRefCookie(HttpServletResponse response) {
		ResponseCookie refresh = ResponseCookie.from("ref_uid", "")
		                                       .maxAge(0)
		                                       .path("/")
		                                       .build();
		response.setHeader(SET_COOKIE, refresh.toString());
	}
	
	@GetMapping("/token/signout")
	public void signOut(HttpServletRequest request, HttpServletResponse response, @AuthenticationPrincipal UserDetails user) throws IOException {
		// 클라이언트가 쿠키에 리프레쉬 토큰을 갖고 있는지 확인
		String refCookie = getRefCookie(request);
		// 쿠키가 있으면 삭제 후(만료시간 0으로 설정)
		if (refCookie != null) {
			removeRefCookie(response);
		}
		// DB에 저장된 토큰 값 공백 처리
		userService.updateTokens(user.getUsername(), "", "");
	}
	
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

