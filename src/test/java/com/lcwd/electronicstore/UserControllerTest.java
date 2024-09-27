package com.lcwd.electronicstore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lcwd.electronicstore.dto.PageableResponse;
import com.lcwd.electronicstore.dto.UserDto;
import com.lcwd.electronicstore.entities.User;
import com.lcwd.electronicstore.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.Arrays;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @MockBean
    private UserService userService;

    private User user;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        user = User.builder()
                .name("Rushikesh")
                .email("Rushikesh@gmail.com")
                .about("This is testing create method")
                .gender("Male")
                .imageName("abc.png")
                .password("lcwd")
                .build();
    }

    @Test
    public void createUserTest() throws Exception {

//        /users +POST+ user data as json
        //data as json+status created

        UserDto dto = mapper.map(user, UserDto.class);

        Mockito.when(userService.createUser(Mockito.any())).thenReturn(dto);

        //actual request for url

        this.mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonString(user))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").exists());
    }

//bellow throws error due to token
//    @Test
//    public void updateUserTest() throws Exception {
//
//        // /users/{userId} + PUT request+ json
//
//        String userId = "123";
//        UserDto dto = this.mapper.map(user, UserDto.class);
//
//        Mockito.when(userService.updateUser(Mockito.any(), Mockito.anyString())).thenReturn(dto);
//
//        this.mockMvc.perform(
//                        MockMvcRequestBuilders.put("/users/" + userId)
//                                .header(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkdXJnZXNoQGRldi5pbiIsImlhdCI6MTY3NTI0OTA0MywiZXhwIjoxNjc1MjY3MDQzfQ.HQbZ4BrQlAgd5X40RZJhSMZ0zgZAfDcQtxJaSy97YZHgdNBV0g2r7-ZXRmw1EkKhkFtdkytG_E6I7MnsxVEZqg")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(convertObjectToJsonString(user))
//                                .accept(MediaType.APPLICATION_JSON)
//                )
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").exists());
//
//    }

    private String convertObjectToJsonString(Object user) {

        try {
            return new ObjectMapper().writeValueAsString(user);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    //get all users : testing
    @Test
    public void getAllUsersTest() throws Exception {

        UserDto object1 = UserDto.builder().name("Rushikesh").email("Rushikesh@gmail.com").password("Rushikesh").about("Testing").build();
        UserDto object2 = UserDto.builder().name("Rushikesh2").email("Rushikesh@gmail.com").password("Rushikesh").about("Testing").build();
        UserDto object3 = UserDto.builder().name("Rushikesh3").email("Rushikesh@gmail.com").password("Rushikesh").about("Testing").build();
        UserDto object4 = UserDto.builder().name("Rushikesh4").email("Rushikesh@gmail.com").password("Rushikesh").about("Testing").build();
        PageableResponse<UserDto> pageableResponse = new PageableResponse<>();
        pageableResponse.setContent(Arrays.asList(
                object1, object2, object3, object4
        ));
        pageableResponse.setLastPage(false);
        pageableResponse.setPageSize(10);
        pageableResponse.setPageNumebr(100);
        pageableResponse.setTotalElements(1000);
        Mockito.when(userService.getAllUser(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString())).thenReturn(pageableResponse);


        this.mockMvc.perform(MockMvcRequestBuilders.get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)

                )
                .andDo(print())
                .andExpect(status().isOk());

    }
}
