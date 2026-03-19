package com.example.Learning.Service;

import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.Learning.Dto.UserResponseDto;
import com.example.Learning.Dto.UserSigninDto;
import com.example.Learning.Entity.UserEntity;
import com.example.Learning.Repository.IUserRepository;

@Service
public class UserService implements UserDetailsService{

	@Autowired
	private IUserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public UserResponseDto signin(UserSigninDto userDto) {
		UserEntity userEntity = new UserEntity();
		userEntity.setUserName(userDto.getName());
		userEntity.setPhone_no(userDto.getPhone_no());
		userEntity.setEmail(userDto.getEmail());
		userEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));
		userEntity.setCreated_at(new Date());
		userEntity.setUpdated_at(new Date());
		UserEntity user = userRepository.save(userEntity);
		UserResponseDto userResponseDto = new UserResponseDto();
		BeanUtils.copyProperties(user, userResponseDto);
		return userResponseDto;
	}

	public UserResponseDto login(UserSigninDto userDto) {
		UserEntity user = userRepository.findByEmail(userDto.getEmail());
		if(user == null) {
			throw new RuntimeException("User Not Found");
		}
		if(!passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
			throw new RuntimeException("Invalid Password");
		}
		UserResponseDto userResponseDto = new UserResponseDto();
		BeanUtils.copyProperties(user, userResponseDto);
		return userResponseDto;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity user =  userRepository.findByUsername(username)
				.orElseThrow(()->new UsernameNotFoundException("User Not Found"));
		
		return org.springframework.security.core.userdetails.User
	            .builder()
	            .username(user.getEmail())
	            .password(user.getPassword())
	            .roles("USER")   // ⭐ default role
	            .build();
	}
	

}
