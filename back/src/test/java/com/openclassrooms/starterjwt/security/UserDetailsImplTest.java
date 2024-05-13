package com.openclassrooms.starterjwt.security;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

@ExtendWith(MockitoExtension.class)
public class UserDetailsImplTest {

    @Mock
    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp() {
        userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("user1@mail.com")
                .firstName("User")
                .lastName("USER")
                .admin(false)
                .password("password")
                .build();
    }

    @Test
    void testGetAuthorities() {
        // WHEN
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        // THEN
        assertThat(authorities).isNotNull().isEmpty();
    }

    @Test
    void testIsAccountNonExpired() {
        // THEN
        assertThat(userDetails.isAccountNonExpired()).isTrue();
    }

    @Test
    void testIsAccountNonLocked() {
        // THEN
        assertThat(userDetails.isAccountNonLocked()).isTrue();
    }

    @Test
    void testIsCredentialsNonExpired() {
        // THEN
        assertThat(userDetails.isCredentialsNonExpired()).isTrue();
    }

    @Test
    void testIsEnabled() {
        // THEN
        assertThat(userDetails.isEnabled()).isTrue();
    }

    @Test
    void testEquals_SameObject() {
        // GIVEN
        UserDetailsImpl sameUserDetails = userDetails;

        // THEN
        assertThat(userDetails.equals(sameUserDetails)).isTrue();
    }

    @Test
    void testEquals_DifferentClass() {
        // GIVEN
        Object differentObject = new Object();

        // THEN
        assertThat(userDetails.equals(differentObject)).isFalse();
    }

    @Test
    void testEquals_DifferentId() {
        // GIVEN
        UserDetailsImpl differentUserDetails = UserDetailsImpl.builder().id(2L).build();

        // THEN
        assertThat(userDetails.equals(differentUserDetails)).isFalse();
    }

    @Test
    void testEquals_SameId() {
        // GIVEN
        UserDetailsImpl sameIdUserDetails = UserDetailsImpl.builder().id(1L).build();

        // THEN
        assertThat(userDetails.equals(sameIdUserDetails)).isTrue();
    }
}
