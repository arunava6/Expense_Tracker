package com.example.ExpenseTracker.Security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Internal Filter for: {}", request.getRequestURI());
        try {
            final String authorizationHeader = request.getHeader("Authorization");
            String userEmail = null;
            String jwtToken = null;
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwtToken = authorizationHeader.substring(7);
                userEmail = jwtUtil.extractUsername(jwtToken);
                log.info("Extracted email from JWT: {}", userEmail);
            }
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(userEmail);
                if (jwtUtil.validationToken(jwtToken, userEmail)) {
                    log.info("Token validated successfully for user: {}", userEmail);
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                } else {
                    log.warn("Token validation failed for user: {}", userEmail);
                }
            }

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            log.error("JWT expired: {}", e.getMessage());
            writeErrorResponse(response, "JWT expired");
            return; // Important: stop filter chain
        } catch (JwtException e) {
            log.error("Invalid JWT: {}", e.getMessage());
            writeErrorResponse(response, "invalid JWT");
            return; // Important: stop filter chain
        } catch (Exception e) {
            log.error("Authentication failed: {}", e.getMessage(), e);
            writeErrorResponse(response, "Authentication failed: " + e.getMessage());
            return; // Important: stop filter chain
        }
    }

    private void writeErrorResponse(HttpServletResponse response, String message)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        response.getWriter().write(
                """
                        {
                          "status": 401,
                          "error": "UNAUTHORIZED",
                          "message": "%s"
                        }
                        """.formatted(message)
        );
    }
}


