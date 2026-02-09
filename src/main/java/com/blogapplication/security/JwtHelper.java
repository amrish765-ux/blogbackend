package com.blogapplication.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtHelper {
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60; // 5 hours

    private final SecretKey secret = Keys.hmacShaKeyFor(
            "/s+CTcV49T57LQVlTs7oWsVHXpk8fzUjADv/7Fj/0owPnBLBZtZ+i+D8ggvb2ZTGZ4Yl65XqLyQqiiQke7iPJQ=="
                    .getBytes()
    );
    public String getUsernameFromToken(String token){
        return getClaimFromToken(token,Claims::getSubject);
    }


    public Date getExpirationDateFromToken(String token){
        return getClaimFromToken(token,Claims::getExpiration);
    }


    public String generateToken(UserDetails userDetails) {
        Map<String,Object>claims=new HashMap<>();
        return doGenerateToken(claims,userDetails.getUsername());
    }

    public <T> T getClaimFromToken(String token, Function<Claims,T>climsResolver){
        final Claims claims=getAllClaimsFromToken(token);
        return climsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(secret)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(secret)
                .compact();
    }

    private boolean isTokenExpired(String token){
        final Date expiration=getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public boolean validateToken(String token,UserDetails userDetails){
        final String username=getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername())&&!isTokenExpired(token));
    }
}
