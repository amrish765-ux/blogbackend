package com.blogapplication.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        String rid = MDC.get("requestId");
        String msg = authException.getMessage() == null
                ? "Full authentication is required to access this resource"
                : authException.getMessage().replace("\"", "\\\"");

        String json = """
            {
              "status": 401,
              "error": "UNAUTHORIZED",
              "message": "%s",
              "path": "%s",
              "requestId": "%s"
            }
            """.formatted(msg, request.getRequestURI(), rid);

        response.getWriter().write(json);
    }
}
