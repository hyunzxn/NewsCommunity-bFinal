package com.teamharmony.newscommunity.users.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.teamharmony.newscommunity.users.dto.ProfileVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class UserProfile {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;
	private String nickname;
	private String profile_pic;
	private String profile_info;
	@JsonBackReference
	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	
	public void setUser(User user) {
		this.user = user;
		user.setProfile(this);
	}
	
	@Builder
	public UserProfile(String nickname, String profile_pic, String profile_info) {
		this.nickname = nickname;
		this.profile_pic = profile_pic;
		this.profile_info = profile_info;
	}

	public UserProfile(ProfileVO vo) {
		this(vo.getName_give(), vo.getFile_give().getOriginalFilename(), vo.getAbout_give());
	}
}
