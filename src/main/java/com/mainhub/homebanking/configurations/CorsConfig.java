package com.mainhub.homebanking.configurations;

import io.jsonwebtoken.lang.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    //COMPARTIR RECURSOS Y ORIGENES CRUZADOS, para asi restringir solicitudes de dominion diferente para lo que fue creada la app
    //Este código de configuración define las reglas de CORS (Cross-Origin Resource Sharing) para una aplicación Spring Boot.
    // Estas reglas controlan qué orígenes pueden hacer solicitudes a la aplicación y qué métodos y encabezados están permitidos.
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        //Instancia del objeto para asi hacerles modificaciones
        CorsConfiguration configuration = new CorsConfiguration();

        //Se establecen las rutas permitidas, que nos pueden pegar a nuestra app
        configuration.setAllowedOrigins(io.jsonwebtoken.lang.Arrays.asList(new String[]{"http://localhost:8080", "http://localhost:5173" , "https://homebankig.onrender.com"}));

        //Se establecen los métodos HTTP permitidos utilizando
        configuration.setAllowedMethods(Arrays.asList(new String[]{"GET", "POST", "PUT", "DELETE"}));

        //Se permite cualquier encabezado
        configuration.setAllowedHeaders(List.of("*"));

        //es una clase de Spring que se utiliza para configurar la política CORS (Cross-Origin Resource Sharing)
        //Se establece una fuente de configuracion de CORS para las rutas
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        //Se establece a que rutas de nuestra rupa nos puede pegar
        // "/api/clientes/" o "/api/accounts/" o lo que sea
        source.registerCorsConfiguration("/**", configuration);

        // Se retorna la fuente configurada para que se apliquen las configuaciones

        return source;
    }
}