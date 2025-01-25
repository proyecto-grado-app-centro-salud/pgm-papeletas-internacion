package com.example.microservicio_papeletas_internacion.security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class JwtEntryPoint implements AuthenticationEntryPoint {
    Logger logger = LoggerFactory.getLogger(JwtEntryPoint.class);   
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // logger.error("Error de autenticación: {}", authException.getStackTrace());
        StringBuilder errorDetails = new StringBuilder();
        for (StackTraceElement element : authException.getStackTrace()) {
            errorDetails.append(element.toString()).append("\n");
        }
        
        // logger.error("Error de autenticación: {}", errorDetails.toString());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No autorizado");
    }
}