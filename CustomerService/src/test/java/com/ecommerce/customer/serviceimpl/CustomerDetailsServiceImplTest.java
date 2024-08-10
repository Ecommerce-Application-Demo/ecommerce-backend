package com.ecommerce.customer.serviceimpl;

import com.ecommerce.customer.dto.AddressDto;
import com.ecommerce.customer.dto.CustomerDto;
import com.ecommerce.customer.entity.Address;
import com.ecommerce.customer.entity.Customer;
import com.ecommerce.customer.entity.CustomerAuth;
import com.ecommerce.customer.entity.DefaultAddress;
import com.ecommerce.customer.exception.CustomerException;
import com.ecommerce.customer.exception.ErrorCode;
import com.ecommerce.customer.repository.*;
import com.ecommerce.customer.service.declaration.CustomerService;
import com.ecommerce.customer.service.impl.CustomerDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerDetailsServiceImplTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerAuthRepository customerAuthRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private DefaultAddressRepository defaultAddressRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private CustomerDetailsServiceImpl customerDetailsService;

    private Customer customer;
    private CustomerAuth customerAuth;
    private CustomerDto customerDto;
    private Address address;
    private AddressDto addressDto;

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

        address = new Address();
        address.setAddId(1);
        address.setName("test");
        address.setPincode("123456");
        address.setUserIdEmail("test@example.com");

        addressDto = new AddressDto();
        addressDto.setName("test");
        addressDto.setPincode("123456");
        addressDto.setDefault(true);

    }

    @Test
    public void testGetUser_Authenticated() throws CustomerException {
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");

        String result = customerDetailsService.getUser();

        assertEquals("test@example.com", result);
    }

    @Test
    public void testCustomerDetails() {
        when(customerRepository.findByEmail(anyString())).thenReturn(Optional.of(customer));
        when(modelMapper.map(any(Customer.class), any(Class.class))).thenReturn(customerDto);

        CustomerDto result = customerDetailsService.customerDetails("test@example.com");

        assertEquals(customerDto, result);
    }

    @Test
    public void testPasswordVerify() throws CustomerException {
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");
        when(customerAuthRepository.findById(anyString())).thenReturn(Optional.of(customerAuth));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        Boolean result = customerDetailsService.passwordVerify("password");

        assertTrue(result);
    }

    @Test
    public void testChangePassword() throws CustomerException {
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");

        when(customerAuthRepository.findById(anyString())).thenReturn(Optional.of(customerAuth));
        when(passwordEncoder.encode(anyString())).thenReturn("newEncodedPassword");

        String result = customerDetailsService.changePassword("newPassword");

        assertEquals(customerDetailsService.passwordSuccessMessage, result);
        verify(customerAuthRepository, times(1)).save(any(CustomerAuth.class));
    }

    @Test
    public void testInvalidateAllToken() throws CustomerException {
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(redisTemplate.keys(anyString())).thenReturn(Set.of("key1", "key2"));
        when(valueOperations.get(anyString())).thenReturn("test@example.com");

        Boolean result = customerDetailsService.invalidateAllToken();

        assertTrue(result);
        verify(redisTemplate, times(2)).delete(anyString());
        verify(refreshTokenRepository, times(1)).deleteByEmail(anyString());
    }

    @Test
    public void testDeleteAcc() throws CustomerException {
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");

        doNothing().when(customerRepository).deleteAccount(anyString());

        Boolean result = customerDetailsService.deleteAcc();

        assertTrue(result);
        verify(customerRepository, times(1)).deleteAccount(anyString());
    }

    @Test
    public void testEditDetails_ValidUserId() throws CustomerException {
        when(customerRepository.existsById(anyInt())).thenReturn(true);
        when(modelMapper.map(any(CustomerDto.class), any(Class.class))).thenReturn(customer);

        CustomerDto result = customerDetailsService.editDetails(customerDto);

        assertEquals(customerDto, result);
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    public void editDetails_InvalidUserId() {
        when(modelMapper.map(any(CustomerDto.class), any(Class.class))).thenReturn(customer);
        when(customerRepository.existsById(anyInt())).thenReturn(false);

        CustomerException exception = assertThrows(CustomerException.class, () -> {
            customerDetailsService.editDetails(customerDto);
        });

        assertEquals(ErrorCode.INVALID_USER_ID.name(), exception.getMessage());
    }

    @Test
    public void testChangeEmail_EmailDoNotExist() throws CustomerException {
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");

        when(customerService.isPresent(anyString())).thenReturn(false);
        doNothing().when(customerRepository).updateEmail(anyString(), anyString());

        customerDetailsService.changeEmail("new@example.com");

        verify(customerRepository, times(1)).updateEmail(anyString(), anyString());
    }

    @Test
    public void changeEmail_EmailExists() {
        when(customerService.isPresent(anyString())).thenReturn(true);

        CustomerException exception = assertThrows(CustomerException.class, () -> {
            customerDetailsService.changeEmail("existing@example.com");
        });

        assertEquals(ErrorCode.EMAIL_ALREADY_EXISTS.name(), exception.getMessage());
    }

    @Test
    public void testAddAddress() throws CustomerException {
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");

        when(modelMapper.map(any(AddressDto.class), any(Class.class))).thenReturn(address);
        when(modelMapper.map(any(Address.class), any(Class.class))).thenReturn(addressDto);
        when(customerRepository.findByEmail(anyString())).thenReturn(Optional.of(customer));
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        AddressDto result = customerDetailsService.addAddress(addressDto);

        assertEquals(addressDto, result);
        verify(addressRepository, times(1)).save(any(Address.class));
    }

    @Test
    public void testGetAddress() throws CustomerException {
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");

        when(addressRepository.findAllByUserIdEmail(anyString())).thenReturn(List.of(address));
        when(defaultAddressRepository.findById(anyString())).thenReturn(Optional.of(new DefaultAddress("test@example.com", 1)));
        when(modelMapper.map(any(Address.class), any(Class.class))).thenReturn(addressDto);

        List<AddressDto> result = customerDetailsService.getAddress();

        assertFalse(result.isEmpty());
        assertTrue(result.get(0).isDefault());
    }

    @Test
    public void testEditAddress() throws CustomerException {
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");

        when(addressRepository.existsById(anyInt())).thenReturn(true);
        when(modelMapper.map(any(AddressDto.class), any(Class.class))).thenReturn(address);

        AddressDto result = customerDetailsService.editAddress(addressDto);

        assertEquals(addressDto, result);
        verify(addressRepository, times(1)).save(any(Address.class));
    }

    @Test
    public void testDeleteAddress() throws CustomerException {
        when(addressRepository.existsById(anyInt())).thenReturn(true);
        doNothing().when(addressRepository).deleteById(anyInt());

        customerDetailsService.deleteAddress(1);

        verify(addressRepository, times(1)).deleteById(anyInt());
    }
}