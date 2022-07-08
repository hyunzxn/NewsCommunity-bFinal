package com.teamharmony.newscommunity.users.repo;

import com.teamharmony.newscommunity.users.entity.User;
import com.teamharmony.newscommunity.users.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
	UserProfile findByUser_id(Long user_id);
}
