package com;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.filter.Http401Unauthorized;
import com.filter.JWTAuthenticationFilter;
import com.filter.JWTLoginFilter;

@Order(1)
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	private final String[] PERMIT_ALL_GET = {
		// "/api/user/load-all"
	};

	private final String[] PERMIT_ALL_POST = { "/api/user/register" };

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Disable CSRF
		http.csrf().disable();
		http.exceptionHandling() //
			.authenticationEntryPoint(new Http401Unauthorized()); // handles bad credentials
			// http.accessDeniedHandler(accessDeniedHandler);
		// disable session creation on Spring security
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //
		http.authorizeRequests() //
				.antMatchers("/").permitAll() //
				.antMatchers("/favicon.ico").permitAll() //
				.antMatchers(HttpMethod.GET, PERMIT_ALL_GET).permitAll() //
				.antMatchers(HttpMethod.POST, PERMIT_ALL_POST).permitAll() //
				.anyRequest() //
				.authenticated(); //
		http.addFilterBefore(new JWTLoginFilter("/api/user/login", authenticationManager()),
				UsernamePasswordAuthenticationFilter.class) //
				.addFilterBefore(new JWTAuthenticationFilter(userDetailsService),
						UsernamePasswordAuthenticationFilter.class) //
				.headers().cacheControl();
	}

	// Setup service find User in database & PasswordEncoder
	// @Autowired
	// public void configureGlobal(AuthenticationManagerBuilder auth) throws
	// Exception {
	// auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	// }
	
	// TODO add new
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		// configure AuthenticationManager so that it knows from where to load
		// user for matching credentials
		// Use BCryptPasswordEncoder
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

//	@Bean
//	public AuthenticationManager customAuthenticationManager() throws Exception {
//		return authenticationManager();
//	}

	// Setup PasswordEncoder
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
