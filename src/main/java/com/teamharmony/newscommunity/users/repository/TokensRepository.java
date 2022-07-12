package com.teamharmony.newscommunity.users.repository;


import com.teamharmony.newscommunity.users.entity.Tokens;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokensRepository extends JpaRepository<Tokens, Long> {
	Tokens findByUsername(String username);
}
