package com.ecommerce.customer.serviceimpl;

import com.ecommerce.customer.dto.CustomerDto;
import com.ecommerce.customer.entity.Customer;
import com.ecommerce.customer.entity.CustomerAuth;
import com.ecommerce.customer.exception.CustomerException;
import com.ecommerce.customer.repository.CustomerAuthRepository;
import com.ecommerce.customer.repository.CustomerRepository;
import com.ecommerce.customer.repository.RefreshTokenRepository;
import com.ecommerce.customer.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerAuthRepository customerAuthRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private CustomerDto customerDto;
    private Customer customer;
    private CustomerAuth customerAuth;

    @BeforeEach
    public void setUp() {
        customerDto = new CustomerDto();
        customerDto.setName("test");
        customerDto.setEmail("test@example.com");
        customerDto.setPassword("Password@1234");

        customer = new Customer();
        customer.setName("test");
        customer.setEmail("test@example.com");
        customer.setPassword("Password@1234");

        customerAuth = new CustomerAuth();
        customerAuth.setEmail("test@example.com");
        customerAuth.setPassword("encodedPassword");
        customerAuth.setAuthCustomer(customer);
    }

    @Test
    public void testRegisterNewCustomer() throws CustomerException {
        when(customerRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(modelMapper.map(any(CustomerDto.class), any(Class.class))).thenReturn(customer);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        customerService.registerNewCustomer(customerDto);

        verify(customerRepository, times(1)).save(any(Customer.class));
        verify(customerAuthRepository, times(1)).save(any(CustomerAuth.class));
    }

    @Test
    public void testRegisterNewCustomer_EmailAlreadyExists() {
        when(customerRepository.findByEmail(anyString())).thenReturn(Optional.of(customer));

        assertThrows(CustomerException.class, () -> customerService.registerNewCustomer(customerDto));
    }

    @Test
    public void testWelcomeService() {
        when(customerRepository.findByEmail(anyString())).thenReturn(Optional.of(customer));

        String result = customerService.welcomeService("test@example.com");

        assertEquals(customer.getName(), result);
    }

    @Test
    public void testIsPresent() {
        when(customerRepository.findByEmail(anyString())).thenReturn(Optional.of(customer));

        Boolean result = customerService.isPresent("test@example.com");

        assertTrue(result);
    }

    @Test
    public void testForgetPassword() throws CustomerException {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(customerAuthRepository.findById(anyString())).thenReturn(Optional.of(customerAuth));
        when(passwordEncoder.encode(anyString())).thenReturn("newEncodedPassword");
        when(redisTemplate.keys(anyString())).thenReturn(Set.of("key1", "key2"));
        when(redisTemplate.opsForValue().get(anyString())).thenReturn("test@example.com");

        String result = customerService.forgetpassword("test@example.com", "newPassword");

        assertEquals(customerService.passwordSuccessMessage, result);
        verify(customerAuthRepository, times(1)).save(any(CustomerAuth.class));
        verify(redisTemplate, times(2)).delete(anyString());
        verify(refreshTokenRepository, times(1)).deleteByEmail(anyString());
    }

    @Test
    public void testForgetPassword_Exception() {
        when(customerAuthRepository.findById(anyString())).thenThrow(new RuntimeException());

        assertThrows(CustomerException.class, () -> customerService.forgetpassword("test@example.com", "newPassword"));
    }
}