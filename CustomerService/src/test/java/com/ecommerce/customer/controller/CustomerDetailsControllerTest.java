package com.ecommerce.customer.controller;

import com.ecommerce.customer.dto.AddressDto;
import com.ecommerce.customer.dto.CustomerDto;
import com.ecommerce.customer.entity.StringInput;
import com.ecommerce.customer.exception.CustomerException;
import com.ecommerce.customer.security.LogoutService;
import com.ecommerce.customer.service.declaration.CustomerDetailsService;
import com.ecommerce.customer.service.declaration.OtpService;
import com.ecommerce.customer.service.declaration.RefreshTokenService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerDetailsControllerTest {

    @Mock
    private CustomerDetailsService customerDetailsService;

    @Mock
    private OtpService otpService;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private LogoutService logoutService;

    @Mock
    private Environment environment;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private CustomerDetailsController customerDetailsController;

    @Test
    public void deleteAcc() throws CustomerException {
        when(customerDetailsService.deleteAcc()).thenReturn(true);

        ResponseEntity<Boolean> response = customerDetailsController.deleteAcc();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }

    @Test
    public void logoutApi() throws CustomerException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        doNothing().when(refreshTokenService).deleteToken(anyString());
        doNothing().when(logoutService).logout(any(HttpServletRequest.class));
        when(environment.getProperty(anyString())).thenReturn("Logged out");

        ResponseEntity<String> response = customerDetailsController.logoutApi(new StringInput("refreshToken"), request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Logged out", response.getBody());
    }

    @Test
    public void invalidateAllToken() throws CustomerException {
        when(customerDetailsService.invalidateAllToken()).thenReturn(true);

        ResponseEntity<Boolean> response = customerDetailsController.invalidateAllToken();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }

    @Test
    public void customerPasswordValidation() throws CustomerException {
        when(customerDetailsService.passwordVerify(anyString())).thenReturn(true);

        ResponseEntity<Boolean> response = customerDetailsController.customerPasswordValidation(new StringInput("password"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }

    @Test
    public void passwordChange() throws CustomerException {
        when(customerDetailsService.changePassword(anyString())).thenReturn("Password changed");

        ResponseEntity<String> response = customerDetailsController.passwordChange(new StringInput("newPassword"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password changed", response.getBody());
    }

    @Test
    public void customerDetails() {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("test@example.com");
        CustomerDto customerDto = new CustomerDto();
        when(customerDetailsService.customerDetails(anyString())).thenReturn(customerDto);

        ResponseEntity<CustomerDto> response = customerDetailsController.customerDetails(principal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customerDto, response.getBody());
    }

    @Test
    public void editDetails() throws CustomerException {
        CustomerDto customerDto = new CustomerDto();
        when(customerDetailsService.editDetails(any(CustomerDto.class))).thenReturn(customerDto);

        ResponseEntity<CustomerDto> response = customerDetailsController.editDetails(customerDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customerDto, response.getBody());
    }

    @Test
    public void generateEmailOtp() throws MessagingException {
        when(otpService.generateOtp(anyString())).thenReturn(1234);
        doNothing().when(otpService).sendOtpByEmail(anyString(), anyString());
        when(environment.getProperty(anyString())).thenReturn("OTP sent to ");

        ResponseEntity<String> response = customerDetailsController.generateEmailOtp(new StringInput("test@example.com"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("OTP sent to test@example.com", response.getBody());
    }

    @Test
    public void editEmail() throws CustomerException {
        doNothing().when(customerDetailsService).changeEmail(anyString());
        when(environment.getProperty(anyString())).thenReturn("Email changed");

        ResponseEntity<String> response = customerDetailsController.editEmail("new@example.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Email changed", response.getBody());
    }

    @Test
    public void addAddress() throws CustomerException {
        AddressDto addressDto = new AddressDto();
        when(customerDetailsService.addAddress(any(AddressDto.class))).thenReturn(addressDto);

        ResponseEntity<AddressDto> response = customerDetailsController.addAddress(addressDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(addressDto, response.getBody());
    }

    @Test
    public void getAddress() throws CustomerException {
        List<AddressDto> addressList = new ArrayList<>();
        when(customerDetailsService.getAddress()).thenReturn(addressList);

        ResponseEntity<List<AddressDto>> response = customerDetailsController.getAddress();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(addressList, response.getBody());
    }

    @Test
    public void editAddress() throws CustomerException {
        AddressDto addressDto = new AddressDto();
        when(customerDetailsService.editAddress(any(AddressDto.class))).thenReturn(addressDto);

        ResponseEntity<AddressDto> response = customerDetailsController.editAddress(addressDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(addressDto, response.getBody());
    }

    @Test
    public void deleteAddress() throws CustomerException {
        doNothing().when(customerDetailsService).deleteAddress(anyInt());
        when(environment.getProperty(anyString())).thenReturn("Address deleted");

        ResponseEntity<String> response = customerDetailsController.deleteAddress("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Address deleted", response.getBody());
    }
}