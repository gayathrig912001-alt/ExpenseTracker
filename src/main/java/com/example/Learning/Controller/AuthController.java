package com.example.Learning.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.Learning.Dto.JwtTokenDto;
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
	public ResponseEntity<UserResponseDto> signinUser(@RequestBody UserSigninDto user) {
		UserResponseDto userResponseDto = userService.signin(user);
		return ResponseEntity.ok(userResponseDto);
	}
	
	@PostMapping("/api/login")
	public ResponseEntity<JwtTokenDto> loginUser(@RequestBody UserSigninDto user) {
		try {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
				(user.getEmail(), user.getPassword()));
		}catch(Exception e) {
			throw e;
		}
		String token = jwtUtil.generateToken(user.getEmail());
		UserResponseDto userResponseDto = new UserResponseDto();
		userResponseDto.setName(user.getEmail());
		JwtTokenDto tokenDto = new JwtTokenDto(HttpStatus.OK.getReasonPhrase(), token, userResponseDto);
		return ResponseEntity.ok(tokenDto);
	}
}
