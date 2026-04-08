package com.example.Learning.Dto;

public class JwtTokenDto {

	private String token;
	
	private String message;
	
	private UserResponseDto userResponseDto;

	public JwtTokenDto(String message, String token, UserResponseDto userResponseDto) {
		super();
		this.token = token;
		this.message = message;
		this.userResponseDto = userResponseDto;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public UserResponseDto getUserResponseDto() {
		return userResponseDto;
	}

	public void setUserResponseDto(UserResponseDto userResponseDto) {
		this.userResponseDto = userResponseDto;
	}
	
	
}
