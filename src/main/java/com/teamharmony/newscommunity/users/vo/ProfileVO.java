package com.teamharmony.newscommunity.users.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProfileVO {
	@NotBlank(message = "닉네임은 공백일 수 없습니다.")
	@Size(max = 10, message = "닉네임은 최대 10자까지 입력 가능합니다.")
	private String name;
	private MultipartFile file;
	@Size(max = 60, message = "소개글은 최대 60자까지 입력 가능합니다.")
	private String about;
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ProfileVO that = (ProfileVO) o;
		return Objects.equals(name, that.name) &&
				Objects.equals(file, that.file) &&
						Objects.equals(about, that.about);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(name, file, about);
	}
}
