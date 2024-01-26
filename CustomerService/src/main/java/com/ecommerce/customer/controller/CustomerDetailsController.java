package com.ecommerce.customer.controller;

import java.security.Principal;
import java.util.List;
import com.ecommerce.customer.dto.AddressDto;
import com.ecommerce.customer.dto.CustomerDto;
import com.ecommerce.customer.entity.StringInput;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ecommerce.customer.exception.CustomerException;
import com.ecommerce.customer.security.LogoutService;
import com.ecommerce.customer.service.declaration.CustomerDetailsService;
import com.ecommerce.customer.service.declaration.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;

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

	@DeleteMapping("/delete-acc")
	@Operation(summary = "To delete user account")
	public ResponseEntity<Boolean> deleteAcc() throws CustomerException {
		return new ResponseEntity<>(customerDetailsService.deleteAcc(), HttpStatus.OK);
	}

	@PostMapping("/logout")
	@Operation(summary = "user logout", description = "Need to provide current refresh token while logging out for security reason")
	public ResponseEntity<String> logoutApi(@RequestBody @NotBlank StringInput refreshToken,
			HttpServletRequest request) {
		refreshTokenService.deleteToken(refreshToken.getInput());
		logoutService.logout(request);
		return new ResponseEntity<>(environment.getProperty("LOGGED.OUT"), HttpStatus.OK);
	}

	@PostMapping("/relogin")
	@Operation(summary = "Password verification for already logged in user")
	public ResponseEntity<Boolean> customerLoginApi(@RequestBody @NotBlank StringInput password)
			throws CustomerException {
		return new ResponseEntity<>(customerDetailsService.passwordVerify(password.getInput()), HttpStatus.OK);
	}

	@PutMapping("/password")
	@Operation(summary = "To change user password")
	public ResponseEntity<Boolean> passwordChange(@RequestBody @NotBlank StringInput password)
			throws CustomerException {
		return new ResponseEntity<>(customerDetailsService.changePassword(password.getInput()), HttpStatus.OK);
	}

	@GetMapping("/account")
	@Operation(summary = "To check user account details")
	public ResponseEntity<CustomerDto> customerDetails(Principal principal) {
		return new ResponseEntity<>(customerDetailsService.customerDetails(principal.getName()), HttpStatus.OK);
	}

	@PutMapping("/account")
	@Operation(summary = "To edit user acc details")
	public ResponseEntity<CustomerDto> editDetails(@RequestBody CustomerDto customerDto) throws CustomerException {
		return new ResponseEntity<>(customerDetailsService.editDetails(customerDto), HttpStatus.OK);
	}

	@PostMapping("/address")
	@Operation(summary = "To add user addresses")
	public ResponseEntity<AddressDto> addAddress(@RequestBody AddressDto addressDto) throws CustomerException {
		return new ResponseEntity<>(customerDetailsService.addAddress(addressDto), HttpStatus.OK);
	}

	@GetMapping("/addresses")
	@Operation(summary = "To get all user addresses for an account")
	public ResponseEntity<List<AddressDto>> getAddress() throws CustomerException {
		return new ResponseEntity<>(customerDetailsService.getAddress(), HttpStatus.OK);
	}

	@PutMapping("/address")
	@Operation(summary = "To edit user address details")
	public ResponseEntity<AddressDto> editAddress(@RequestBody AddressDto addressDto) throws CustomerException {
		return new ResponseEntity<>(customerDetailsService.editAddress(addressDto), HttpStatus.OK);
	}
}
