package com.lcwd.electronicstore.controllers;

import java.io.IOException;


import java.io.InputStream;
import java.util.List;


import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lcwd.electronicstore.dto.ApiResponseMessage;
import com.lcwd.electronicstore.dto.ImageResponse;
import com.lcwd.electronicstore.dto.PageableResponse;
import com.lcwd.electronicstore.dto.UserDto;
import com.lcwd.electronicstore.exceptions.GlobalExceptionHandler;
import com.lcwd.electronicstore.services.FileService;
import com.lcwd.electronicstore.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.responses.*;

@RestController
@RequestMapping("/users")
@Tag(name = "User Controller",description = "These are  User APIs")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private FileService fileService;

	@Value("${user.profile.image.path}")
	private String imageUploadPath;

	Logger logger=LoggerFactory.getLogger(UserController.class);

	@PostMapping()
	@Operation(summary = "Post new user")
	//create
	//@Valid is important for turn on validation in cto class if user
	public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){
		UserDto userDto1=userService.createUser(userDto);
		return new ResponseEntity<>(userDto1,HttpStatus.CREATED);
	}

	@PutMapping("/{userId}")
	@Operation(summary = "update exsting user byb userid")
	//update
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Success | OK"),
			@ApiResponse(responseCode = "401", description = "not authorized !!"),
			@ApiResponse(responseCode = "201", description = "new user created !!")
	})
	public ResponseEntity<UserDto> updateUser(@PathVariable("userId") String userId,@Valid @RequestBody UserDto userDtoForUpdate){
		UserDto updatedUserDto=userService.updateUser(userDtoForUpdate,userId);
		return new ResponseEntity<>(updatedUserDto,HttpStatus.OK);
	}

	@DeleteMapping("/{userId}")
	@Operation(summary = "delete exsting user by userId")
	//delete
	public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable("userId") String userId) throws IOException{
		userService.deleteUser(userId);
		ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
				.message("User got deleted")
				.success(true)
				.status(HttpStatus.OK)
				.build();
		return new ResponseEntity<>(apiResponseMessage,HttpStatus.OK);
	}

	@GetMapping
	@Operation(summary = "get all exsting users")
	//getall
	//Pagination
	//http://localhost:8080/users?pageNumber=0&pageSize=10&sortBy=name&sortDir=asc
	//http://localhost:8080/users?pageNumber=0&pageSize=10&sortBy=name&sortDir=desc
	public ResponseEntity<PageableResponse<UserDto>> getAllUser(
			@RequestParam(value="pageNumber",defaultValue = "0",required = false)int pageNumber,
			@RequestParam(value="pageSize",defaultValue = "10",required = false)int pageSize,
			@RequestParam(value="sortBy",defaultValue = "name",required = false)String sortBy,
			@RequestParam(value="sortDir",defaultValue = "asc",required = false)String sortDir
			){
		return new ResponseEntity<>(userService.getAllUser(pageNumber,pageSize,sortBy,sortDir),HttpStatus.OK);
	}

	//	@GetMapping("/{userId}")
	//	  @Operation(summary = "get exsting user by userId")
	//	//get
	//	public ResponseEntity<UserDto> getAllUser(@PathVariable("userId") String userId){
	//		return new ResponseEntity<>(userService.getSingleUserById(userId),HttpStatus.OK);
	//	}

	@GetMapping("/email/{email}")
	@Operation(summary = "get exsting user by email")
	//getallbyEmail
	public ResponseEntity<UserDto> getUserByEmail(@PathVariable("email") String email){
		return new ResponseEntity<>(userService.getSingleUserByEmail(email),HttpStatus.OK);
	}

	@GetMapping("/search/{keyword}")
	@Operation(summary = "get exsting user by contain in name")
	//searchUser
	public ResponseEntity<List<UserDto>> getUserByKeyword(@PathVariable("keyword") String keyword){
		return new ResponseEntity<>(userService.serachUserIfNameContains(keyword),HttpStatus.OK);
	}

	@PostMapping("/image/{userId}")
	@Operation(summary = "upload image")
	//upload user image
	public ResponseEntity<ImageResponse> uploadImage(@RequestParam("userImage") MultipartFile image,@PathVariable String userId) throws IOException{
		String imageName =fileService.uploadImage(image, imageUploadPath);
		UserDto user =userService.getSingleUserById(userId);
		user.setImageName(imageName);
		userService.updateUser(user,userId);
		ImageResponse imageResponse=ImageResponse.builder().imageName(imageName).success(true).status(HttpStatus.CREATED).build();
		return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);

	}

	@GetMapping("/image/{userId}")
	@Operation(summary = "serve image")
	//serve user image
	public void serveUserImage(@PathVariable("userId") String userId,HttpServletResponse response) throws IOException {
		UserDto user=userService.getSingleUserById(userId);
		logger.info("User image name :  {}",user.getImageName());
		InputStream resource =fileService.getResource(imageUploadPath, user.getImageName());
		response.setContentType(MediaType.IMAGE_PNG_VALUE);
		StreamUtils.copy(resource,response.getOutputStream());
	}
}
