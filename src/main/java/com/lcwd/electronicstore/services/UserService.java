package com.lcwd.electronicstore.services;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;

import com.lcwd.electronicstore.dto.PageableResponse;
import com.lcwd.electronicstore.dto.UserDto;

public interface UserService {
	  
	  //Create
	  UserDto createUser(UserDto userDto);
	  
	  //Update
	  UserDto updateUser(UserDto userDto,String ID);
	  
	  //delete
	  void deleteUser(String userId) throws IOException;
	  
	  //Get all Users
	  PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir);
	  
	  //get Single user by id
	  UserDto getSingleUserById(String userId);
	  
	  //get single user byemail
	  UserDto getSingleUserByEmail(String email);
	  
	  //search user
	  List<UserDto> serachUserIfNameContains(String keyword);
}
