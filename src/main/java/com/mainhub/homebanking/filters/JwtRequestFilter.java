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

/** Para esta clase, utilizaremos la anotación @Component,
 * que la marcará como un componente Spring, permitiendo que sea escaneada
 * y añadida al contexto como un Bean para ser gestionada por Spring. **/
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService; // Servicio para cargar detalles del usuario.

    @Autowired
    private JwtUtilService jwtUtilService; // Servicio para manejar la lógica de JWT (JSON Web Token).



    /**
     *
     * Al realizar una solicitud a nuestra aplicación,
     * pasará por SecurityFilterChain, utilizando por defecto el método de autenticación
     * de nombre de usuario y contraseña (debe estar encriptado). La solicitud luego pasa por el AuthenticationManager,
     * que autentica nuestras credenciales de acceso. En esta etapa, la autenticación puede o no tener éxito.
     * Si es favorable, se crea un SecurityContextHolder, que es un objeto en memoria que se mantiene en el contexto
     * de la aplicación para contener todos los datos del usuario conectado en ese momento.
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
            //SecurityContextHolder verifica si no hay un usuario autenticado en el contexto de seguridad.

            if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // Cargar los detalles del usuario basado en el nombre de usuario.
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);

                // Verificar si el token JWT no ha expirado.
                if (!jwtUtilService.isTokenExpired(jwt)) {
                    // Crear objeto de autenticación con los detalles del usuario y sus autoridades.

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );

                    /**
                     * new WebAuthenticationDetailsSource(): Esta es una clase de Spring Security
                     * que se encarga de crear detalles de autenticación específicos para la solicitud HTTP actual.
                     */
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
    /**
     * Contexto en el Código Proporcionado
     * En el código que proporcionaste, esta línea se ejecuta después de que se verifica que el token JWT es válido y
     * que no ha expirado. Se carga la información del usuario (UserDetails) asociado con el token JWT y se crea un
     * objeto UsernamePasswordAuthenticationToken para representar la autenticación del usuario.
     *
     * Luego, se establecen los detalles adicionales de la solicitud utilizando WebAuthenticationDetailsSource.
     * Finalmente, esta autenticación se establece en el SecurityContextHolder, lo que permite que la aplicación reconozca al
     * usuario autenticado para las solicitudes posteriores.
     */
}

