package com.ecommerce.customer.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.ecommerce.customer.entity.Customer;
import com.ecommerce.customer.entity.CustomerAuth;
import com.ecommerce.customer.repository.CustomerAuthRepository;
import com.ecommerce.customer.repository.CustomerRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.stereotype.Component;

@Component
public class JwtHelper {
	
	@Value("#{new Integer(${JWT_VALIDITY})}")
	public int JWT_VALIDITY;
	
	@Value("${JWT_SECRET}")
	public String JWT_SECRET;

	@Autowired
	CustomerRepository customerRepository;
	@Autowired
	CustomerAuthRepository customerAuthRepository;

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public String extractUserName(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUserName(token);
		CustomerAuth user=customerAuthRepository.findByEmail(username).get();
		if(!user.getIsEnabled())
			throw new  DisabledException("User is disabled");
		Claims claims= extractAllClaims(token);
		if(!claims.containsValue(user.getLoginSalt()))
			throw new ExpiredJwtException(null,null,"Provided JWT is expired");
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	public String generateToken(String email) {
		Map<String, Object> claims = new HashMap<>();
		Customer user=customerRepository.findByEmail(email.toLowerCase()).get();
		claims.put("Name",user.getName());
		claims.put("User Id",user.getUserId());
		claims.put("validationKey",customerAuthRepository.findByEmail(email).get().getLoginSalt());
		return createToken(claims, email);
	}

	private String createToken(Map<String, Object> claims, String email) {
		return Jwts.builder().setClaims(claims).setSubject(email.toLowerCase()).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_VALIDITY))
				.signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
	}

	private Key getSignKey() {
		byte[] key = Decoders.BASE64.decode(JWT_SECRET);
		return Keys.hmacShaKeyFor(key);
	}
}
