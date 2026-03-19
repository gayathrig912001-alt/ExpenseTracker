package com.example.Learning.util;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.Learning.Service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtTokenValidator extends OncePerRequestFilter{

	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	@Lazy
	private UserService userService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authToken = request.getHeader("Authorization");
		String token = null;
		String userName = null;
		if(!authToken.isBlank() && authToken.startsWith("Bearer ")) {
			token = authToken.substring(7);
			userName = jwtUtil.extractUserName(token);
		}
		if(!userName.isBlank() && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = userService.loadUserByUsername(userName);
			if(jwtUtil.validateUserName(userName, userDetails, token)) {
				UsernamePasswordAuthenticationToken userNamePasswordToken = new UsernamePasswordAuthenticationToken(
						userName, userDetails);
				userNamePasswordToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(userNamePasswordToken);
			}
		}
		filterChain.doFilter(request, response);
	}

	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
	    String path = request.getServletPath();
	    return path.equals("/api/login") ||
	           path.equals("/api/signin") ;
	}
}
