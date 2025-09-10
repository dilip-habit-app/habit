package com.habittracker.habit.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
public class JWTService {
    private static final  String SECRET = "verysecretthisoneshouldbetoolong";

    public String generateToken(String userName){
        return Jwts.builder().setSubject(userName)
                .setIssuer("Habit-app")
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plusSeconds(300)))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }
    public String extractUserName(String token){
        return Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    public boolean isValid(String token){
        try{
            extractUserName(token);
            return true;
        }catch ( Exception ex){
            return false;
        }
    }
}
