package com.teamharmony.newscommunity.users.service;

import com.teamharmony.newscommunity.users.dto.response.ProfileResponseDto;
import com.teamharmony.newscommunity.users.dto.response.UserResponseDto;
import com.teamharmony.newscommunity.users.vo.ProfileVO;
import com.teamharmony.newscommunity.users.entity.*;
import com.teamharmony.newscommunity.users.filesotre.FileStore;
import com.teamharmony.newscommunity.users.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.apache.http.entity.ContentType.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final UserProfileRepository profileRepository;
	private final UserRoleRepository userRoleRepository;
	private final TokensRepository tokensRepository;
	
	private final PasswordEncoder passwordEncoder;
	private final FileStore fileStore;
	
	@Value("${aws.s3.bucket-name}")
	private String bucketName;
	
	/**
	 * 인증을 위한 사용자 조회
	 *
	 * @param 		username 해당 사용자 ID
	 * @return 		사용자 정보를 담은 객체
	 * @see   		UserDetailsService#loadUserByUsername
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		if (user == null) throw new UsernameNotFoundException("User not found");
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		Collection<UserRole> userRole = userRoleRepository.findByUser(user);
		Collection<Role> roles = new ArrayList<>();
		userRole.forEach(r -> roles.add(r.getRole()));
		roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName().toString())));
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
	}
	
	@Override
	public User saveUser(User user) {
		log.info("Saving new user {} to the database", user.getUsername());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}
	
	@Override
	public Role saveRole(Role role) {
		log.info("Saving new role {} to the database", role.getName());
		return roleRepository.save(role);
	}
	
	@Override
	public Tokens updateTokens(String username, String access_token, String refresh_token) {
		Tokens tokens = getTokens(username);
		tokens.update(access_token, refresh_token);
		return tokensRepository.save(tokens);
	}
	
	@Override
	public void defaultProfile(User user) {
		UserProfile profile = UserProfile.builder()
		                                 .nickname(user.getUsername())
		                                 .profile_pic("default")
		                                 .build();
		
		profile.setUser(user);
		profileRepository.save(profile);
	}
	
	@Override
	public Map<String, String> updateProfile(String username, ProfileVO profileVO) {
		MultipartFile file = profileVO.getFile();
		User user = getUser(username);
		UserProfile existingProfile = user.getProfile();	// 해당 유저의 기존 프로필 찾기
		if (existingProfile == null) throw new IllegalArgumentException(String.format("User profile %s not found", username));
		
		if (!file.isEmpty()) {
			isImage(file); // 파일이 이미지인지 확인
			// 버킷에 저장될 경로, 파일명 그리고 파일의 metadata 생성
			String path = String.format("%s/%s", bucketName, username);
			String fileName = String.format("%s", file.getOriginalFilename());
			Map<String, String> metadata = extractMetadata(file);
			
			try {
				if(!existingProfile.getProfile_pic().equals("default")) fileStore.delete(path, existingProfile.getProfile_pic()); // 기존 파일 삭제
				fileStore.save(path, fileName, Optional.of(metadata), file.getInputStream()); // 업데이트 파일 저장
			} catch (IOException e) {
				throw new IllegalArgumentException(e);
			}
			
			// 프로필 변경 사항 적용 후 DB 저장
			existingProfile.update(profileVO);
			profileRepository.save(existingProfile);

		} else {
			// 프로필 사진 외 변경 사항 적용 후 DB 저장
			existingProfile.notUpdatePic(profileVO);
			profileRepository.save(existingProfile);
		}
		Map<String, String> body = new HashMap<>();
		body.put("result", "success");
		body.put("msg", "프로필 변경이 완료되었습니다.");
		return body;
	}
	
	@Override
	public String getProfileImageUrl(String username, UserProfile profile) {
		if (profile == null) throw new IllegalArgumentException(String.format("User profile %s not found", username));
		String path = String.format("%s/%s", bucketName,username);
		// 버킷에서 프로필 사진 가져오기
		if (!profile.getProfile_pic().equals("default")) {
			return fileStore.download(path, profile.getProfile_pic());
		} else {
			return "default";
		}
	}
	
	private Map<String, String> extractMetadata(MultipartFile file) {
		Map<String, String> metadata = new HashMap<>();
		metadata.put("Content-Type", file.getContentType());
		metadata.put("Content-Length", String.valueOf(file.getSize()));
		return metadata;
	}
	
	private void isImage(MultipartFile file) {
		if (!Arrays.asList(IMAGE_JPEG.getMimeType(), IMAGE_PNG.getMimeType(), IMAGE_GIF.getMimeType())
		           .contains(file.getContentType()))
			throw new IllegalArgumentException("File must be an image [ "+ file.getContentType() +" ]");
	}

	@Override
	public void addRoleToUser(String username, RoleType roleName) {
		log.info("Adding role {} to user {}", roleName, username);
		User user = getUser(username);
		Role role = getRole(roleName);
		UserRole userRole = new UserRole(user, role);
		userRoleRepository.save(userRole);
	}
	
	@Override
	public User getUser(String username) {
		log.info("Fetching user {}", username);
		return userRepository.findByUsername(username);
	}
	
	@Override
	public Role getRole(RoleType name) {
		log.info("Fetching role {}", name);
		return roleRepository.findByName(name);
	}
	
	@Override
	public Map<String, Object> getProfile(String username, boolean status) {
		log.info("Fetching profile of user {}", username);
		User user = getUser(username);
		UserProfile profile = user.getProfile();
		ProfileResponseDto profileDto = new ProfileResponseDto(profile);
		Map<String, Object> body = new HashMap<>();
		body.put("status", status);
		body.put("link", getProfileImageUrl(username, profile));
		body.put("profile", profileDto);
		return body;
	}
	
	@Override
	public Collection<Role> getRoles(User user) {
		Collection<UserRole> userRole = userRoleRepository.findByUser(user);
		Collection<Role> roles = new ArrayList<>();
		userRole.forEach(r -> roles.add(r.getRole()));
		return roles;
	}
	
	@Override
	public List<UserResponseDto> getUsers() {
		log.info("Fetching all users");
		List<User> users= userRepository.findAll();
		return users.stream().map(UserResponseDto::toDto).collect(toList());
	}
	
	@Override
	public Tokens getTokens(String username) {
		log.info("Fetching tokens of user {}", username);
		return tokensRepository.findByUsername(username);
	}
	
	@Override
	public Map<String, Boolean> checkUser(String username) {
		log.info("Checking duplicates username {}", username);
		Map<String, Boolean> body = new HashMap<>();
		Boolean exists = getUser(username) != null;
		body.put("exists", exists);
		return body;
	}
}
