package com.ecommerce.customer.controller;

import java.security.Principal;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.mail.MessagingException;
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
@Tag(name = "Customer-Authentication Controller : REST APIs") // http://localhost:8500/user/swagger-ui/index.html
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


	@Operation(summary = "To check if email id is present in database")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Ëmail is present or not in DB",
					content = { @Content(mediaType = "application/json",
							schema = @Schema(implementation = boolean.class)) }
						)})
	@PostMapping("/email")
	public ResponseEntity<Boolean> customerIsPresent(@RequestBody @NotNull StringInput email) {
		
		return new ResponseEntity<>(customerService.isPresent(email.getInput()), HttpStatus.OK);
	}


	@Operation(summary = "To generate Otp for email validation")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "OTP Sent",
					content = { @Content(mediaType = "application/json",
							schema = @Schema(implementation = String.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = String.class)))
							})
	@PostMapping("/generate")
	public ResponseEntity<String> generateEmailOtp(@RequestBody @NotNull StringInput email) throws MessagingException {
		Integer otp = otpService.generateOtp(email.getInput());
		otpService.sendOtpByEmail(email.getInput(), otp.toString());
		return new ResponseEntity<>(environment.getProperty("OTP.SENT") + email.getInput(), HttpStatus.OK);
	}

	@Operation(summary = "To validate Otp for email validation")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "OTP successfully validated & User is already registered.",
					content = { @Content(mediaType = "application/json",
							schema = @Schema(implementation = String.class)) }),
			@ApiResponse(responseCode = "200", description = "OTP successfully validated but User is not registered.",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "400", description = "OTP is invalid or expired.",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = String.class))) })
	@PostMapping("/validate")
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


	@Operation(summary = "Register a new customer")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successful Registration",
					content = { @Content(mediaType = "application/json",
							schema = @Schema(implementation = JwtTokens.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid email/password format",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = String.class)))
							})
	@PostMapping("/register")
	public ResponseEntity<Object> customerRegisterApi(@Valid @RequestBody CustomerDto customerDto)
			throws CustomerException {
		customerService.registerNewCustomer(customerDto);
		if (authenticationManager
				.authenticate(
						new UsernamePasswordAuthenticationToken(customerDto.getEmail(), customerDto.getPassword()))
				.isAuthenticated()) {
			String jwtToken = jwtHelper.generateToken(customerDto.getEmail());
			String refreshToken = refreshTokenService.getRefreshToken(customerDto.getEmail());
			JwtTokens response= new JwtTokens(jwtToken, refreshToken,refreshTokenService.extractExpiration(refreshToken),customerDto.getName(),customerDto.getEmail());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(environment.getProperty("INVALID.CREDENTIAL"), HttpStatus.BAD_REQUEST);
		}
	}

	// Login with email id & password
	@Operation(summary = "Login with user Credentials")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successful login",
					content = { @Content(mediaType = "application/json",
							schema = @Schema(implementation = JwtTokens.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid email/password",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = String.class)))
							})
	@PostMapping("/login")
	public ResponseEntity<Object> customerLoginApi(@Valid @RequestBody CustomerAuthDto customerAuthDto)
			throws BadCredentialsException, CustomerException {
		if (authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(customerAuthDto.getEmail(), customerAuthDto.getPassword()))
				.isAuthenticated()) {
			String jwtToken = jwtHelper.generateToken(customerAuthDto.getEmail());
			String refreshToken = refreshTokenService.getRefreshToken(customerAuthDto.getEmail());
			JwtTokens response= new JwtTokens(jwtToken, refreshToken,refreshTokenService.extractExpiration(refreshToken),customerService.welcomeService(customerAuthDto.getEmail()),customerAuthDto.getEmail());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(environment.getProperty("INVALID.CREDENTIAL"), HttpStatus.BAD_REQUEST);
		}
	}

	// Get new jwt with refresh token
	@Operation(summary = "Get new JWT with refresh token")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",description = "Generated new JWT token refresh token",
					content = { @Content(mediaType = "application/json",
							schema = @Schema(implementation = JwtTokens.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid/Expired Token",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = String.class)))
							})
	@PostMapping("/jwt-token")
	public ResponseEntity<JwtTokens> generateNewJwt(@RequestBody StringInput refreshToken) throws CustomerException {
		String email = refreshTokenService.tokenValidation(refreshToken.getInput());
		String newRefreshtoken = refreshTokenService.getRefreshToken(email);
		JwtTokens response= new JwtTokens(jwtHelper.generateToken(email), newRefreshtoken, refreshTokenService.extractExpiration(newRefreshtoken),customerService.welcomeService(email) , email);
		refreshTokenService.deleteToken(refreshToken.getInput());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	@Operation(summary = "Welcome message for logged in user")
	@GetMapping("/welcome")
	public String welcome(Principal principal) {
		String customerName = customerService.welcomeService(principal.getName());
		return "Welcome " + customerName;
	}

	@Operation(summary = "Index unauthenticated api for testing")
	@GetMapping("/index")
	public String index() {
		return "Welcome to Ecommerce Application!";
	}
}
