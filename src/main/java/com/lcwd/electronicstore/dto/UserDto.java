package com.lcwd.electronicstore.dto;

import java.util.HashSet;
import java.util.Set;

import com.lcwd.electronicstore.entities.Role;
import com.lcwd.electronicstore.validator.ImageNameValid;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
	
	private String userId;
	@Size(min=3,max=25,message="Invalid Name Size")
    @Schema( name = "username", accessMode = Schema.AccessMode.READ_ONLY, description = "user name of new user !!")
	private String name;
	
	//@Pattern(regexp = "",message="InValid User Email")
	@NotBlank(message = "InValid User Email")
	private String email;
	
	@NotBlank(message = "Password Is Required")
	private String password;
	
	@Size(min=4,max=12,message = "Gender Size Is invalid")
	private String gender;
	
	@NotBlank(message = "Please writr Something about yourself")
	private String about;
	
	//@Pattern 
	//@Custom Validator
	
	@ImageNameValid
	private String imageName;
	
	//private Set<RoleDto> roles = new HashSet<>();
}
