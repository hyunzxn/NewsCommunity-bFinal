package com.teamharmony.newscommunity.users.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProfileVO {
	private String name;
	private MultipartFile file;
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
