package com.openclassrooms.starterjwt.security;

import com.openclassrooms.starterjwt.controllers.AuthController;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import com.openclassrooms.starterjwt.security.jwt.AuthTokenFilter;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthTokenFilterTest {
    @Mock
    private UserDetailsServiceImpl userDetailsService;

    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthTokenFilter authTokenFilter;


    @Test
    void parseJwtValidToken() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String validToken = "validToken";
        request.addHeader("Authorization", "Bearer " + validToken);

        // Act
        String result = authTokenFilter.parseJwt(request);

        // Assert
        assertThat(result).isEqualTo(validToken);
    }

    /*
    @Test
    void InvalidJwtToken() throws ServletException, IOException {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(new UserDetailsImpl(1L, "test@test.com", "Bruce", "Wayne", false, "1234"));
        String token = jwtUtils.generateJwtToken(authentication);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        authTokenFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
     */

    @Test
    void NoJwtToken() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        authTokenFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void parseJwtNoTokenInHeader() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();

        // Act
        String result = authTokenFilter.parseJwt(request);

        // Assert
        assertThat(result).isNull();
    }

}