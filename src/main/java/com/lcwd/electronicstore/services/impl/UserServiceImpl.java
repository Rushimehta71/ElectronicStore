package com.lcwd.electronicstore.services.impl;

import java.awt.print.Pageable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.lcwd.electronicstore.dto.PageableResponse;
import com.lcwd.electronicstore.dto.UserDto;
import com.lcwd.electronicstore.entities.User;
import com.lcwd.electronicstore.exceptions.ResourceNotFoundException;
import com.lcwd.electronicstore.helper.HelperUtility;
import com.lcwd.electronicstore.repositories.UserRepository;
import com.lcwd.electronicstore.services.UserService;

@Component
public class UserServiceImpl implements UserService {

	@Autowired
//	just to refer how this obje is coming this bellow is decleard in security config
//	@Bean
//	    public PasswordEncoder passwordEncoder() {
//	        return new BCryptPasswordEncoder();
//	    }
	private PasswordEncoder passwordEncoder;
	
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ModelMapper mapper;

	@Value("${user.profile.image.path}")
	private String imagePath;
	Logger logger=LoggerFactory.getLogger(UserServiceImpl.class);
	@Override
	public UserDto createUser(UserDto userDto) {
		//Genrate Unique id
		String id =UUID.randomUUID().toString();
		userDto.setUserId(id);
		//Set encoded pass
		userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
		User user =dtoToEntity(userDto);
		User savedUser=userRepository.save(user);
		UserDto returnUserDto =entityToDto(savedUser);
		return returnUserDto;
	}

	@Override
	public UserDto updateUser(UserDto userDto, String userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found With the given ID"));
		user.setAbout(userDto.getAbout());
		user.setGender(userDto.getGender());
		user.setName(userDto.getName());
		user.setImageName(userDto.getImageName());
		user.setPassword(userDto.getPassword());
		//Email we are not allowing to update
		User updatedUser = userRepository.save(user);
		UserDto returnUserDto =entityToDto(updatedUser);
		return returnUserDto;
	}

	@Override
	public void deleteUser(String userId) throws IOException {
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found With the given ID"));
		//Delete assosiated image
		String fullPath=imagePath+user.getImageName();
		Path path = Paths.get(fullPath);
		try {
			Files.delete(path);
		} catch (NoSuchFileException e) {
		logger.info("User Image Not found in folder");
			e.printStackTrace();
		}
		userRepository.delete(user);
	}

	//Page
	//Page number start with 0
	@Override
	public PageableResponse<UserDto> getAllUser(int pageNumber,int pageSize,String sortBy, String sortDir) {
		//If dsc then do it by dsc name or do it by asc and name
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		PageRequest pageable=PageRequest.of(pageNumber, pageSize,sort);
		Page<User> userListPage=userRepository.findAll(pageable);
		//Below method is created by us to get all page related details
		PageableResponse<UserDto> response=HelperUtility.getPageableResponse(userListPage, UserDto.class);
		return response;
	}

	@Override
	public UserDto getSingleUserById(String userId) {
		User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found with given id"));
		UserDto userDto=entityToDto(user);
		return userDto;
	}

	@Override
	public UserDto getSingleUserByEmail(String email) {
		User user = userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User by Email not found"));
		return entityToDto(user);
	}

	@Override
	public List<UserDto> serachUserIfNameContains(String keyword) {
		List<User> userList =	userRepository.findByNameContaining(keyword);
		List<UserDto>  userDtoList = userList.stream().map(user ->entityToDto(user)).collect(Collectors.toList());
		return userDtoList;
	}

	private UserDto entityToDto(User savedUser) {
		//We can do this conversion as below manually
		//		UserDto userDto = UserDto.builder()
		//				.userId(savedUser.getUserId())
		//				.about(savedUser.getAbout())
		//				.email(savedUser.getEmail())
		//				.gender(savedUser.getGender())
		//				.imageName(savedUser.getImageName()) 
		//				.name(savedUser.getName())
		//				.password(savedUser.getPassword())
		//				.build();
		return mapper.map(savedUser, UserDto.class);
	}

	private User dtoToEntity(UserDto userDto) {
		//We can do this conversion as below manually
		//		User user = User.builder()
		//				.userId(userDto.getUserId())
		//				.about(userDto.getAbout())
		//				.email(userDto.getEmail())
		//				.gender(userDto.getGender())
		//				.imageName(userDto.getImageName())
		//				.name(userDto.getName())
		//				.password(userDto.getPassword())
		//				.build();
		return mapper.map(userDto, User.class);
	}
}
