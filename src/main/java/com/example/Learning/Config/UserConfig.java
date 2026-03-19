package com.example.Learning.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.Learning.Service.UserService;
import com.example.Learning.util.JwtTokenValidator;

@Configuration
@EnableWebSecurity
public class UserConfig {

	@Autowired
	public JwtTokenValidator jwtTokenValidator;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
		.csrf(csrf -> csrf.disable())
		.formLogin(form -> form.disable())
	    .httpBasic(basic -> basic.disable()) 
		.authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/signin","/api/login","/api/expense").permitAll()
                .anyRequest().authenticated());
		http.addFilterBefore(jwtTokenValidator, UsernamePasswordAuthenticationFilter.class);
		//.formLogin(Customizer.withDefaults());
		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
	
	public UserDetailsService userDetailService() {
		return new UserService();
	}
	@Bean
	public AuthenticationManager authenticationManager(UserDetailsService userDetailService,
	        PasswordEncoder passwordEncoder) throws Exception {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailService);
		provider.setPasswordEncoder(passwordEncoder);
		return new ProviderManager(provider);
	}
	
	@Bean
	public WebMvcConfigurer corsConfigurer() {
	    return new WebMvcConfigurer() {
	        @Override
	        public void addCorsMappings(CorsRegistry registry) {
	            registry.addMapping("/**")
	                    .allowedOrigins("http://localhost:3000")
	                    .allowedMethods("*")
	                    .allowedHeaders("*")
	                    .allowCredentials(true);
	        }
	    };
	}
}
