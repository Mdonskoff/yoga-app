package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
public class AuthControllerTest {

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
    @Mock
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;


    @BeforeEach
    void setUp() {
        authController = new AuthController(authenticationManager, passwordEncoder, jwtUtils, userRepository);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
    }
    @Test
    @DisplayName("Should login")
    void giveLoginRequest_thenAuthenticate_shouldLogin() throws Exception {
        // Créer un utilisateur dans la base de données pour simuler l'utilisateur existant
        User user = new User("test@example.com", "Doe", "John", passwordEncoder.encode("1234"), false);
        User newUser = userRepository.save(user);

        // Créer une demande de connexion valide
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("1234");


        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("test@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.admin").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(newUser.getId()));

    }

        /*

        // Appeler la méthode authenticateUser() du contrôleur
        //ResponseEntity<?> responseEntity = authController.authenticateUser(loginRequest);

        // Vérifier que la réponse est conforme aux attentes
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        JwtResponse jwtResponse = (JwtResponse) responseEntity.getBody();
        assertEquals(newUser.getId(), jwtResponse.getId());
        assertEquals("test@example.com", this.jwtUtils.getUserNameFromJwtToken(jwtResponse.getToken()));
        assertNotNull(jwtResponse.getToken());

         */


    @Test
    @DisplayName("Should register but email already exists")
    void giveSignupRequest_thenRegister_shouldReturnBadRequest() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password");

        // Ajouter un utilisateur avec le même email pour simuler un email déjà pris
        User existingUser = new User(signupRequest.getEmail(), "Doe", "John", "password", false);
        userRepository.save(existingUser);

        MessageResponse messageResponse = new MessageResponse("Error: Email is already taken!");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(messageResponse.getMessage()));

    }

    @Test
    @DisplayName("Should register")
    void giveSignupRequest_thenRegister_shouldRegister() throws Exception {
        signupRequest = new SignupRequest();
        signupRequest.setEmail("test1@test.com");
        signupRequest.setLastName("lastName");
        signupRequest.setFirstName("firstName");
        signupRequest.setPassword("password");

        // Supprimez l'utilisateur s'il existe déjà pour éviter les erreurs lors de l'exécution du test
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            userRepository.deleteByEmail(signupRequest.getEmail());
        }

        MessageResponse messageResponse = new MessageResponse("User registered successfully!");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(messageResponse.getMessage()));

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
