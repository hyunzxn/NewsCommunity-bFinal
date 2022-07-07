package com.teamharmony.newscommunity.users.service;

import com.teamharmony.newscommunity.users.dto.ProfileVO;
import com.teamharmony.newscommunity.users.entity.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface UserService {
	User saveUser(User user);
	Role saveRole(Role role);
	void defaultProfile(User user, UserProfile profile);
	Map<String, String> updateProfile(String username, ProfileVO profile);
	String getProfileImageUrl(String username, UserProfile profile);
	Map<String, Object> getProfile(String username, boolean status);
	void addRoleToUser(String username, RoleType roleName);
	User getUser(String username);
	Role getRole(RoleType name);
	Collection<Role> getRoles(User user);
	List<User> getUsers();
	Map<String, Boolean> checkUser(String username);
}
