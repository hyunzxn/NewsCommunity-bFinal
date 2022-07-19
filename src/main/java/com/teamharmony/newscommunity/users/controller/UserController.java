package com.teamharmony.newscommunity.users.controller;

import com.teamharmony.newscommunity.users.dto.UserResponseDto;
import com.teamharmony.newscommunity.users.service.UserService;
import com.teamharmony.newscommunity.users.vo.ProfileVO;
import com.teamharmony.newscommunity.users.dto.SignupRequestDto;
import com.teamharmony.newscommunity.users.entity.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.HibernateException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.net.URI;
import java.util.List;
import java.util.Map;

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
	 * @see   		UserService#signUp
	 */
	@PostMapping("/signup")
	public ResponseEntity<?>signUp(@Valid SignupRequestDto dto) {
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/signup").toUriString());
		try {
			userService.signUp(dto);
			return ResponseEntity.created(uri).build();
		} catch (HibernateException e) {
			return ResponseEntity.internalServerError().build();
		} catch (IllegalArgumentException e) {
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
	public ResponseEntity<?>checkUser(@NotBlank @RequestParam String username) {
		return ResponseEntity.ok().body(userService.checkUser(username));
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
	 * @see				UserService#saveRole
	 */
	@PostMapping("/admin/role/save")
	public ResponseEntity<Void>saveUser(@RequestBody Role role) {
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
		userService.saveRole(role);
		return ResponseEntity.created(uri).build();
	}
	
	/**
	 * 사용자 정보에 권한 추가
	 *
	 * @param 		form 사용자 ID와 권한명이 담긴 객체
	 * @see				UserService#addRoleToUser
	 */
	@PostMapping("/admin/role/addtouser")
	public ResponseEntity<Void>addRoleToUser(@RequestBody RoleToUserForm form) {
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/admin/role/addtouser").toUriString());
		userService.addRoleToUser(form.getUsername(), form.getRoleName());
		return ResponseEntity.created(uri).build();
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
	 * 회원 프로필 사진 URL
	 *
	 * @param 		username 프로필 회원 ID
	 * @return 		프로필 사진 url
	 * @see				UserService#getProfileImageUrl
	 */
	@GetMapping("/user/profile/pic/{username}")
	public ResponseEntity<String>getProfilePicUrl(@PathVariable String username) {
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/profile/pic/{username}").toUriString());
		return ResponseEntity.created(uri).body(userService.getProfileImageUrl(username));
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
	
	@Data
	class RoleToUserForm{
		private String username;
		private RoleType roleName;
	}
}