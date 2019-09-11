package com.boot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

import com.boot.filter.JWTAuthEntryPoint;
import com.boot.filter.JWTAuthFilter;
import com.boot.filter.JWTLoginFilter;

@Order(1)
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JWTAuthEntryPoint jwtAuthEntryPoint;

	@Autowired
	private UserDetailsService userDetailsService;
	
	private final String[] PERMIT_ALL_GET = {
		"/api/user/load-all"
	};

	private final String[] PERMIT_ALL_POST = {
		"/api/user/register"
	};
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable() 	 	// We don't need CSRF for JWT authentication
			.exceptionHandling() 	//
			.authenticationEntryPoint(jwtAuthEntryPoint) //
			.and() //
				.sessionManagement() //
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 
			.and() //
				.authorizeRequests() //
				.antMatchers("/").permitAll() //
				.antMatchers( "/favicon.ico").permitAll() //
				.antMatchers(HttpMethod.GET, PERMIT_ALL_GET).permitAll()	//
				.antMatchers(HttpMethod.POST, PERMIT_ALL_POST).permitAll()	//
				.anyRequest() 	 //
				.authenticated() //
			.and() //
			.addFilterBefore(new JWTLoginFilter("/api/auth/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class) //
			.addFilterBefore(new JWTAuthFilter(userDetailsService), UsernamePasswordAuthenticationFilter.class) //
			.headers() //
			.cacheControl();
	}
	
	/**
	 * Setup Service find User in database & PasswordEncoder.
	 */
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
	
}
