package com.service.impl;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.entity.User;
import com.repository.UserRepository;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository repository;

	private Set<SimpleGrantedAuthority> getAuthority(User user) {
		Set<SimpleGrantedAuthority> auth = new HashSet<>();
		user.getRoles().forEach(role -> {
			auth.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
		});
		return auth;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (StringUtils.isEmpty(username)) {
			throw new UsernameNotFoundException("Username is empty!");
		}
		final Optional<User> user = repository.findByUsername(username);
		if (user.isPresent()) {
			return new org.springframework.security.core.userdetails //
					   .User(user.get().getUsername(), user.get().getPassword(), getAuthority(user.get()));
		} else {
			throw new UsernameNotFoundException("User `" + username + "` was not found!");
		}
	}
}
