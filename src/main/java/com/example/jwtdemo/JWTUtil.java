package com.example.jwtdemo;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {
    private static final String SECRET_KEY = "QWERTYUIOPASDFGHJKLZXCVBNMQWERTYU";
    private static final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    String generateToken(String username, long expiryMinutes){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+expiryMinutes*60*1000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String validateAndExtractUsername(String token) {
        try{
            String username =  Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
//            System.out.println("username-----"+username);
            return username;
        } catch (JwtException e) {
//            System.out.println("Exception--->"+e);
            return null;
        }
    }
}
