package com.project.bsky.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.bsky.model.User;
import com.project.bsky.repository.UserRepository;
import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	@Autowired
	private UserRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = repository.findByUserNameIgnoreCase(username);
		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassWord(),
				new ArrayList<>());
	}
}
