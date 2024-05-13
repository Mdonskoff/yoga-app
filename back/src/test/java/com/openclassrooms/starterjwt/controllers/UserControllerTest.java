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

    @Test //À VÉRIFIER
    @DisplayName("Should return a user")
    void giveIdUser_thenFindUserById_shouldReturnAUserDto() throws Exception {

        User user = new User(1L, "test@test.com", "Wayne", "Bruce", "1234", false, LocalDateTime.now(), LocalDateTime.now());

        user.setEmail("test@test.com");
        user.setLastName("Wayne");
        user.setFirstName("Bruce");
        user.setId(1L);
        user.setAdmin(false);

        User savedUSer = userRepository.save(user);

        /*UserDto userDto = new UserDto();
        userDto.setEmail("test@test.com");
        userDto.setFirstName("Bruce");
        userDto.setLastName("Wayne");
        userDto.setId(1L);
        userDto.setAdmin(false);

        UserDto expectedUserDto = userMapper.toDto(user);
         */


        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{id}", savedUSer.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(savedUSer.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(savedUSer.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(savedUSer.getFirstName()));

        /*
        ResponseEntity<?> responseEntity = userController.findById(String.valueOf(idUser));
        assertEquals(userDto.getEmail(), ((UserDto)responseEntity.getBody()).getEmail());
        assertEquals(userDto.getFirstName(), ((UserDto)responseEntity.getBody()).getFirstName());
        assertEquals(userDto.getLastName(), ((UserDto)responseEntity.getBody()).getLastName());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

         */
    }

    @Test
    @DisplayName("Should not found user")
    void giveIdUser_thenFindUserById_shouldNotFoundUser() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

       /* ResponseEntity<?> responseEntity = userController.save("1");
       assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode()); */

    }
    @Test
    @DisplayName("Should return a not found status")
    void giveIdUser_thenFindUserById_shouldReturnANotFoundStatus() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        /* ResponseEntity<?> responseEntity = userController.findById("1");
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode()); */

    }

    @Test
    @DisplayName("Should delete a user")
    void giveIdUser_thenFindUserById_shouldDeleteUser() throws Exception {

        User user = new User("test@test.com", "Wayne", "Bruce", passwordEncoder.encode("1234"), false);

        User newUser = userRepository.save(user);

        // Créer une demande de connexion valide
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@test.com");
        loginRequest.setPassword("1234");

        // Appeler la méthode authenticateUser() du contrôleur
        authController.authenticateUser(loginRequest);

        Long userId = newUser.getId();

        // Effectuez une demande DELETE vers la route /api/session/{id} avec l'ID de la session
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/{id}", userId))
                // Vérifiez que la réponse est un code de statut OK
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Vérifiez que la session a été supprimée de la base de données
        assertFalse(userRepository.existsById(userId));

        /*
        // Appeler la méthode de suppression appropriée sur le contrôleur UserController
        userController.save(user.getId().toString());
        // Appeler la méthode authenticateUser() du contrôleur
        ResponseEntity<?> responseEntity = authController.authenticateUser(loginRequest);
        // Vérifier que la suppression a réussi en vérifiant si l'utilisateur n'existe plus dans la base de données
        Optional<User> deletedUser = userRepository.findById(user.getId());
        assertFalse(deletedUser.isPresent());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
         */

    }

    @Test
    @DisplayName("Should return a unauthorized status")
    void giveIdUser_thenFindUserById_shouldReturnUnauthorizedStatus() throws Exception {

        User user = new User("test@test.com", "Wayne", "Bruce", passwordEncoder.encode("1234"), false);
        User user2 = new User("test2@test.com", "Wayne", "Bruce", passwordEncoder.encode("1234"), false);

        userRepository.save(user);
        User newUser = userRepository.save(user2);

        // Créer une demande de connexion valide
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@test.com");
        loginRequest.setPassword("1234");

        // Appeler la méthode authenticateUser() du contrôleur
        authController.authenticateUser(loginRequest);

        // Appeler la méthode de suppression appropriée sur le contrôleur UserController
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
