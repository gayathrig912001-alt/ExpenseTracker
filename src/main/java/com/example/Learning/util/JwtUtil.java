package com.example.Learning.util;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {
	
	@Value("${secretKey}")
	private String secretKey;//= "My-Secret-Key-to-build-jwt-authentication-for-Expense-tracker-application";
	
	@Value("${expiryTimeInMs}")
	private long expiryTime;
	
	private SecretKey key;
	
	//Added post construct to create key value. by setting key without using construct means secretKey value is null because 
	//the value added in application.properties so it so set when application startup
    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    //This  method is to generate the jwt token
	public String generateToken(String userName) {
		return Jwts.builder()
				.setSubject(userName)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis()+expiryTime))
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}
	
	private Claims extractClaim(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
	public String extractUserName(String token) {
		return extractClaim(token).getSubject();
	}
	
	public boolean validateUserNameAndToken(String userName, UserDetails userDetails, String token) {
		return userName.equalsIgnoreCase(userDetails.getUsername())&& !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		return extractClaim(token).getExpiration().before(new Date());
	}
	
	/*private SecretKey key() {   //another way like method call we can get key value
		return Keys.hmacShaKeyFor(secretKey.getBytes());
	} */
}
