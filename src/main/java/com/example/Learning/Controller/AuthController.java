package com.example.Learning.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.Learning.Dto.UserResponseDto;
import com.example.Learning.Dto.UserSigninDto;
import com.example.Learning.Service.UserService;
import com.example.Learning.util.JwtUtil;

@RestController
public class AuthController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@PostMapping("/api/signin")
	public UserResponseDto signinUser(@RequestBody UserSigninDto user) {
		return userService.signin(user);
	}
	
	@PostMapping("/api/login")
	public String loginUser(@RequestBody UserSigninDto user) {
		try {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
				(user.getName(), user.getPassword()));
		}catch(Exception e) {
			throw e;
		}
		return jwtUtil.generateToken(user.getName());
	}
}
