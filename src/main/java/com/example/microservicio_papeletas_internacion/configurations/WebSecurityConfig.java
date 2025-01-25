package com.example.microservicio_papeletas_internacion.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.microservicio_papeletas_internacion.security.jwt.CustomAccessDeniedHandler;
import com.example.microservicio_papeletas_internacion.security.jwt.JwtEntryPoint;
import com.example.microservicio_papeletas_internacion.security.jwt.JwtTokenFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
    @Autowired
    private JwtEntryPoint jwtEntryPoint;

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;


    @Bean
    public JwtTokenFilter jwtTokenFilter() {
        return new JwtTokenFilter();
    }
    
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
            .cors()  // Configuración de CORS
            .and()
            .csrf().disable()  // Deshabilitar CSRF para permitir solicitudes sin autenticación
            .authorizeRequests()
                .requestMatchers("/auth/*","/manage/*").permitAll()  // Permite todas las rutas sin autenticación
                .anyRequest().authenticated()
                .and().exceptionHandling()
                .accessDeniedHandler(new CustomAccessDeniedHandler()).authenticationEntryPoint(jwtEntryPoint)
                .and()  
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);  // Permite cualquier otra solicitud sin autenticación
        return http.build();
    }
}