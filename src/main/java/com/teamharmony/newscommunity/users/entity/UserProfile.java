package com.teamharmony.newscommunity.users.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.teamharmony.newscommunity.users.vo.ProfileVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class UserProfile {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;
	@Column(nullable = false)
	@Size(max = 10, message = "닉네임은 최대 10자까지 입력 가능합니다.")
	private String nickname;
	@Column(nullable = false)
	private String profile_pic;
	@Size(max = 60, message = "소개글은 최대 60자까지 입력 가능합니다.")
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
		this(vo.getName(), vo.getFile().getOriginalFilename(), vo.getAbout());
	}
	
	public void update(ProfileVO vo) {
		this.nickname = vo.getName();
		this.profile_pic = vo.getFile().getOriginalFilename();
		this.profile_info = vo.getAbout();
	}
	public void notUpdatePic(ProfileVO vo) {
		this.nickname = vo.getName();
		this.profile_info = vo.getAbout();
	}
}
