package com.elyashevich.mmfask.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TokenUtilTest {

    private static final String SECRET = "984hg493gh0439rthr0429uruj2309yh937gc763fe87t3f89723gf";

    @ParameterizedTest
    @CsvSource({
            "test@example.com, test@example.com",
            "another@example.com, another@example.com"
    })
    void extractEmailClaims_ValidToken_ReturnsEmail(final String username, final String expectedEmail) {
        // Arrange
        var token = Jwts.builder()
                .setSubject(username)
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()), SignatureAlgorithm.HS256)
                .compact();

        // Act
        var actualEmail = TokenUtil.extractEmailClaims(token);

        // Assert
        assertEquals(expectedEmail, actualEmail);
    }

    @ParameterizedTest
    @CsvSource({
            "ROLE_USER|ROLE_ADMIN, ROLE_USER|ROLE_ADMIN",
            "ROLE_USER|ROLE_MANAGER, ROLE_USER|ROLE_MANAGER"
    })
    void getRoles_ValidToken_ReturnsRoles(final String rolesString, final String expectedRolesString) {
        // Arrange
        var roles = List.of(rolesString.split("\\|"));
        var expectedRoles = List.of(expectedRolesString.split("\\|"));
        var token = Jwts.builder()
                .setClaims(Map.of("roles", roles))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()), SignatureAlgorithm.HS256)
                .compact();

        // Act
        var actualRoles = TokenUtil.getRoles(token);

        // Assert
        assertEquals(expectedRoles, actualRoles);
    }

    @ParameterizedTest
    @CsvSource({
            "test@example.com, ROLE_USER|ROLE_ADMIN",
            "another@example.com, ROLE_USER|ROLE_MANAGER"
    })
    void generateToken_ValidUserDetails_ReturnsToken(final String username, final String rolesString) {
        // Arrange
        var authorities = Stream.of(rolesString.split("\\|"))
                .map(SimpleGrantedAuthority::new)
                .toList();
        var userDetails = new UserDetailsStub(username, authorities);

        // Act
        var token = TokenUtil.generateToken(userDetails);

        // Assert
        var claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .setAllowedClockSkewSeconds(5) // Clock skew tolerance
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertAll(
                () -> assertEquals(username, claims.getSubject()),
                () -> assertEquals(
                        authorities.stream().map(SimpleGrantedAuthority::getAuthority).toList(),
                        claims.get("roles")
                )
        );
    }

    @ParameterizedTest
    @CsvSource({
            "test@example.com, 10000, true",
            "expired@example.com, -10000, false"
    })
    void validate_Token_ReturnsExpectedResult(final String username, final long expirationOffset, final boolean expectedValidationResult) {
        // Arrange
        var expirationDate = new Date(System.currentTimeMillis() + expirationOffset);
        var token = Jwts.builder()
                .setSubject(username)
                .setExpiration(expirationDate)
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()), SignatureAlgorithm.HS256)
                .compact();

        // Act
        var isValid = false;
        try {
            var claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes()))
                    .setAllowedClockSkewSeconds(5) // Allows 5 seconds of clock skew tolerance
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            isValid = claims.getExpiration().after(new Date());
        } catch (ExpiredJwtException ignored) {
            // no code
        }

        // Assert
        assertEquals(expectedValidationResult, isValid);
    }

    @SuppressWarnings("all")
    private static class UserDetailsStub implements UserDetails {
        private final String username;
        private final List<SimpleGrantedAuthority> authorities;

        UserDetailsStub(final String username, final List<SimpleGrantedAuthority> authorities) {
            this.username = username;
            this.authorities = authorities;
        }

        @Override
        public List<SimpleGrantedAuthority> getAuthorities() {
            return authorities;
        }

        @Override
        public String getPassword() {
            return null;
        }

        @Override
        public String getUsername() {
            return username;
        }
    }
}