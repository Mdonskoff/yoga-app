package com.openclassrooms.starterjwt.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @InjectMocks
    UserDetailsServiceImpl userDetailsService;

    @Test
    void loadUserByUsernameTest() {

        // GIVEN
        User user = new User(1L, "test@yoga.com", "Duck", "Donald", "password", false, LocalDateTime.now(), LocalDateTime.now());
        when(userRepository.findByEmail("test@yoga.com")).thenReturn(java.util.Optional.of(user));

        // WHEN
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsServiceImpl.loadUserByUsername("test@yoga.com");

        // THEN
        assertThat(userDetails.getUsername()).isEqualTo(user.getEmail());
        assertThat(userDetails.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(userDetails.getLastName()).isEqualTo(user.getLastName());
        assertThat(userDetails.getPassword()).isEqualTo(user.getPassword());

    }

    @Test
    void loadUserByUsernameUserNotFoundTest() {
        // GIVEN
        when(userRepository.findByEmail("test@yoga.com")).thenReturn(java.util.Optional.empty());

        // WHEN / THEN
        assertThatThrownBy(() -> userDetailsServiceImpl.loadUserByUsername("test@yoga.com"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User Not Found with email: test@yoga.com");
    }

    @Test
    @DisplayName("should return UserDetailsImpl object")
    public void giveUserEmail_thenLoadUserByUsername_shouldReturnUserDetailsImplObject() {
        userDetailsService = new UserDetailsServiceImpl(userRepository);

        String email = "test@test.com";
        User user = new User();
        user.setId(1L).setEmail(email).setLastName("Duck").setFirstName("Donald").setPassword("1234");
        UserDetails userDetails = UserDetailsImpl
                .builder()
                .id(user.getId())
                .username(user.getEmail())
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .password(user.getPassword())
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserDetails newUserDetail = userDetailsService.loadUserByUsername(email);

        assertEquals(userDetails, newUserDetail);
    }
}
