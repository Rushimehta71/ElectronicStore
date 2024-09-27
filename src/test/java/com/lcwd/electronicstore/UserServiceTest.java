package com.lcwd.electronicstore;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
//Notice this package name
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.lcwd.electronicstore.dto.PageableResponse;
import com.lcwd.electronicstore.dto.UserDto;
import com.lcwd.electronicstore.entities.User;
import com.lcwd.electronicstore.repositories.UserRepository;
import com.lcwd.electronicstore.services.UserService;
@SpringBootTest
public class UserServiceTest {


	//##Marked with the mockbean as we want a method to return a dummy data that save method is inside the  user repo.
	@MockBean
	private UserRepository userRepository;

	//    @MockBean
	//    private RoleRepository roleRepository;

	@Autowired
	private UserService userService;

	User user;
	//   Role role;

	String roleId;

	@Autowired
	private ModelMapper mapper;

	//This method will get called before each test and get ready with User object with the details.
	@BeforeEach
	public void init() {
		// role = Role.builder().roleId("abc").roleName("NORMAL").build();
		user = User.builder()
				.name("Rushikesh")
				.email("Rushikesh@gmail.com")
				.about("This is testing create method")
				.gender("Male")
				.imageName("abc.png")
				.password("lcwd")
				//  .roles(Set.of(role))
				.build();
	}


	//create user

	@Test
	public void createUserTest() {
		Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
		//  Mockito.when(roleRepository.findById(Mockito.anyString())).thenReturn(Optional.of(role));
		UserDto user1 = userService.createUser(mapper.map(user, UserDto.class));
		System.out.println(user1.getName());
		Assertions.assertNotNull(user1);
		Assertions.assertEquals("Rushikesh", user1.getName());
	}

	// update user test
	@Test
	public void updateUserTest() {
		String userId = "hosdhfosdhvo";
		UserDto userDto = UserDto.builder()
				.name("Rushikesh")
				.about("This is updated user about details")
				.gender("Male")
				.imageName("xyz.png")
				.build();
		//If any string id comes It will return the optional object of user which we will update by UserDto no need to retrieve from database
		Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
		//if any if we pass any object to it it should return user no need to retrieve from database
		//**IMP**When we will pass user id the it will return the user from int method but inside it will update that object keep this in mind.
		Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);

		UserDto updatedUser = userService.updateUser(userDto, userId);
		//      UserDto updatedUser=mapper.map(user,UserDto.class);
		System.out.println(updatedUser.getName());
		System.out.println(updatedUser.getImageName());
		Assertions.assertNotNull(userDto);
		Assertions.assertEquals(userDto.getName(), updatedUser.getName(), "Name is not validated !!");
		//multiple assertion are valid..
	}


	//delete user
	@Test
	public void deleteUserTest() throws IOException {
		String userId = "hosdhfosdhvo";
		Mockito.when(userRepository.findById("hosdhfosdhvo")).thenReturn(Optional.of(user));
		userService.deleteUser(userId);
		//Delete returns nothins so we just verify it runed once or not thats it
		Mockito.verify(userRepository,Mockito.times(1)).delete(user);
		//Add abc .xml file also so it will  not genrate exception
	}

	//Get ALL
	@Test
	public void getAllUsersTest() {
		User user1 = User.builder()
				.name("Rushikesh")
				.email("Rushikesh@gmail.com")
				.about("This is testing create method")
				.gender("Male")
				.imageName("abc.png")
				.password("lcwd")
				.build();
		User user2 = User.builder()
				.name("Rushikesh2")
				.email("Rushikesh1@gmail.com")
				.about("This is testing create method")
				.gender("Male")
				.imageName("abc.png")
				.password("lcwd")
				.build();
		List<User> userList = Arrays.asList(user, user1, user2);
		Page<User> page = new PageImpl<User>(userList);
		Mockito.when(userRepository.findAll((PageRequest) Mockito.any())).thenReturn(page);
		PageableResponse<UserDto> allUser = userService.getAllUser(1, 2, "name", "asc");
		Assertions.assertEquals(3, allUser.getContent().size());
	}

	@Test
	public void getUserByIdTest() {
		String userId = "userIdTest";
		Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		//actual call of service method
		UserDto userDto = userService.getSingleUserById(userId);
		Assertions.assertNotNull(userDto);
		Assertions.assertEquals(user.getName(), userDto.getName(), "Name not matched !!");
	}

	@Test
	public void getUserByEmailTest() {

		String emailId = "rrr@gmail.com";
		Mockito.when(userRepository.findByEmail(emailId)).thenReturn(Optional.of(user));
		UserDto userDto = userService.getSingleUserByEmail(emailId);
		Assertions.assertNotNull(userDto);
		Assertions.assertEquals(user.getEmail(), userDto.getEmail(), "Email not matched !!");
	}


	@Test
	public void searchUserTest() {

		User user1 = User.builder()
				.name("Rushikesh2 mehta")
				.email("Rushikesh9@gmail.com")
				.about("This is testing create method")
				.gender("Male")
				.imageName("abc.png")
				.password("lcwd")
				.build();

		User user2 = User.builder()
				.name("Rushikesh3 mehta")
				.email("Rushikesh7@gmail.com")
				.about("This is testing create method")
				.gender("Male")
				.imageName("abc.png")
				.password("lcwd")
				.build();

		User user3 = User.builder()
				.name("Rushikesh7 mehta")
				.email("Rushikesh6@gmail.com")
				.about("This is testing create method")
				.gender("Male")
				.imageName("abc.png")
				.password("lcwd")
				.build();

		String keywords = "mehta";
		Mockito.when(userRepository.findByNameContaining(keywords)).thenReturn(Arrays.asList(user, user1, user2, user3));
		List<UserDto> userDtos = userService.serachUserIfNameContains(keywords);
		Assertions.assertEquals(4, userDtos.size(), "size not matched !!");
	}

}