package com.teamharmony.newscommunity.domain.auth.util;

import com.teamharmony.newscommunity.exception.TokenException;
import org.springframework.http.ResponseCookie;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class CookieUtil {
	// 리프레쉬 쿠키값 가져오기
	public static String getRefCookie(HttpServletRequest request) throws TokenException {
		Cookie[] cookies = request.getCookies();
		Cookie cookie = null;
		if (cookies != null && cookies.length > 0) {
			for (Cookie value : cookies) {
				if (value.getName()
				         .equals("ref_uid")) {
					cookie = value;
				}
			}
		}
		if(cookie == null)	throw TokenException.builder().message("갱신 토큰을 찾을 수 없습니다.").code("A401").build();
		return cookie.getValue();
	}
	
	// 리프레쉬 쿠키 삭제(만료시간 0으로 설정)
	public static String removeRefCookie() {
		ResponseCookie refresh = ResponseCookie.from("ref_uid", "")
		                                       .maxAge(0)
		                                       .path("/")
		                                       .build();
		return refresh.toString();
	}
}
