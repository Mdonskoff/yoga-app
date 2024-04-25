package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AuthControllerIT {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthController authController;

    private SignupRequest signupRequest;


    @BeforeEach
    void setUp() {
        authController = new AuthController(authenticationManager, passwordEncoder, jwtUtils, userRepository);
    }
    @Test
    @DisplayName("Should login")
    void giveLoginRequest_thenAuthenticate_shouldLogin() {
        // Créer un utilisateur dans la base de données pour simuler l'utilisateur existant
        User user = new User("test@example.com", "Doe", "John", passwordEncoder.encode("1234"), false);
        userRepository.save(user);

        // Créer une demande de connexion valide
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("1234");

        // Appeler la méthode authenticateUser() du contrôleur
        ResponseEntity<?> responseEntity = authController.authenticateUser(loginRequest);

        // Vérifier que la réponse est conforme aux attentes
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        JwtResponse jwtResponse = (JwtResponse) responseEntity.getBody();
        assertEquals("test@example.com", jwtResponse.getUsername());
        assertNotNull(jwtResponse.getToken());
    }

    @Test
    @DisplayName("Should register but email already exists")
    void giveSignupRequest_thenRegister_shouldReturnBadRequest() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password");

        // Ajouter un utilisateur avec le même email pour simuler un email déjà pris
        User existingUser = new User(signupRequest.getEmail(), "Doe", "John", "password", false);
        userRepository.save(existingUser);

        ResponseEntity<?> responseEntity = authController.registerUser(signupRequest);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Error: Email is already taken!", ((MessageResponse) responseEntity.getBody()).getMessage());
    }

    @Test
    @DisplayName("Should register")
    void giveSignupRequest_thenRegister_shouldRegister() {
        signupRequest = new SignupRequest();
        signupRequest.setEmail("test1@test.com");
        signupRequest.setLastName("lastName");
        signupRequest.setFirstName("firstName");
        signupRequest.setPassword("password");

        // Supprimez l'utilisateur s'il existe déjà pour éviter les erreurs lors de l'exécution du test
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            userRepository.deleteByEmail(signupRequest.getEmail());
        }

        ResponseEntity<?> responseEntity = authController.registerUser(signupRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("User registered successfully!", ((MessageResponse) responseEntity.getBody()).getMessage());
    }

    // Supprimer tous les enregistrements après chaque test
    @AfterEach
    public void cleanup() {
        userRepository.deleteAll();
    }






    // Tests avec les mocks
    /*
    @Test
    @DisplayName("Should login")
    void giveLoginRequest_thenAuthenticate_shouldLogin() throws Exception {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@test.com");
        loginRequest.setPassword("test");
        User user = new User();
        user.setEmail("test@test.com");
        user.setAdmin(false);

        UserDetailsImpl userDetails = new UserDetailsImpl(1L,
                "test@test.com", "John","Doe", false, "password");
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                loginRequest.getPassword()))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userRepository.findByEmail(userDetails.getUsername())).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").exists());

        verify(userRepository).findByEmail(userDetails.getUsername());
    }
     */

    /*
    @Test
    @DisplayName("Should register but email already exists")
    void giveSignupRequest_thenRegister_shouldReturnBadRequest() throws Exception{

        MessageResponse messageResponse = new MessageResponse("Error: Email is already taken!");

        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(messageResponse.getMessage()));

        verify(userRepository).existsByEmail(signupRequest.getEmail());
    }
     */
/*
    @Test
    @DisplayName("Should register")
    void giveSignupRequest_thenRegister_shouldRegister() throws Exception{

        MessageResponse messageResponse = new MessageResponse("User registered successfully!");

        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(messageResponse.getMessage()));

        verify(userRepository).existsByEmail(signupRequest.getEmail());
        verify(userRepository).save(new User());
    }
 */

}
