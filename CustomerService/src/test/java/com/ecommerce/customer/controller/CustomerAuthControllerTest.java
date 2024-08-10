package com.ecommerce.customer.controller;

import com.ecommerce.customer.dto.*;
import com.ecommerce.customer.entity.JwtRefreshToken;
import com.ecommerce.customer.entity.StringInput;
import com.ecommerce.customer.exception.CustomerException;
import com.ecommerce.customer.exception.ErrorCode;
import com.ecommerce.customer.repository.CustomerRepository;
import com.ecommerce.customer.security.JwtHelper;
import com.ecommerce.customer.service.declaration.CustomerService;
import com.ecommerce.customer.service.declaration.OtpService;
import com.ecommerce.customer.service.declaration.RefreshTokenService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.time.Instant;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerAuthControllerTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private OtpService otpService;

    @Mock
    private JwtHelper jwtHelper;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Environment environment;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerAuthController customerAuthController;

    @Test
    public void customerIsPresent_EmailExists() {
        when(customerService.isPresent(anyString())).thenReturn(true);

        ResponseEntity<Boolean> response = customerAuthController.customerIsPresent(new StringInput("test@example.com"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }

    @Test
    public void customerIsPresent_EmailDoesNotExist() {
        when(customerService.isPresent(anyString())).thenReturn(false);

        ResponseEntity<Boolean> response = customerAuthController.customerIsPresent(new StringInput("test@example.com"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody());
    }

    @Test
    public void generateEmailOtp_ValidEmail() throws MessagingException {
        when(otpService.generateOtp(anyString())).thenReturn(1234);
        doNothing().when(otpService).sendOtpByEmail(anyString(), anyString());
        when(environment.getProperty(anyString())).thenReturn("OTP sent to ");

        ResponseEntity<String> response = customerAuthController.generateEmailOtp(new StringInput("test@example.com"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("OTP sent to test@example.com", response.getBody());
    }

    @Test
    public void validateEmailOtp_ValidOtpAndRegisteredUser() throws CustomerException {
        when(otpService.validateOtp(anyString(), anyInt())).thenReturn(true);
        when(customerService.isPresent(anyString())).thenReturn(true);
        when(environment.getProperty(anyString())).thenReturn("OTP validated. User is registered.");

        ResponseEntity<String> response = customerAuthController.validateEmailOtp(new OtpDetailsDto("test@example.com", 1234));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("OTP validated. User is registered.", response.getBody());
    }

    @Test
    public void validateEmailOtp_ValidOtpAndUnregisteredUser() throws CustomerException {
        when(otpService.validateOtp(anyString(), anyInt())).thenReturn(true);
        when(customerService.isPresent(anyString())).thenReturn(false);
        when(environment.getProperty(anyString())).thenReturn("OTP validated. User is not registered.");

        ResponseEntity<String> response = customerAuthController.validateEmailOtp(new OtpDetailsDto("test@example.com", 1234));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("OTP validated. User is not registered.", response.getBody());
    }

    @Test
    public void validateEmailOtp_InvalidOtp() throws CustomerException {
        when(otpService.validateOtp(anyString(), anyInt())).thenReturn(false);

        assertThrows(CustomerException.class, () -> {
            customerAuthController.validateEmailOtp(new OtpDetailsDto("test@example.com", 1234));
        });
    }

    @Test
    public void validateEmailOtpV2_ValidOtpAndRegisteredUser() throws CustomerException {
        when(otpService.validateOtp(anyString(), anyInt())).thenReturn(true);
        when(customerService.isPresent(anyString())).thenReturn(true);
        when(jwtHelper.generateToken(anyString())).thenReturn("jwtToken");
        when(refreshTokenService.getRefreshToken(anyString())).thenReturn("refreshToken");
        when(refreshTokenService.extractExpiration(anyString())).thenReturn(Instant.now().plusSeconds(300));
        when(customerService.welcomeService(anyString())).thenReturn("Welcome Test User");

        ResponseEntity response = customerAuthController.validateEmailOtpV2(new OtpDetailsDto("test@example.com", 1234));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        OtpValidationResponse responseBody = (OtpValidationResponse) response.getBody();
        assertNotNull(responseBody);
        assertTrue(responseBody.getIsValid());
        assertTrue(responseBody.getIsRegistered());
        JwtTokens tokens = responseBody.getTokens();
        assertNotNull(tokens);
        assertEquals("jwtToken", tokens.getAccessToken());
        assertEquals("refreshToken", tokens.getRefreshToken());
    }

    @Test
    public void validateEmailOtpV2_ValidOtpAndUnregisteredUser() throws CustomerException {
        when(otpService.validateOtp(anyString(), anyInt())).thenReturn(true);
        when(customerService.isPresent(anyString())).thenReturn(false);

        ResponseEntity response = customerAuthController.validateEmailOtpV2(new OtpDetailsDto("test@example.com", 1234));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        OtpValidationResponse responseBody = (OtpValidationResponse) response.getBody();
        assertNotNull(responseBody);
        assertTrue(responseBody.getIsValid());
        assertFalse(responseBody.getIsRegistered());
        assertNull(responseBody.getTokens());
    }

    @Test
    public void validateEmailOtpV2_InvalidOtp() throws CustomerException {
        when(otpService.validateOtp(anyString(), anyInt())).thenReturn(false);

        assertThrows(CustomerException.class, () -> {
            customerAuthController.validateEmailOtpV2(new OtpDetailsDto("test@example.com", 1234));
        });
    }

    @Test
    public void customerRegisterApi_ValidRegistration() throws CustomerException {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setEmail("test@example.com");
        customerDto.setPassword("Password");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(new UsernamePasswordAuthenticationToken("test@example.com", "password", new ArrayList<>()));
        when(jwtHelper.generateToken(anyString())).thenReturn("jwtToken");
        when(refreshTokenService.getRefreshToken(anyString())).thenReturn("refreshToken");
        when(refreshTokenService.extractExpiration(anyString())).thenReturn(Instant.now().plusSeconds(300));

        ResponseEntity<Object> response = customerAuthController.customerRegisterApi(customerDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        JwtTokens tokens = (JwtTokens) response.getBody();
        assertNotNull(tokens);
        assertEquals("jwtToken", tokens.getAccessToken());
        assertEquals("refreshToken", tokens.getRefreshToken());
    }

    @Test
    public void customerLoginApi_ValidCredentials() throws CustomerException {
        CustomerAuthDto customerAuthDto = new CustomerAuthDto("test@example.com", "password");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(new UsernamePasswordAuthenticationToken("test@example.com", "password", new ArrayList<>()));
        when(jwtHelper.generateToken(anyString())).thenReturn("jwtToken");
        when(refreshTokenService.getRefreshToken(anyString())).thenReturn("refreshToken");
        when(refreshTokenService.extractExpiration(anyString())).thenReturn(Instant.now().plusMillis(60000));
        when(customerService.welcomeService(anyString())).thenReturn("Welcome Test User");

        ResponseEntity<Object> response = customerAuthController.customerLoginApi(customerAuthDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        JwtTokens tokens = (JwtTokens) response.getBody();
        assertNotNull(tokens);
        assertEquals("jwtToken", tokens.getAccessToken());
        assertEquals("refreshToken", tokens.getRefreshToken());
    }

    @Test
    public void customerLoginApi_InvalidCredentials() {
        CustomerAuthDto customerAuthDto = new CustomerAuthDto("invalid@example.com", "WrongPassword");
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken("invalid@example.com", "WrongPassword");
        authToken.setAuthenticated(false);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authToken);

        assertThrows(CustomerException.class, () -> customerAuthController.customerLoginApi(customerAuthDto));
    }

    @Test
    public void forgetPassword_ValidOtpAndRegisteredUser() throws CustomerException {
        ForgetPasswordDto forgetPasswordDto = new ForgetPasswordDto(1234, "test@example.com", "newPassword");
        when(otpService.validateOtp(anyString(), anyInt())).thenReturn(true);
        when(customerService.isPresent(anyString())).thenReturn(true);
        when(customerService.forgetpassword(anyString(), anyString())).thenReturn("Password updated successfully.");

        ResponseEntity<String> response = customerAuthController.forgetPassword(forgetPasswordDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password updated successfully.", response.getBody());
    }

    @Test
    public void forgetPassword_ValidOtpAndUnregisteredUser() throws CustomerException {
        ForgetPasswordDto forgetPasswordDto = new ForgetPasswordDto(1234, "test@example.com", "newPassword");
        when(otpService.validateOtp(anyString(), anyInt())).thenReturn(true);
        when(customerService.isPresent(anyString())).thenReturn(false);

        assertThrows(CustomerException.class, () -> {
            customerAuthController.forgetPassword(forgetPasswordDto);
        });
    }

    @Test
    public void forgetPassword_InvalidOtp() throws CustomerException {
        ForgetPasswordDto forgetPasswordDto = new ForgetPasswordDto(1234, "test@example.com", "newPassword");
        when(otpService.validateOtp(anyString(), anyInt())).thenReturn(false);

        assertThrows(CustomerException.class, () -> {
            customerAuthController.forgetPassword(forgetPasswordDto);
        });
    }

    @Test
    public void generateNewJwt_ValidRefreshToken() throws CustomerException {
        when(refreshTokenService.tokenValidation(anyString())).thenReturn("test@example.com");
        when(refreshTokenService.extendTokenTime(anyString())).thenReturn(new JwtRefreshToken("test@example.com","refreshToken", Instant.now().plusSeconds(300)));
        when(jwtHelper.generateToken(anyString())).thenReturn("jwtToken");
        when(customerService.welcomeService(anyString())).thenReturn("Welcome Test User");

        ResponseEntity<JwtTokens> response = customerAuthController.generateNewJwt(new StringInput("refreshToken"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        JwtTokens tokens = response.getBody();
        assertNotNull(tokens);
        assertEquals("jwtToken", tokens.getAccessToken());
        assertEquals("refreshToken", tokens.getRefreshToken());
    }

    @Test
    public void generateNewJwt_InvalidRefreshToken() throws CustomerException {
        when(refreshTokenService.tokenValidation(anyString())).thenThrow(new CustomerException(ErrorCode.REFRESH_TOKEN_EXPIRED.name()));

        assertThrows(CustomerException.class, () -> {
            customerAuthController.generateNewJwt(new StringInput("invalidToken"));
        });
    }
}