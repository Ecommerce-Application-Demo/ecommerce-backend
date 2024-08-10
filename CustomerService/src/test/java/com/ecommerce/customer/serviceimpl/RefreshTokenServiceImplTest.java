package com.ecommerce.customer.serviceimpl;

import com.ecommerce.customer.entity.JwtRefreshToken;
import com.ecommerce.customer.exception.CustomerException;
import com.ecommerce.customer.repository.RefreshTokenRepository;
import com.ecommerce.customer.service.impl.RefreshTokenServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RefreshTokenServiceImplTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private RefreshTokenServiceImpl refreshTokenService;

    private JwtRefreshToken refreshToken;

    @BeforeEach
    public void setUp() {
        refreshToken = new JwtRefreshToken();
        refreshToken.setEmail("test@example.com");
        refreshToken.setToken("testToken");
        refreshToken.setExpirationDate(Instant.now().plusSeconds(300));
    }

    @Test
    public void testGetRefreshToken() {
        ReflectionTestUtils.setField(refreshTokenService, "REFRESH_TOKEN_VALIDITY", 300);

        when(refreshTokenRepository.save(any(JwtRefreshToken.class))).thenReturn(refreshToken);

        String result = refreshTokenService.getRefreshToken("test@example.com");

        assertNotNull(result);
        verify(refreshTokenRepository, times(1)).save(any(JwtRefreshToken.class));
    }

    @Test
    public void retrieveTokenFromDb_ValidToken() throws CustomerException {
        when(refreshTokenRepository.findByToken(anyString())).thenReturn(Optional.of(refreshToken));

        JwtRefreshToken result = refreshTokenService.retrieveTokenFromDb("testToken");

        assertNotNull(result);
        assertEquals(refreshToken, result);
    }

    @Test
    public void retrieveTokenFromDb_InvalidToken() {
        when(refreshTokenRepository.findByToken(anyString())).thenReturn(Optional.empty());

        assertThrows(CustomerException.class, () -> refreshTokenService.retrieveTokenFromDb("invalidToken"));
    }

    @Test
    public void testTokenValidation_Valid() throws CustomerException {
        when(refreshTokenRepository.findByToken(anyString())).thenReturn(Optional.of(refreshToken));
        String result = refreshTokenService.tokenValidation(refreshToken.getToken());
        assertNotNull(result);
    }

    @Test
    public void testTokenValidation_Expired() {
        when(refreshTokenRepository.findByToken(anyString())).thenReturn(Optional.of(refreshToken));
        refreshToken.setExpirationDate(Instant.now().minusSeconds(60));
        assertThrows(CustomerException.class, () -> {
            refreshTokenService.tokenValidation(refreshToken.getToken());
        });
    }

    @Test
    public void extendTokenTime() throws CustomerException {
        when(refreshTokenRepository.findByToken(anyString())).thenReturn(Optional.of(refreshToken));
        ReflectionTestUtils.setField(refreshTokenService, "REFRESH_TOKEN_VALIDITY", 300);
        when(refreshTokenRepository.save(any(JwtRefreshToken.class))).thenReturn(refreshToken);

        JwtRefreshToken result = refreshTokenService.extendTokenTime("testToken");

        assertEquals(result,refreshToken);
        verify(refreshTokenRepository, times(1)).save(any(JwtRefreshToken.class));
    }

    @Test
    public void deleteTokenValidToken() throws CustomerException {
        when(refreshTokenRepository.findByToken(anyString())).thenReturn(Optional.of(refreshToken));

        refreshTokenService.deleteToken("testToken");

        verify(refreshTokenRepository, times(1)).deleteById("testToken");
    }

    @Test
    public void cleanupExpiredTokens() {
        JwtRefreshToken expiredToken = new JwtRefreshToken();
        expiredToken.setExpirationDate(Instant.now().minusSeconds(60));
        when(refreshTokenRepository.findAll()).thenReturn(List.of(expiredToken));

        refreshTokenService.cleanup();

        verify(refreshTokenRepository, times(1)).delete(expiredToken);
    }
}