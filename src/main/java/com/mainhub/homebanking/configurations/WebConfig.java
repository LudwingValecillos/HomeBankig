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
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        //Recibe un HttpSecurity como argumento y devuelve un SecurityFilterChain.
        //Que se encarga de configurar la seguridad de la aplicación.

        httpSecurity
                //Definiendo la configuración de CORS

                .cors(cors -> cors.configurationSource(corsConfigurationSource))

                //Cross-Site Request Forgery, o Falsificación de Petición en Sitios Cruzados.

                //Se desactiva porque no se utiliza el inisio de sesión si no mediante tokens por cada solicitud
                //Pero como trabajamos con un frontend se desactiva asi no se fija si la peticion vino de un formulario
                .csrf(AbstractHttpConfigurer::disable)

                //Se desactiva la configuración de Basic que proporciona Spring Security
                //No es seguro ya que las credenciales de usuario son sensibles y viajan sin protección
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                //Se desactiva la configuración de los encabezados
                //si deseas permitir que otras aplicaciones se incrusten en la tuya.
                //Se desactiva ya que utilizamos el h2-console para interactuar con la base de datos ya que es embebida en la app
                .headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer.frameOptions(
                        HeadersConfigurer.FrameOptionsConfig::disable))
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()

                                .requestMatchers("/api/current", "/api/accounts/clients/current/accounts", "/api/cards/clients/current/cards", "/api/transactions/clients/current/transactions"
                                        , "/api/loans/apply", "/api/loans/loansAvailable", "/api/loans/loansApplied", "/api/accounts/delete/{id}").hasRole("CLIENT")
                                .requestMatchers("/api/clients/", "/api/clients/**", "/api/accounts/", "/api/accounts/**", "/api/cards/", "/api/cards/**", "/api/loans/", "/api/loans/**").hasRole("ADMIN")
                                .anyRequest().authenticated()
                )

                //Se agrega el filtro de autenticación antes del filtro de autenticación de recursos
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)

                //Se establece la politica de inicio de sesión como STATELESS ya que no se requiere una sesión
                //Se maneja la autenticación por medio de tokens que esta presente por cada peticion

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        //Se retorna la configuración y se construye la configuracion de la cadena de seguridad

        //Build(patrones diseños)

        return httpSecurity.build();
    }


    @Bean
    PasswordEncoder passwordEncoder() {

        // Crea un bean que utiliza el algoritmo BCrypt para cifrar contraseñas de forma segura.
        // BCrypt es un algoritmo de hash unidireccional que genera una representación encriptada de la contraseña,
        // haciendo imposible recuperar la contraseña original.
        // Este cifrado se utiliza principalmente al registrar nuevos usuarios para proteger sus credenciales.

        return new BCryptPasswordEncoder();
    }


    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        //Este bean es el encargado de la autenticación de los usuarios una vez que se vayan logeando


        return authenticationConfiguration.getAuthenticationManager();
    }
}
