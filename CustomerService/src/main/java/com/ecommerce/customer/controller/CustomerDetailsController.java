package com.ecommerce.customer.controller;

import com.ecommerce.customer.dto.AddressDto;
import com.ecommerce.customer.dto.CustomerDto;
import com.ecommerce.customer.entity.StringInput;
import com.ecommerce.customer.exception.CustomerException;
import com.ecommerce.customer.exception.ErrorResponse;
import com.ecommerce.customer.security.LogoutService;
import com.ecommerce.customer.service.declaration.CustomerDetailsService;
import com.ecommerce.customer.service.declaration.OtpService;
import com.ecommerce.customer.service.declaration.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/my")
@Tag(name = "Customer Details Controller : REST APIs")
public class CustomerDetailsController {

	@Autowired
	CustomerDetailsService customerDetailsService;
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	RefreshTokenService refreshTokenService;
	@Autowired
	Environment environment;
	@Autowired
	LogoutService logoutService;
	@Autowired
	OtpService otpService;

	@DeleteMapping("/delete-acc")
	@Operation(summary = "To delete user account")
	public ResponseEntity<Boolean> deleteAcc() throws CustomerException {
		return new ResponseEntity<>(customerDetailsService.deleteAcc(), HttpStatus.OK);
	}

	@Operation(summary = "user logout", description = "Need to provide current refresh token while logging out for security reason")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successful logout",
					content = { @Content(mediaType = "application/json",
							schema = @Schema(implementation = String.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid/expired Refresh token",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ErrorResponse.class)))
							})
	@PostMapping("/logout")
	public ResponseEntity<String> logoutApi(@RequestBody @NotNull StringInput refreshToken,
			HttpServletRequest request) throws CustomerException {
		refreshTokenService.deleteToken(refreshToken.getInput());
		logoutService.logout(request);
		return new ResponseEntity<>(environment.getProperty("LOGGED.OUT"), HttpStatus.OK);
	}

	@Operation(summary = "Invalidate all JWTs & Refresh Tokens")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Invalidated",
					content = { @Content(mediaType = "application/json",
							schema = @Schema(implementation = boolean.class)) })
	})
	@GetMapping("/logout/allDevice")
	public ResponseEntity<Boolean> invalidateAllToken() throws CustomerException {
		return new ResponseEntity<>(customerDetailsService.invalidateAllToken(), HttpStatus.OK);
	}

	@Operation(summary = "Password verification for already logged in user")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successful login",
					content = { @Content(mediaType = "application/json",
							schema = @Schema(implementation = boolean.class)) })
							})
	@PostMapping("/relogin")
	public ResponseEntity<Boolean> customerPasswordValidation(@RequestBody @NotNull StringInput password)
			throws CustomerException {
		return new ResponseEntity<>(customerDetailsService.passwordVerify(password.getInput()), HttpStatus.OK);
	}

	@PutMapping("/password")
	@Operation(summary = "To change user password")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "true",
					content = { @Content(mediaType = "application/json",
							schema = @Schema(implementation = String.class)) })
	})
	public ResponseEntity<String> passwordChange(@RequestBody @NotNull StringInput password)
			throws CustomerException {
		return new ResponseEntity<>(customerDetailsService.changePassword(password.getInput()), HttpStatus.OK);
	}

	@Operation(summary = "To check user account details")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Logged in user details",
					content = { @Content(mediaType = "application/json",
							schema = @Schema(implementation = CustomerDto.class)) })
	})
	@GetMapping("/account")
	public ResponseEntity<CustomerDto> customerDetails(Principal principal) {
		return new ResponseEntity<>(customerDetailsService.customerDetails(principal.getName()), HttpStatus.OK);
	}

	@Operation(summary = "To edit user acc details")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Edited user details",
					content = { @Content(mediaType = "application/json",
							schema = @Schema(implementation = CustomerDto.class)) })
	})
	@PutMapping("/account")
	public ResponseEntity<CustomerDto> editDetails(@RequestBody CustomerDto customerDto) throws CustomerException {
		return new ResponseEntity<>(customerDetailsService.editDetails(customerDto), HttpStatus.OK);
	}

	@Operation(summary = "To generate Otp for email validation")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "OTP Sent",
					content = { @Content(mediaType = "application/json",
							schema = @Schema(implementation = String.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ErrorResponse.class)))
	})
	@PostMapping("/generate")
	public ResponseEntity<String> generateEmailOtp(@RequestBody @NotNull StringInput email) throws MessagingException {
		Integer otp = otpService.generateOtp(email.getInput());
		//otpService.sendOtpByEmail(email.getInput(), otp.toString());
		return new ResponseEntity<>(environment.getProperty("OTP.SENT") + email.getInput(), HttpStatus.OK);
	}

	
	@Operation(summary = "To edit user email")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Email changed",
					content = { @Content(mediaType = "application/json",
							schema = @Schema(implementation = String.class))}),
			@ApiResponse(responseCode = "422", description = "Invalid id supplied",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ErrorResponse.class)))
	})
	@PutMapping("/account/email")
	public ResponseEntity<String> editEmail(@RequestParam(name = "newEmail") @NotNull String newEmail)
			throws CustomerException {
		customerDetailsService.changeEmail(newEmail);
		return new ResponseEntity<>(environment.getProperty("EMAIL.CHANGED"),HttpStatus.OK);
	}

	@Operation(summary = "To add user addresses")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Address added",
					content = { @Content(mediaType = "application/json",
							schema = @Schema(implementation = AddressDto.class)) })
	})
	@PostMapping("/address")
	public ResponseEntity<AddressDto> addAddress(@RequestBody @Valid AddressDto addressDto) throws CustomerException {
		return new ResponseEntity<>(customerDetailsService.addAddress(addressDto), HttpStatus.OK);
	}

	@Operation(summary = "To get all user addresses for an account")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "All address of user",
					content = { @Content(mediaType = "application/json",
							schema = @Schema(implementation = AddressDto.class)) })
	})
	@GetMapping("/addresses")
	public ResponseEntity<List<AddressDto>> getAddress() throws CustomerException {
		return new ResponseEntity<>(customerDetailsService.getAddress(), HttpStatus.OK);
	}

	@Operation(summary = "To edit user address details")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Address edited",
					content = { @Content(mediaType = "application/json",
							schema = @Schema(implementation = AddressDto.class)) }),
			@ApiResponse(responseCode = "404", description = "Address Id Not Found",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ErrorResponse.class)))
	})
	@PutMapping("/address")
	public ResponseEntity<AddressDto> editAddress(@RequestBody @Valid AddressDto addressDto) throws CustomerException {
		return new ResponseEntity<>(customerDetailsService.editAddress(addressDto), HttpStatus.OK);
	}
	
	@Operation(summary = "To delete user address details")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Address deleted",
					content = { @Content(mediaType = "application/json",
							schema = @Schema(implementation = String.class)) }),
			@ApiResponse(responseCode = "404", description = "Address Id Not Found",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ErrorResponse.class)))
	})
	@DeleteMapping("/address/{addId}")
	public ResponseEntity<String> deleteAddress(@PathVariable("addId") @NotNull String addId) throws CustomerException {
		customerDetailsService.deleteAddress(Integer.parseInt(addId));
		return new ResponseEntity<>(environment.getProperty("ADDRESS.DELETED"), HttpStatus.OK);
	}
}
