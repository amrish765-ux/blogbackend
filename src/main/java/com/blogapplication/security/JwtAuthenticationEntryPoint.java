package com.blogapplication.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // Set status code to 401 Unauthorized
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        response.setContentType("application/json");
//
//        // Create a JSON response with error message
//        String jsonResponse = String.format("{\"error\": \"Unauthorized\", \"message\": \"%s\"}", authException.getMessage());

        // Write the JSON response to the response writer
        PrintWriter writer = response.getWriter();
        writer.println("Access denied!!"+authException.getMessage());
//        writer.flush();
    }
}
