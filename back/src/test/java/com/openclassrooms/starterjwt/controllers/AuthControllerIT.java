package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AuthControllerIT {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthController authController;

    @Mock
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    private SignupRequest signupRequest;

    @BeforeEach
    void setUp(){
        authController = new AuthController(authenticationManager, passwordEncoder, jwtUtils, userRepository);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
        signupRequest = new SignupRequest();
        signupRequest.setEmail("test@test.com");
        signupRequest.setLastName("lastName");
        signupRequest.setFirstName("firstName");
        signupRequest.setPassword("password");
    }

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

    @Test
    @DisplayName("Should register but email already exists")
    void giveSignupRequest_thenRegister_shouldReturnBadRequest() throws Exception{

        MessageResponse messageResponse = new MessageResponse("Error: Email already exists!");
        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("api/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(messageResponse.getMessage()));
        verify(userRepository).existsByEmail(signupRequest.getEmail());
    }

    @Test
    @DisplayName("Should register")
    void giveSignupRequest_thenRegister_shouldRegister() throws Exception{
        MessageResponse messageResponse = new MessageResponse("User successfully registered !");

        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.post("api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(messageResponse.getMessage()));
        verify(userRepository).existsByEmail(signupRequest.getEmail());
        verify(userRepository).save(new User());
    }
}
