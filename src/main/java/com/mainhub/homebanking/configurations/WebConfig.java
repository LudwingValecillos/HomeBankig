package com.mainhub.homebanking.configurations;

import com.mainhub.homebanking.filters.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
public class WebConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter; // Filtro personalizado para manejar la autenticación JWT.

    @Autowired
    private CorsConfigurationSource corsConfigurationSource; // Fuente de configuración para CORS (Cross-Origin Resource Sharing).

    /**
     * Dentro de nuestra aplicación tendremos
     * que realizar ciertas configuraciones
     * para gestionar la autorización. SecurityFilterChain utiliza la
     * autenticación basada en los datos que tiene SecurityContextHolder,
     * pasando esos datos al Administrador para determinar en función de los datos si estoy
     * autorizado o no a consumir el recurso al que se accede (puede depender de alguna propiedad
     * del usuario objeto del contexto, como un ROLE).
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity

                // Configuración de CORS utilizando la fuente de configuración proporcionada.
                .cors(cors -> cors.configurationSource(corsConfigurationSource))

                //
                .csrf(AbstractHttpConfigurer::disable)

                // Desactiva la autenticación básica HTTP.
                .httpBasic(AbstractHttpConfigurer::disable)

                // Desactiva el formulario de inicio de sesión.
                .formLogin(AbstractHttpConfigurer::disable)

                // Configura los encabezados de seguridad, desactivando la protección contra marcos (frame options).
                .headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer.frameOptions(
                        HeadersConfigurer.FrameOptionsConfig::disable))

                // Configura las reglas de autorización para las solicitudes HTTP.
                .authorizeHttpRequests(authorize ->
                        authorize

                                .requestMatchers("/api/clients/", "/api/clients/**", "/api/accounts/", "/api/accounts/**").hasRole("ADMIN")

                                // Permite el acceso sin autenticación a las rutas especificadas (login, registro, y consola H2).
                                .requestMatchers("/api/auth/login", "/api/auth/register", "/h2-console/**").permitAll()

                               .requestMatchers("/api/auth/current", "/api/clients/test", "/api/accounts/current/new", "/api/accounts/hello").hasRole("CLIENT")

                )

                // Agrega el filtro JWT antes del filtro de autenticación por nombre de usuario y contraseña.

                /**
                 * Filtro JWT: Se agrega un filtro personalizado (JwtRequestFilter) antes del filtro de autenticación de
                 * nombre de usuario y contraseña. Este filtro maneja las solicitudes entrantes para validar y procesar tokens JWT.
                 */

                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                // Configura la política de creación de sesiones como sin estado (stateless), sin crear sesiones en el servidor.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Construye y retorna la configuración de seguridad.
        return httpSecurity.build();
    }

    /**
     * Configura un codificador de contraseñas utilizando BCrypt para el almacenamiento seguro de contraseñas.
     *
     * @return Un codificador de contraseñas BCrypt.
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura un AuthenticationManager utilizando la configuración de autenticación proporcionada.
     *
     * @param authenticationConfiguration Configuración de autenticación.
     * @return El AuthenticationManager configurado.
     * @throws Exception Si ocurre algún error durante la configuración.
     */

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();

    }
}
