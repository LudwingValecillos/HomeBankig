package com.mainhub.homebanking.servicesSecurity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.cglib.core.internal.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtUtilService {

    // Clave secreta utilizada para firmar los tokens JWT
    private static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();

    // Duración del token JWT (1 hora)
    public static final long JWT_TOKEN_VALIDITY = 1000 * 60 * 60;

    // Extrae todos los claims (atributos) del token JWT
    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();
    }

    // Extrae un claim específico del token JWT
    public <T> T extractClaim(String token, Function<Claims, T> claimsTFunction) {
        final Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    // Extrae el nombre de usuario del token JWT
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extrae la fecha de expiración del token JWT
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Verifica si el token JWT ha expirado
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Crea un nuevo token JWT con los claims y el nombre de usuario proporcionados
    private String createToken(Map<String, Object> claims, String username) {
        System.out.println("Clave secreta utilizada para firmar los tokens JWT: " + SECRET_KEY);
        return Jwts.builder()
                .claims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(SECRET_KEY)
                .compact();
    }

    // Genera un nuevo token JWT para un usuario
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        String rol = userDetails.getAuthorities().iterator().next().getAuthority();
        claims.put("rol", rol);
        return createToken(claims, userDetails.getUsername());
    }
}
