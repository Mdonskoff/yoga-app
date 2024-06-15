package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Autowired
    private UserController userController;

    @Autowired
    private AuthController authController;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private final String id = "10";

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(){
        authController = new AuthController(authenticationManager, passwordEncoder, jwtUtils, userRepository);
        userService = new UserService(userRepository);
        userController = new UserController(userService, userMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    @DisplayName("Should return a user")
    void giveIdUser_thenFindUserById_shouldReturnAUserDto() throws Exception {

        User user = new User(1L, "test@test.com", "Wayne", "Bruce", "1234", false, LocalDateTime.now(), LocalDateTime.now());

        user.setEmail("test@test.com");
        user.setLastName("Wayne");
        user.setFirstName("Bruce");
        user.setId(1L);
        user.setAdmin(false);

        User savedUSer = userRepository.save(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{id}", savedUSer.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(savedUSer.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(savedUSer.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(savedUSer.getFirstName()));

    }

    @Test
    @DisplayName("Should not found user")
    void giveIdUser_thenFindUserById_shouldNotFoundUser() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }
    @Test
    @DisplayName("Should return a not found status")
    void giveIdUser_thenFindUserById_shouldReturnANotFoundStatus() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    @DisplayName("Should delete a user")
    void giveIdUser_thenFindUserById_shouldDeleteUser() throws Exception {

        User user = new User("test@test.com", "Wayne", "Bruce", passwordEncoder.encode("1234"), false);

        User newUser = userRepository.save(user);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@test.com");
        loginRequest.setPassword("1234");

        authController.authenticateUser(loginRequest);

        Long userId = newUser.getId();

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/{id}", userId))
                .andExpect(MockMvcResultMatchers.status().isOk());

        assertFalse(userRepository.existsById(userId));

    }

    @Test
    @DisplayName("Should return a unauthorized status")
    void giveIdUser_thenFindUserById_shouldReturnUnauthorizedStatus() throws Exception {

        User user = new User("test@test.com", "Wayne", "Bruce", passwordEncoder.encode("1234"), false);
        User user2 = new User("test2@test.com", "Wayne", "Bruce", passwordEncoder.encode("1234"), false);

        userRepository.save(user);
        User newUser = userRepository.save(user2);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@test.com");
        loginRequest.setPassword("1234");

        authController.authenticateUser(loginRequest);

        ResponseEntity<?> responseEntity = userController.save(newUser.getId().toString());

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Should NumberFormatException")
    void giveIdUser_shouldNumberFormatException() throws Exception {

        ResponseEntity<?> responseEntity = userController.save("a");

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Should return a bad request status")
    void giveIdUser_thenFindUserById_shouldReturnBadRequestStatus() throws Exception {

        ResponseEntity<?> responseEntity = userController.findById("a");

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @AfterEach
    public void cleanup() {
        userRepository.deleteAll();
    }
}
