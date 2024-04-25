package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserControllerIT {

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private final String id = "10";

    @BeforeEach
    void setUp(){
        userService = new UserService(userRepository);
        userController = new UserController(userService, userMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    @DisplayName("Should return a user")
    void giveIdUser_thenFindUserById_shouldReturnAUserDto() throws Exception {

        User user = new User();
        user.setEmail("test@test.com");
        user.setLastName("test");
        user.setFirstName("test");

        UserDto expectedUserDto = userMapper.toDto(user);
        when(userRepository.findById(Long.parseLong(id))).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(expectedUserDto.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(expectedUserDto.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(expectedUserDto.getFirstName()));

        verify(userRepository).findById(Long.parseLong(id));
    }

    @Test
    @DisplayName("Should not found user")
    void giveIdUser_thenFindUserById_shouldNotFoundUser() throws Exception {

        when(userRepository.findById(Long.parseLong(id))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(userRepository).findById(Long.parseLong(id));
    }
    @Test
    @DisplayName("Should throw a bad request error")
    void giveIdUser_thenFindUserById_shouldThrowBadRequestError() throws Exception {

        when(userRepository.findById(Long.parseLong(id))).thenThrow(NumberFormatException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        verify(userRepository).findById(Long.parseLong(id));
    }

    @Test
    @DisplayName("Should delete a user")
    void giveIdUser_thenFindUserById_shouldDeleteUser() throws Exception {

        User user = new User();
        user.setEmail("test@test.com");
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "test@test.com", "John",
                "Doe", false, "password");
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(new UsernamePasswordAuthenticationToken(userDetails, null));
        when(userRepository.findById(Long.parseLong(id))).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(userRepository).findById(Long.parseLong(id));
    }

    @Test
    @DisplayName("Should return a not found status")
    void giveIdUser_thenFindUserById_shouldReturnNotFindStatus() throws Exception {

        when(userRepository.findById(Long.parseLong(id))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(userRepository).findById(Long.parseLong(id));
    }

    @Test
    @DisplayName("Should return a unauthorized status")
    void giveIdUser_thenFindUserById_shouldReturnUnauthorizedStatus() throws Exception {

        User user = new User();
        user.setEmail("t@test.com");
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "test@test.com", "John",
                "Doe", false, "password");
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(new UsernamePasswordAuthenticationToken(userDetails, null));
        when(userRepository.findById(Long.parseLong(id))).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/{id}", id))
                        .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        verify(userRepository).findById(Long.parseLong(id));
    }

    @Test
    @DisplayName("Should return a bad request status")
    void giveIdUser_thenFindUserById_shouldReturnBadRequestStatus() throws Exception {

        when(userRepository.findById(Long.parseLong(id))).thenThrow(NumberFormatException.class);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        verify(userRepository).findById(Long.parseLong(id));
    }
}
