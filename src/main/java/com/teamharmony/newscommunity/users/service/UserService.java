package com.teamharmony.newscommunity.users.service;

import com.teamharmony.newscommunity.users.dto.response.UserResponseDto;
import com.teamharmony.newscommunity.users.vo.ProfileVO;
import com.teamharmony.newscommunity.users.entity.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface UserService {
	
	/**
	 * 저장소에 사용자 저장
	 *
	 * @param 		user 저장할 사용자 정보
	 * @return 		저장된 사용자 정보
	 * @see				UserServiceImpl#saveUser
	 */
	User saveUser(User user);
	
	/**
	 * 저장소에 권한 저장
	 *
	 * @param 		role 저장할 권한 정보
	 * @return 		저장된 권한 정보
	 * @see				UserServiceImpl#saveRole
	 */
	Role saveRole(Role role);
	
	/**
	 * 토큰 값 변경
	 *
	 * @param 		username 해당 사용자 ID
	 * @param 		access_token 허용된 접근 토큰 값
	 * @param 		refresh_token 허용된 갱신 토큰 값
	 * @return 		저장된 토큰 정보
	 * @see				UserServiceImpl#updateTokens
	 */
	Tokens updateTokens(String username, String access_token, String refresh_token);
	
	/**
	 * 회원 가입시 기본 프로필 적용
	 *
	 * @param 		user 기본 프로필을 적용할 사용자 정보
	 * @see				UserServiceImpl#defaultProfile
	 */
	void defaultProfile(User user);
	
	/**
	 * 프로필 정보 변경
	 *
	 * @param 		username 프로필을 변경할 사용자 ID
	 * @param 		profile 변경할 프로필 정보
	 * @return 		성공 확인, 메시지
	 * @see				UserServiceImpl#updateProfile
	 */
	Map<String, String> updateProfile(String username, ProfileVO profile);
	
	/**
	 * 버킷에 저장된 프로필 사진 조회
	 *
	 * @param 		username 프로필 정보를 가져올 회원 ID
	 * @param 		profile 프로필 정보
	 * @return 		프로필 사진 URL
	 * @see				UserServiceImpl#getProfileImageUrl
	 */
	String getProfileImageUrl(String username, UserProfile profile);
	
	/**
	 * 프로필 정보 조회
	 *
	 * @param 		username 프로필 정보를 가져올 회원 ID
	 * @param 		status 인증된 사용자 ID와 일치 여부
	 * @return 		인증된 사용자 ID와 일치 여부, 프로필 사진 url, 프로필 정보
	 * @see				UserServiceImpl#getProfile
	 */
	Map<String, Object> getProfile(String username, boolean status);
	
	/**
	 * 사용자에 권한 부여
	 *
	 * @param 		username 권한을 추가할 사용자 ID
	 * @param 		roleName 추가할 권한명
	 * @see				UserServiceImpl#addRoleToUser
	 */
	void addRoleToUser(String username, RoleType roleName);
	
	/**
	 * 사용자 정보 조회
	 *
	 * @param 		username 조회할 사용자 ID
	 * @return 		사용자 정보
	 * @see				UserServiceImpl#getUser
	 */
	User getUser(String username);
	
	/**
	 * 권한 정보 조회
	 *
	 * @param 		name 조회할 권한명
	 * @return 		권한 정보
	 * @see				UserServiceImpl#getRole
	 */
	Role getRole(RoleType name);
	
	/**
	 * 사용자가 지닌 권한 정보 조회
	 *
	 * @param 		user 조회할 사용자 ID
	 * @return 		사용자가 지닌 권한 정보
	 * @see				UserServiceImpl#getRoles
	 */
	Collection<Role> getRoles(User user);
	
	/**
	 * 전체 사용자 조회
	 *
	 * @return 		전체 사용자 정보
	 * @see				UserServiceImpl#getUsers
	 */
	List<UserResponseDto> getUsers();
	
	/**
	 * 사용자의 허용 토큰 정보 조회
	 *
	 * @param 		username 조회할 사용자 ID
	 * @return 		사용자의 허용 토큰 정보
	 * @see				UserServiceImpl#getTokens
	 */
	Tokens getTokens(String username);
	
	/**
	 * 사용자 ID 중복 여부
	 *
	 * @param 		username 중복 확인할 사용자 ID
	 * @return 		사용자 ID 중복 여부
	 * @see				UserServiceImpl#checkUser
	 */
	Map<String, Boolean> checkUser(String username);
}
