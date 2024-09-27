package com.lcwd.electronicstore.controllers;

import java.security.Principal;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lcwd.electronicstore.dto.JwtRequest;
import com.lcwd.electronicstore.dto.JwtResponse;
import com.lcwd.electronicstore.dto.UserDto;
import com.lcwd.electronicstore.exceptions.BadApiRequestException;
import com.lcwd.electronicstore.security.JwtHelper;


@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	JwtHelper helper;

	//http://localhost:8080/auth/current
	@GetMapping("/current")
	public ResponseEntity<UserDto> getCurruntUser(Principal principle){
		String name=principle.getName();
		return new ResponseEntity<>(modelMapper.map( userDetailsService.loadUserByUsername(name),UserDto.class ) ,HttpStatus.OK);
	}

	@PostMapping("/login")
	public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {

//		When you implement your custom user class and it implements the UserDetails interface, you provide the necessary methods to Spring Security for authentication and authorization purposes.
//		Here's the typical workflow:
//		1. **Implement UserDetails Interface**: Your custom user class should implement the UserDetails interface, providing implementations for methods like getUsername(), getPassword(), and getAuthorities().
//		2. **Store User Details**: Store user details, including the username and password (after encryption), in your database.
//		3. **Authentication**: When a user attempts to log in, you typically receive their username and password. You pass these credentials to Spring Security's authentication mechanism.
//		4. **AuthenticationProvider**: Spring Security's AuthenticationProvider, such as DaoAuthenticationProvider, retrieves the user details using the configured UserDetailsService. It compares the provided password with the stored (encrypted) password retrieved from the database.
//		5. **Successful Authentication**: If the passwords match, authentication succeeds, and you can proceed with allowing the user access to protected resources.
//		6. **Failed Authentication**: If the passwords do not match or the user is not found, authentication fails, and you handle the failure accordingly (e.g., display an error message to the user).
//		So, in essence, you implement your custom user class, store user details securely, and let Spring Security handle the authentication process by comparing the provided credentials with the stored credentials retrieved from your database.
		//below method is just to Authenticate id and password are correct or not spring security if yes it will generate the token in next steps
		
		this.doAuthenticate(request.getEmail(), request.getPassword());
		//userDetailsService this is just interface so dont forget to impliment it and return UserDetails of spring class
		UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
		String token = this.helper.generateToken(userDetails);
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);
		JwtResponse response = JwtResponse.builder()
				.jwtToken(token)
				.user(userDto).build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	private void doAuthenticate(String email, String password) {

		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
		try {
			authenticationManager.authenticate(authentication);
		} catch (BadCredentialsException e) {
			throw new BadApiRequestException(" Invalid Username or Password  !!");
		}

	}


}
