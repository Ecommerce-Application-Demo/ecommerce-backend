package com.ecommerce.customer.service;

import java.util.Optional;
import com.ecommerce.customer.repository.CustomerAuthRepository;
import com.ecommerce.customer.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.ecommerce.customer.entity.CustomerAuth;

@Component
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
    CustomerAuthRepository customerAuthRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		CustomerAuth user = customerAuthRepository.findByEmail(email.toLowerCase()).orElseThrow(() -> new UsernameNotFoundException("CUSTOMER.NOT.FOUND"));
		return Optional.of(user).map(CustomUserDetails::new).get();
	}

}
