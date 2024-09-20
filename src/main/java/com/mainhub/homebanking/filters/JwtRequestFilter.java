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

/**
 * Filtro que intercepta cada petición HTTP y verifica la autenticación JWT.
 * Si el token es válido, establece el contexto de seguridad para permitir el acceso a recursos protegidos.
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtilService jwtUtilService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // Extrae el token JWT del encabezado de autorización de la petición
            String authorizationHeader = request.getHeader("Authorization");
            String jwtToken = null;
            String username = null;

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwtToken = authorizationHeader.substring(7);
                username = jwtUtilService.extractUserName(jwtToken);
            }

            // Si hay un usuario y no hay una autenticación establecida en el contexto de seguridad
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // Carga los detalles del usuario (roles, permisos) a partir del nombre de usuario

                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                // Verifica que el token no haya expirado
                if (!jwtUtilService.isTokenExpired(jwtToken)) {

                    // Se crea un objeto Authentication que representa al usuario autenticado y se establece en el contexto de seguridad de Spring Security.
                    //Esta autenticación se establece en el contexto de seguridad de Spring Security y contiene información sobre el usuario y sus permisos.
                    //Se define null las credenciales ya que vamos a trabajr con la autenticacion a traves de un token.

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );


                    //De la autenticacion se le setea los detalles
                    //Se crea un objeto WebAuthenticationDetailsSource que se utiliza para obtener la información de la solicitud HTTP.
                    //Llamamos a su metodo (el unico que tiene) buildDetails con el que vmaos a crear y establecer los talles a la
                    // autenticacion basados en la peticion proporcionada

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    //Establecemos la autenticación del usuario actual, para gestionar la autenticacion y la autorización del usuario.
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {

            System.out.println("Error en el filtro JWT: " + e.getMessage());

        } finally {
            // Lo recibimos por parametros y llamamos al metodo doFilter para que continue con la siguiente cadena de filtros
            filterChain.doFilter(request, response);
        }
    }
}