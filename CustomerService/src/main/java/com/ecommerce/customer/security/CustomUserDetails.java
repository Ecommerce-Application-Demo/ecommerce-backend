package com.ecommerce.customer.security;

import com.ecommerce.customer.entity.CustomerAuth;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7397872130291810640L;

	private String email;
	private String password;
	private Boolean isEnabled;

	public CustomUserDetails(CustomerAuth customerAuth) {
		this.email = customerAuth.getEmail();
		this.password = customerAuth.getPassword();
		this.isEnabled = customerAuth.getIsEnabled();
	}

	public CustomUserDetails(String email) {
		this.email = email;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.emptyList();
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return isEnabled;
	}

}
