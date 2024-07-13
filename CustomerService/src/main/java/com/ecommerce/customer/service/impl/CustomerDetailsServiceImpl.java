package com.ecommerce.customer.service.impl;

import com.ecommerce.customer.dto.AddressDto;
import com.ecommerce.customer.dto.CustomerDto;
import com.ecommerce.customer.entity.Address;
import com.ecommerce.customer.entity.Customer;
import com.ecommerce.customer.entity.CustomerAuth;
import com.ecommerce.customer.entity.DefaultAddress;
import com.ecommerce.customer.exception.CustomerException;
import com.ecommerce.customer.exception.ErrorCode;
import com.ecommerce.customer.repository.*;
import com.ecommerce.customer.service.declaration.CustomerDetailsService;
import com.ecommerce.customer.service.declaration.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class CustomerDetailsServiceImpl implements CustomerDetailsService {

    @Value("${PASSWORD.UPDATE.SUCCESS}")
    String passwordSuccessMessage;

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    CustomerAuthRepository customerAuthRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    DefaultAddressRepository defaultAddressRepository;
    @Autowired
    CustomerService customerService;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public String getUser() throws CustomerException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return authentication.getName();
        } else {
            throw new CustomerException(ErrorCode.USER_NOT_FOUND.name());
        }
    }

    @Override
    public CustomerDto customerDetails(String email) {
        Customer customer = customerRepository.findByEmail(email.toLowerCase()).get();
        return modelMapper.map(customer, CustomerDto.class);
    }

    @Override
    public Boolean passwordVerify(String password) throws CustomerException {
        String dbPassword = customerAuthRepository.findById(getUser()).get().getPassword();
        return passwordEncoder.matches(password, dbPassword);
    }

    @Override
    @Transactional
    public String changePassword(String password) throws CustomerException {
        try {
            CustomerAuth customer = customerAuthRepository.findById(getUser()).get();
            customer.setPassword(passwordEncoder.encode(password));
            customerAuthRepository.save(customer);
            invalidateAllToken();
            return passwordSuccessMessage;
        } catch (Exception e) {
            throw new CustomerException(ErrorCode.PASSWORD_UPDATE_ERROR.name());
        }
    }

    @Override
    public Boolean invalidateAllToken() throws CustomerException {
        String user = getUser().toLowerCase();
        Set<String> keys = redisTemplate.keys("*_" + user);

        if (keys != null) {
            for (String key : keys) {
                String value = redisTemplate.opsForValue().get(key).toString();
                if (user.equals(value)) {
                    redisTemplate.delete(key);
                }
            }
        }
        refreshTokenRepository.deleteByEmail(user);
        return true;
    }

    @Override
    public Boolean deleteAcc() throws CustomerException {
        customerRepository.deleteAccount(getUser());
        return true;
    }

    @Override
    public CustomerDto editDetails(CustomerDto customerDto) throws CustomerException {
        Customer customer = modelMapper.map(customerDto, Customer.class);
        if (customerRepository.existsById(customer.getUserId())) {
            customer.setEmail(customer.getEmail().toLowerCase());
            customerRepository.save(customer);
            return customerDto;
        } else {
            throw new CustomerException(ErrorCode.INVALID_USER_ID.name());
        }
    }

    @Override
    public void changeEmail(String email) throws CustomerException {
       if(!customerService.isPresent(email)) {
           invalidateAllToken();
           customerRepository.updateEmail(getUser(), email.toLowerCase());
       }else{
           throw new CustomerException(ErrorCode.EMAIL_ALREADY_EXISTS.name());
       }
    }

    @Override
    public AddressDto addAddress(AddressDto addressDto) throws CustomerException {
        Address address = modelMapper.map(addressDto, Address.class);
        String user = getUser();
        address.setUserIdEmail(user);
        address.setAddCustomer(customerRepository.findByEmail(user).get());
        address = addressRepository.save(address);
        if (addressDto.isDefault() || defaultAddressRepository.findById(user).isEmpty()) {
            DefaultAddress add = new DefaultAddress(user, address.getAddId());
            defaultAddressRepository.save(add);
        }

        return modelMapper.map(address, AddressDto.class);
    }

    @Override
    public List<AddressDto> getAddress() throws CustomerException {
        String user = getUser();
        List<Address> address = addressRepository.findAllByUserIdEmail(user);
        List<AddressDto> addDto = new ArrayList<>();
        if (address.isEmpty())
            return addDto;
        int id = defaultAddressRepository.findById(user).orElseGet(DefaultAddress::new).getAddId();
        addDto = address.stream()
                .map(add -> modelMapper.map(add, AddressDto.class))
                .peek(add2 -> {
                    if (add2.getAddId() == id) {
                        add2.setDefault(true);
                    }
                }).toList();
        return addDto;
    }

    @Override
    public AddressDto editAddress(AddressDto addressDto) throws CustomerException {
        Address address = modelMapper.map(addressDto, Address.class);
        if (addressRepository.existsById((address.getAddId()))) {
            address.setUserIdEmail(getUser());
            addressRepository.save(address);
            if (addressDto.isDefault()) {
                DefaultAddress add = new DefaultAddress(getUser(), address.getAddId());
                defaultAddressRepository.save(add);
            }
            return addressDto;
        } else {
            throw new CustomerException(ErrorCode.INVALID_ADDRESS_ID.name());
        }
    }

    @Override
    public void deleteAddress(int addId) throws CustomerException {
        if (addressRepository.existsById(addId)) {
            addressRepository.deleteById(addId);
        } else {
            throw new CustomerException(ErrorCode.INVALID_ADDRESS_ID.name());
        }
    }

}
