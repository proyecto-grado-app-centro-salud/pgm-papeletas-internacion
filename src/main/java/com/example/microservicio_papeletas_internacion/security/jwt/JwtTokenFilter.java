package com.example.microservicio_papeletas_internacion.security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.microservicio_papeletas_internacion.models.UserMain;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class JwtTokenFilter extends OncePerRequestFilter {


    @Autowired
    JwtProvider jwtProvider;

    Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);   
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            String token = request.getHeader("Authorization");
            if (token != null && jwtProvider.validateToken(token)) {
                logger.error("token valido");

                String ci = jwtProvider.getUserNameFromToken(token);
                List<String> roles = jwtProvider.getRolesFromToken(token);
                UserDetails userDetails = UserMain.build(ci,roles);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.error("token valido final");

            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        filterChain.doFilter(request, response);
    }
}