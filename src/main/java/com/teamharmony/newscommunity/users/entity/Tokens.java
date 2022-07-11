package com.teamharmony.newscommunity.users.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Builder
public class Tokens {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	@Column(unique = true, nullable=false)
	private String username;
	@Column(nullable = false)
	private String accessToken;
	@Column(nullable = false)
	private String refreshToken;
	
	public void update(String access_token, String refresh_token) {
		this.accessToken = access_token;
		this.refreshToken = refresh_token;
	}
}
