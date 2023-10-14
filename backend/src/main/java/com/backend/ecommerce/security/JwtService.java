package com.backend.ecommerce.security;

import com.backend.ecommerce.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
public class JwtService {
    public static final String
            SECRET_KEY = "89a74eecd80b96757fecc95d7b689157600366d5c114fb8794ae07ea2f7c7fd2";
    SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());


    public String extractEmail(String token) {
        token = token.substring(7);
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        String email = String.valueOf(claims.get("email"));
        return email;
    }
    public String generateToken(Authentication authentication) {
        return Jwts
                .builder()
                .claim("email", authentication.getName())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 30 * 60))
                .signWith(key)
                .compact();
    }
}
