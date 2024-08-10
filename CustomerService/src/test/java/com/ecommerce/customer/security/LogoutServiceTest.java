package com.ecommerce.customer.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LogoutServiceTest {

    @Mock
    private JwtHelper jwtHelper;

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private HttpServletRequest request;

    @Mock
    Claims claims;

    @InjectMocks
    private LogoutService logoutService;

    @Test
    void logout_ValidToken() {
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(jwtHelper.extractAllClaims("validToken")).thenReturn(claims);
        when(claims.get("sessionKey")).thenReturn("validKey");

        logoutService.logout(request);

        verify(redisTemplate).delete("validKey");
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void logout_ExceptionDuringLogout() {
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(jwtHelper.extractAllClaims("validToken")).thenReturn(claims);
        when(claims.get("sessionKey")).thenReturn("validKey");
        doThrow(new NullPointerException("Redis error")).when(redisTemplate).delete("validKey");

        logoutService.logout(request);

        verify(redisTemplate).delete("validKey");
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
