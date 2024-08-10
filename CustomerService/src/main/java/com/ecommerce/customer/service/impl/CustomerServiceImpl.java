package com.ecommerce.customer.service.impl;

import com.ecommerce.customer.dto.CustomerDto;
import com.ecommerce.customer.entity.Customer;
import com.ecommerce.customer.entity.CustomerAuth;
import com.ecommerce.customer.exception.CustomerException;
import com.ecommerce.customer.exception.ErrorCode;
import com.ecommerce.customer.repository.CustomerAuthRepository;
import com.ecommerce.customer.repository.CustomerRepository;
import com.ecommerce.customer.repository.RefreshTokenRepository;
import com.ecommerce.customer.service.declaration.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

	@Value("${PASSWORD.UPDATE.SUCCESS}")
	public String passwordSuccessMessage;

	@Autowired
    CustomerRepository customerRepository;
	@Autowired
    CustomerAuthRepository customerAuthRepository;
	@Autowired
	ModelMapper modelMapper;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	RefreshTokenRepository refreshTokenRepository;
	@Autowired
	StringRedisTemplate redisTemplate;

	@Override
	public void registerNewCustomer(CustomerDto customerDto) throws CustomerException {

		boolean registeredEmail =customerRepository.findByEmail(customerDto.getEmail()).isEmpty();

		if (registeredEmail) {
			Customer customer = modelMapper.map(customerDto, Customer.class);
			customer.setEmail(customer.getEmail().toLowerCase());
			customerRepository.save(customer);
			CustomerAuth customerAuth = new CustomerAuth();
			customerAuth.setEmail(customer.getEmail().toLowerCase());
			customerAuth.setPassword(passwordEncoder.encode(customer.getPassword()));
			customerAuth.setAuthCustomer(customer);
			customerAuthRepository.save(customerAuth);
		} else {
			throw new CustomerException(ErrorCode.EMAIL_ALREADY_EXISTS.name());
		}

	}

	@Override
	public String welcomeService(String email) {
		Customer customer = customerRepository.findByEmail(email.toLowerCase()).orElseThrow();
		return customer.getName();
	}

	@Override
	public Boolean isPresent(String email) {
		return customerRepository.findByEmail(email.toLowerCase()).isPresent();
	}

	@Override
	public String forgetpassword(String email, String password) throws CustomerException {
		try {
			CustomerAuth customer = customerAuthRepository.findById(email.toLowerCase()).get();
			customer.setPassword(passwordEncoder.encode(password));
			customerAuthRepository.save(customer);
			Set<String> keys = redisTemplate.keys("*_" + email);

			if (keys != null) {
				for (String key : keys) {
					String value = redisTemplate.opsForValue().get(key).toString();
					if (email.equals(value)) {
						redisTemplate.delete(key);
					}
				}
			}
			refreshTokenRepository.deleteByEmail(email.toLowerCase());
			return passwordSuccessMessage;
		} catch (Exception e) {
			throw new CustomerException(ErrorCode.PASSWORD_UPDATE_ERROR.name());
		}
	}

	@Override
	public void redisKeepAlive() {
		redisTemplate.hasKey("a");

	}
}
