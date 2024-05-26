package com.ecommerce.customer.service;

import com.ecommerce.customer.entity.CustomerAuth;
import com.ecommerce.customer.exception.ErrorCode;
import com.ecommerce.customer.repository.CustomerAuthRepository;
import com.ecommerce.customer.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
    CustomerAuthRepository customerAuthRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		CustomerAuth user = customerAuthRepository.findByEmail(email.toLowerCase())
				.orElseThrow(() -> new UsernameNotFoundException(ErrorCode.USER_NOT_FOUND.name()));
		return Optional.of(user).map(CustomUserDetails::new).get();
	}

}
