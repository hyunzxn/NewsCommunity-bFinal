package com.teamharmony.newscommunity.users.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.teamharmony.newscommunity.comments.entity.Comment;
import com.teamharmony.newscommunity.comments.entity.Likes;
import com.teamharmony.newscommunity.supports.entity.Support;
import com.teamharmony.newscommunity.users.dto.request.SignupRequestDto;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class User  extends Timestamped {
	@JsonIgnore
	@Id @GeneratedValue(strategy = IDENTITY)
	@Column(name = "user_id")
	private Long id;
	@Column(unique = true, nullable = false)
	@Size(min = 2, max = 10)
	private String username;
	@Column(nullable = false)
	@Size(min = 8, max = 20)
	private String password;
	private String email;
	@OneToMany(mappedBy="user", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	private Collection<UserRole> userRoles = new ArrayList<>();

	@JsonBackReference
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Comment> comments;

	@JsonBackReference
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Likes> likesList;

	@JsonBackReference
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Support> supports;

	public void addComment(Comment comment) {
		comment.setUser(this);
		comments.add(comment);
	}

	public void addLikes(Likes likes) {
		likes.setUser(this);
		likesList.add(likes);
	}
	public void addSupports(Support support) {
		support.setUser(this);
		supports.add(support);
	}


	@JsonManagedReference
	@OneToOne(mappedBy = "user", cascade = CascadeType.PERSIST)
	private UserProfile profile;

	@Builder
	public User(SignupRequestDto dto) {
		this.username = dto.getUsername();
		this.password = dto.getPassword();
	}
}
