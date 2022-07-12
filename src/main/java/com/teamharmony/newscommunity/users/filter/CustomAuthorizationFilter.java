package com.teamharmony.newscommunity.users.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamharmony.newscommunity.users.repository.TokensRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.teamharmony.newscommunity.users.security.AuthConstants.TOKEN_TYPE;
import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

// 사용자 권한 확인
@Slf4j @RequiredArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {
	private final UserDetailsService userDetailsService;
	private final TokensRepository tokensRepository;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		if (request.getServletPath().equals("/api/login") ||
				request.getServletPath().equals("/api/token/refresh") ||
				request.getServletPath().startsWith("/api/signup") ||
				request.getServletPath().startsWith("/api/supports") ||
				request.getServletPath().startsWith("/api/news")) {
			
			filterChain.doFilter(request, response);
		} else {
			String authorizationHeader = request.getHeader(AUTHORIZATION);
			// it has the word bearer in front of a token, we know that it's our token
			if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_TYPE)) {
				try {
						String access_token = authorizationHeader.substring(TOKEN_TYPE.length());
						Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
						JWTVerifier verifier = JWT.require(algorithm)
																			.build();
						DecodedJWT decodedJWT = verifier.verify(access_token);
						String username = decodedJWT.getSubject();
						
						// 해당 유저의 허용된 토큰값과 비교
						String allowedToken = tokensRepository.findByUsername(username).getAccessToken();
						if(!allowedToken.equals(access_token))throw new IllegalArgumentException("Token not found");
					
						String[] roles = decodedJWT.getClaim("roles")
																			 .asArray(String.class);
						Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
						stream(roles).forEach(role -> {
							authorities.add(new SimpleGrantedAuthority(role));
						});
						User user = (User) userDetailsService.loadUserByUsername(username);
						UsernamePasswordAuthenticationToken authenticationToken =
								new UsernamePasswordAuthenticationToken(user, null, authorities);
						// SS determine what resource they can access and what they can access depending on the roles
						SecurityContextHolder.getContext()
																 .setAuthentication(authenticationToken);
						filterChain.doFilter(request, response);
					} catch (Exception e) {
						log.error("Error logging in: {}", e.getMessage());
						setError(response, e);
				}
				} else {
					// otherwise, just let the request continue
					filterChain.doFilter(request, response);
				}
			}
		}
	
	private static void setError(HttpServletResponse response, Exception e) throws IOException {
		response.setHeader("error", e.getMessage());
		response.setStatus(FORBIDDEN.value());
		Map<String, String> error = new HashMap<>();
		error.put("error_msg", e.getMessage());
		response.setContentType(APPLICATION_JSON_VALUE);
		new ObjectMapper().writeValue(response.getOutputStream(), error);
	}
}
