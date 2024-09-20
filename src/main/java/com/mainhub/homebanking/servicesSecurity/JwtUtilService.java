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

//Estas anotacions nos permiten inyectar dependencias de otras clases
@Service
public class JwtUtilService {

    // Genera una clave secreta

    // final= es constante, su valor no puede ser modificado una vez asignado
    //static= es significa que solo existe una copia de esta clave para toda la clase y no se crea una nueva instancia para cada objeto de la clase.
    //SecretKey es una clase que representa una clave de cifrado
    //Jwts.SIG.HS256.key() que es un algoritmo hash seguro y ampliamente utilizado para generar firmas digitales.
    //key(): Genera una clave aleatoria utilizando el algoritmo HS256.

    //Esta línea de código crea una clave secreta única y aleatoria que se utiliza para firmar y verificar los tokens JWT en una aplicación.

    //SECRET_KEY = Algoritmo de cifrado/de firma
    private static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();

    public static final long JWT_TOKEN_VALIDITY = 1000 * 60 * 60;

    //Verifica y decodifica el token JWT con la clave secreta y luego extrae y devuelve los claims
    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();
    }

    // Extrae un claim específico del token JWT
    public <T> T extractClaim(String token, Function<Claims, T> claimsTFunction) {
        final Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    //--------------------- METODOS DE UTILIDAD ---------------------


    // Extrae el nombre de usuario del token JWT
    public String extractUserName(String token) {
        //Recorre el token con sus claims y extrae el nombre de usuario
        return extractClaim(token, Claims::getSubject);
    }

    // Extrae la fecha de expiración del token JWT
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Verifica si la fecha de expriracion es antes que ahora devuelve true o false
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    //---------------------------------------------------------------------------


    // Crea un nuevo token JWT con los claims y el nombre de usuario proporcionados

    //Subject es el nombre de usuario == sujeto
    private String createToken(Map<String, Object> claims, String username) {

        //Builder: Crea un nuevo objeto JWT
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
        //Map de String, Object que almacena los claims
        //Donde define un espacio en memoria para almacenar información
        Map<String, Object> claims = new HashMap<>();

        /**
         * userDetails.getAuthorities(): Obtiene una colección de GrantedAuthority que representan los roles o permisos asignados al usuario autenticado.
         * .iterator().next(): Obtiene el primer elemento de la colección de GrantedAuthority. Si la colección está vacía, esto lanzará una excepción NoSuchElementException.
         * .getAuthority(): Obtiene el nombre del rol (authority) asociado al GrantedAuthority.
         */

        String rol = userDetails.getAuthorities().iterator().next().getAuthority();
        claims.put("rol", rol);
        return createToken(claims, userDetails.getUsername());
    }
}
