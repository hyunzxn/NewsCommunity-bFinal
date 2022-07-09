package com.teamharmony.newscommunity.users.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProfileVO {
	private String name_give;
	private MultipartFile file_give;
	private String about_give;
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ProfileVO that = (ProfileVO) o;
		return Objects.equals(name_give, that.name_give) &&
				Objects.equals(file_give, that.file_give) &&
						Objects.equals(about_give, that.about_give);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(name_give, file_give, about_give);
	}
}
