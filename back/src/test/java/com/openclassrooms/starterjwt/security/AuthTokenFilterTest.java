package com.openclassrooms.starterjwt.security;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import com.openclassrooms.starterjwt.security.jwt.AuthTokenFilter;

import static org.assertj.core.api.Assertions.assertThat;

class AuthTokenFilterTest {

    private final AuthTokenFilter authTokenFilter = new AuthTokenFilter();

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