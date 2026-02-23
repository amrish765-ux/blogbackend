package com.blogapplication.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");


        if (header == null || header.isBlank() || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7).trim();


        long dots = token.chars().filter(ch -> ch == '.').count();
        if (dots != 2) {
            log.warn("Bad JWT format {} {} rid={}", request.getMethod(), request.getRequestURI(), MDC.get("requestId"));
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String username = jwtHelper.getUsernameFromToken(token);


            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtHelper.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);


                    log.debug("JWT validated for user={} {} {} rid={}",
                            username, request.getMethod(), request.getRequestURI(), MDC.get("requestId"));
                } else {
                    log.warn("JWT validation failed for user={} {} {} rid={}",
                            username, request.getMethod(), request.getRequestURI(), MDC.get("requestId"));
                }
            }

        } catch (ExpiredJwtException ex) {

            log.warn("JWT expired {} {} rid={}", request.getMethod(), request.getRequestURI(), MDC.get("requestId"));
        } catch (UsernameNotFoundException ex) {
            log.warn("JWT user not found {} {} rid={}", request.getMethod(), request.getRequestURI(), MDC.get("requestId"));
        } catch (JwtException | IllegalArgumentException ex) {

            log.warn("JWT invalid {} {} rid={}", request.getMethod(), request.getRequestURI(), MDC.get("requestId"));
        } catch (Exception ex) {

            log.error("JWT filter error {} {} rid={}", request.getMethod(), request.getRequestURI(), MDC.get("requestId"), ex);
        }

        filterChain.doFilter(request, response);
    }
}
