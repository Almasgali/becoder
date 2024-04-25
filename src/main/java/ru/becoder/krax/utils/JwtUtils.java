package ru.becoder.krax.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@PropertySource("application.properties")
public class JwtUtils {

    @Value("${signing.token.expiration}")
    private static final int EXPIRATION_MS = 3600000;

    private final SecretKey secretKey;

    @Autowired
    public JwtUtils(@Value("${signing.token.key}") String secretKey) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public boolean isExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    public String getToken(UserDetails userDetails) {
        long currentTime = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(currentTime + EXPIRATION_MS))
                .signWith(secretKey, SignatureAlgorithm.HS512).compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean validateJwtToken(String token) {
        return !isExpired(extractClaims(token));
    }

    private Claims extractClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build()
                .parseClaimsJws(token).getBody();
    }
}