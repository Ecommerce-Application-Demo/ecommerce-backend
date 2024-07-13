package com.ecommerce.customer.security;

import com.ecommerce.customer.entity.Customer;
import com.ecommerce.customer.repository.CustomerAuthRepository;
import com.ecommerce.customer.repository.CustomerRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

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
	@Autowired
	StringRedisTemplate redisTemplate;

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

	protected Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public Boolean validateToken(String token) {
		Claims claims= extractAllClaims(token);
		return (!isTokenExpired(token) && Boolean.TRUE.equals(redisTemplate.hasKey(claims.get("sessionKey").toString())));
	}

	public String generateToken(String email) {
		Map<String, Object> claims = new HashMap<>();
		Customer user=customerRepository.findByEmail(email.toLowerCase()).get();
		String sessionKey = UUID.randomUUID().toString().replace("-","") + "_"+email;
		claims.put("Name",user.getName());
		claims.put("User Id",user.getUserId());
		claims.put("sessionKey",sessionKey);
		redisTemplate.opsForValue().set(sessionKey,email.toLowerCase(),JWT_VALIDITY, TimeUnit.SECONDS);
		return createToken(claims, email);
	}

	private String createToken(Map<String, Object> claims, String email) {
		return Jwts.builder().setClaims(claims).setSubject(email.toLowerCase()).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_VALIDITY* 1000L))
				.signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
	}

	private Key getSignKey() {
		byte[] key = Decoders.BASE64.decode(JWT_SECRET);
		return Keys.hmacShaKeyFor(key);
	}
}
