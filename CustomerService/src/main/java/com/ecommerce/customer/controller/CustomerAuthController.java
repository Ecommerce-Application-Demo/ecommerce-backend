package com.ecommerce.customer.controller;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import com.ecommerce.customer.dto.CustomerAuthDto;
import com.ecommerce.customer.dto.CustomerDto;
import com.ecommerce.customer.dto.JwtTokens;
import com.ecommerce.customer.dto.OtpDetailsDto;
import com.ecommerce.customer.entity.StringInput;
import com.ecommerce.customer.exception.CustomerException;
import com.ecommerce.customer.security.JwtHelper;
import com.ecommerce.customer.service.declaration.CustomerService;
import com.ecommerce.customer.service.declaration.RefreshTokenService;
import com.ecommerce.customer.service.declaration.OtpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
@Tag(name = "Customer Controller : REST APIs") // http://localhost:8500/ecom/swagger-ui/index.html
public class CustomerAuthController {

	@Autowired
	CustomerService customerService;
	@Autowired
	RefreshTokenService refreshTokenService;
	@Autowired
	Environment environment;
	@Autowired
	private JwtHelper jwtHelper;
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	OtpService otpService;

	@PostMapping("/email")
	@Operation(summary = "To check if email id is present in database")
	public ResponseEntity<Boolean> customerIsPresent(@RequestBody @NotNull StringInput email) {
		
		return new ResponseEntity<>(customerService.isPresent(email.getInput()), HttpStatus.OK);
	}

	@PostMapping("/generate")
	@Operation(summary = "To generate Otp for email validation")
	public ResponseEntity<String> generateEmailOtp(@RequestBody @NotNull StringInput email) {
		Integer otp = otpService.generateOtp(email.getInput());
		otpService.sendOtpByEmail(email.getInput(), otp.toString());
		return new ResponseEntity<>(environment.getProperty("OTP.SENT") + email.getInput(), HttpStatus.OK);
	}

	@PostMapping("/validate")
	@Operation(summary = "To validate Otp for email validation")
	public ResponseEntity<String> validateEmailOtp(@RequestBody OtpDetailsDto otpDetailsDto) {
		boolean validated = otpService.validateOtp(otpDetailsDto);
		if (validated) {
			if(customerService.isPresent(otpDetailsDto.getEmail())) {
				return new ResponseEntity<>(environment.getProperty("OTP.VALIDATED.REGISTERED"), HttpStatus.OK);
			}else {
				return new ResponseEntity<>(environment.getProperty("OTP.VALIDATED.NOT.REGISTERED"), HttpStatus.OK);
			}
		} else {
			return new ResponseEntity<>(environment.getProperty("OTP.INVALID"), HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/register")
	@Operation(summary = "Register a new customer")
	public ResponseEntity<Object> customerRegisterApi(@Valid @RequestBody CustomerDto customerDto)
			throws CustomerException {
		customerService.registerNewCustomer(customerDto);
		if (authenticationManager
				.authenticate(
						new UsernamePasswordAuthenticationToken(customerDto.getEmail(), customerDto.getPassword()))
				.isAuthenticated()) {
			String jwtToken = jwtHelper.generateToken(customerDto.getEmail());
			String refreshToken = refreshTokenService.getRefreshToken(customerDto.getEmail());
			JwtTokens response = new JwtTokens(jwtToken, refreshToken,customerDto.getName(),customerDto.getEmail());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(environment.getProperty("INVALID.CREDENTIAL"), HttpStatus.BAD_REQUEST);
		}
	}

	// Login with email id & password
	@PostMapping("/login")
	@Operation(summary = "Login with user Credentials")
	public ResponseEntity<Object> customerLoginApi(@Valid @RequestBody CustomerAuthDto customerAuthDto)
			throws BadCredentialsException {
		if (authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(customerAuthDto.getEmail(), customerAuthDto.getPassword()))
				.isAuthenticated()) {
			String jwtToken = jwtHelper.generateToken(customerAuthDto.getEmail());
			String refreshToken = refreshTokenService.getRefreshToken(customerAuthDto.getEmail());
			JwtTokens response= new JwtTokens(jwtToken, refreshToken,customerService.welcomeService(customerAuthDto.getEmail()),customerAuthDto.getEmail());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(environment.getProperty("INVALID.CREDENTIAL"), HttpStatus.BAD_REQUEST);
		}
	}

	// Get new jwt with refresh token
	@PostMapping("/jwt-token")
	@Operation(summary = "Get new jwt with refresh token")
	public ResponseEntity<String> customerLoginApi(@RequestBody String refreshToken) throws CustomerException {
		String email = refreshTokenService.tokenValidation(refreshToken);
		return new ResponseEntity<>(jwtHelper.generateToken(email), HttpStatus.OK);
	}
	
	

	@GetMapping("/welcome")
	public String welcome(Principal principal) {
		String customerName = customerService.welcomeService(principal.getName());
		return "Welcome " + customerName;
	}
	
	@GetMapping("/index")
	public String index() throws Exception {
		return "Welcome to Ecommerce Application!";
	}
}
