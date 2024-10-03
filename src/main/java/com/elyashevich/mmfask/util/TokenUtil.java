package com.elyashevich.mmfask.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.List;
import java.util.Map;

@UtilityClass
public class TokenUtil {

    private final String secret = "984hg493gh0439rthr0429uruj2309yh937gc763fe87t3f89723gf";

    private final Long lifetime = 86_400_000L;

    public static String extractEmailClaims(final String token) {
        return getClaimsFromToken(token).getSubject();
    }

    @SuppressWarnings("unchecked")
    public static List<String> getRoles(final String token) {
        return getClaimsFromToken(token).get("roles", List.class);
    }

    public static String generateToken(final UserDetails userDetails) {
        return createToken(userDetails);
    }

    private static Claims getClaimsFromToken(final String token) {
        return Jwts
                .parserBuilder()
                .setSigningKeyResolver(new SigningKeyResolverAdapter() {
                    @Override
                    public byte[] resolveSigningKeyBytes(JwsHeader header, Claims claims) {
                        return secret.getBytes();
                    }
                })
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private static String createToken(final UserDetails userDetails) {
        Date issuedAt = new Date();
        return Jwts.builder()
                .setClaims(
                        Map.of(
                                "roles",
                                userDetails.getAuthorities().stream()
                                        .map(GrantedAuthority::getAuthority)
                                        .toList()
                        )
                )
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedAt)
                .setExpiration(new Date(issuedAt.getTime() + lifetime))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public static boolean validate(final String token) {
        return !getClaimsFromToken(token).getSubject().isEmpty()
                || getClaimsFromToken(token).getExpiration().before(new Date());
    }
}