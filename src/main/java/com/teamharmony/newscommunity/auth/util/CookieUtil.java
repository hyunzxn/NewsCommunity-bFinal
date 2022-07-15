package com.teamharmony.newscommunity.auth.util;

import org.springframework.http.ResponseCookie;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

public class CookieUtil {
	// 리프레쉬 쿠키값 가져오기
	public static String getRefCookie(HttpServletRequest request) {
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
		return cookie.getValue();
	}
	
	// 리프레쉬 쿠키 삭제(만료시간 0으로 설정)
	public static void removeRefCookie(HttpServletResponse response) {
		ResponseCookie refresh = ResponseCookie.from("ref_uid", "")
		                                       .maxAge(0)
		                                       .path("/")
		                                       .build();
		response.setHeader(SET_COOKIE, refresh.toString());
	}
}
