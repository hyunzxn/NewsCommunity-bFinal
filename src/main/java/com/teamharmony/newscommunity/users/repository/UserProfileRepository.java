package com.teamharmony.newscommunity.users.repository;

import com.teamharmony.newscommunity.users.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
}
