package com.mainhub.homebanking.filters;

import com.mainhub.homebanking.servicesSecurity.JwtUtilService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService; // Servicio para cargar detalles del usuario.

    @Autowired
    private JwtUtilService jwtUtilService; // Servicio para manejar la lógica de JWT (JSON Web Token).

    /**
     * Filtra solicitudes HTTP para verificar el token JWT.
     *
     * @param request     La solicitud HTTP.
     * @param response    La respuesta HTTP.
     * @param filterChain La cadena de filtros a continuar.
     * @throws ServletException Si ocurre un error durante el procesamiento.
     * @throws IOException      Si ocurre un error de entrada/salida.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Variables para el nombre de usuario y el token JWT.
        String userName = null;
        String jwt = null;

        try {

            // Obtener el encabezado de autorización de la solicitud.
            final String authorizationHeader = request.getHeader("Authorization");

            // Verificar si el encabezado es válido y contiene el prefijo "Bearer ".

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                // Extraer el token JWT del encabezado.
                jwt = authorizationHeader.substring(7);
                // Extraer el nombre de usuario del token JWT.
                userName = jwtUtilService.extractUserName(jwt);
                System.out.println("Correo extraido del token: " + userName);
            }

            // Verificar si el nombre de usuario no es nulo y no hay autenticación en el contexto de seguridad.
            //getContext() devuelve el contexto de seguridad actual.
            //SecurityContextHolder asegura que no haya una autenticación en el contexto de seguridad.

            if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // Cargar los detalles del usuario basado en el nombre de usuario.
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);

                // Verificar si el token JWT no ha expirado.
                if (!jwtUtilService.isTokenExpired(jwt)) {
                    // Crear objeto de autenticación con los detalles del usuario y sus autoridades.

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );

                    // Establecer los detalles de autenticación en el objeto de autenticación.
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // Establecer la autenticación en el contexto de seguridad.
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }

        } catch (Exception e) {
            // Manejar excepciones (para producción, usar un logger).
            System.out.println(e.getMessage());
        } finally {
            // Continuar con la cadena de filtros.
            filterChain.doFilter(request, response);
        }
    }
}
