package com.backend.ecommerce.service;

import com.backend.ecommerce.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
public class JwtService {
    public static final String
            SECRET_KEY = "GWk0pPwX4As81LCqjfdN8vMAWeL88W3WXiW78IAE5UBS1NOlVbju2kZq2aKzvNSOBhLKayjz3/PliqKsMxvaLf2U1PXoE2UPFqsrUqAu+nLA87rmod9/+j45CIxA48aKOe6/Pt70iq7H31EvdxYLPwiLfsf+qihVwt6KVYCHSOBb42rGxn5uWoFXz9Y/dJq5yEZZ9rURJJI+cF67Sp4+2nRKpvrPeUdQl+QZ48cYs1hlmPm63tQBLXh8tP+1wGPQUsSwj8mI/lDCP/WtIDNANHsjLyMDjE8fzPK+gYmScgdwEt5SJzOdxt+OabKUBwvtU5t952mcPMrKV200n6MLdHue9+u1jWtgnd0S3q1fSWE=";
    private Key getSignInKey() {
        byte[] key = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(key);
    }
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public <T> T extractClaim (String token, Function<Claims, T> claimsTFunction) {
        Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    public String generateToken(Map<String, Objects> extraClaims, User user) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 30 * 60))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public String generateToken(User user) {
        return generateToken(new HashMap<>(), user);
    }
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String email = extractEmail(token);
        return email.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
}
