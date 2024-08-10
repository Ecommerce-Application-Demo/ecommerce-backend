package com.ecommerce.customer.serviceimpl;

import com.ecommerce.customer.entity.CustomerAuth;
import com.ecommerce.customer.exception.ErrorCode;
import com.ecommerce.customer.repository.CustomerAuthRepository;
import com.ecommerce.customer.security.CustomUserDetails;
import com.ecommerce.customer.service.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private CustomerAuthRepository customerAuthRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void loadUserByUsername_UserExists() {
        CustomerAuth customerAuth = new CustomerAuth();
        customerAuth.setEmail("test@example.com");
        when(customerAuthRepository.findByEmail(anyString())).thenReturn(Optional.of(customerAuth));

        CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername("test@example.com");

        assertNotNull(userDetails);
        assertEquals(customerAuth.getEmail(), userDetails.getUsername());
    }

    @Test
    void loadUserByUsername_UserDoesNotExist() {
        when(customerAuthRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("nonexistent@example.com");
        });

        assertEquals(ErrorCode.USER_NOT_FOUND.name(), exception.getMessage());
    }
}