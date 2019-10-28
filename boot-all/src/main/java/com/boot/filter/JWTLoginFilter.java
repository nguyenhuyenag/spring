package com.boot.filter;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.util.UrlPathHelper;

import com.boot.entity.User;
import com.boot.request.LoginRequest;
import com.boot.response.CustomError;
import com.boot.response.UserResponse;
import com.boot.util.JsonUtils;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(JWTLoginFilter.class);
	private static final UrlPathHelper URL = new UrlPathHelper();

	public JWTLoginFilter(String url, AuthenticationManager auth) {
		super(new AntPathRequestMatcher(url));
		this.setAuthenticationManager(auth);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws JsonParseException, JsonMappingException, IOException {
		LoginRequest login = JsonUtils.MAPPER.readValue(request.getInputStream(), LoginRequest.class);
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword(), new ArrayList<>());
		return this.getAuthenticationManager().authenticate(auth);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication auth) throws IOException, ServletException {
		User user = (User) auth.getPrincipal();
		UserResponse object = new UserResponse(user.getUsername(), user.getRole());
		String json = JsonUtils.writeAsString(object);
		response.getWriter().write(json);
		response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
		String token = TokenHandler.buildToken(user.getUsername());
		response.addHeader(HttpHeaders.AUTHORIZATION, TokenHandler.PREFIX + token);
	}

	// TODO
//	@Override
//	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
//			AuthenticationException exception) throws IOException, ServletException {
//		LOGGER.info("Failed authentication while attempting to access: " + URL.getPathWithinApplication(request));
//		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//		response.setContentType("application/json");
//		Res res = new Res(HttpServletResponse.SC_UNAUTHORIZED, "Username or password is incorrect!");
//		String json = JsonUtils.writeAsString(res);
//		response.getWriter().write(json);
//		response.getWriter().flush();
//	}

}
