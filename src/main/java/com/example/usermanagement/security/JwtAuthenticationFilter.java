package com.example.usermanagement.security;

import com.example.usermanagement.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // Step 1: Extract token from Authorization header
            String token = getTokenFromRequest(request);

            // Step 2: If token exists, validate it
            if (token != null && jwtTokenProvider.validateToken(token)) {

                // Step 3: Extract user info from token
                Long userId = jwtTokenProvider.getUserIdFromToken(token);
                String email = jwtTokenProvider.getEmailFromToken(token);
                String role = jwtTokenProvider.getRoleFromToken(token);

                // Step 4: Create authority with role
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role));

                // Step 5: Create authentication token
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                email,           // Principal (who is making request)
                                null,            // Credentials (null - already authenticated)
                                authorities      // Authorities (what they can do)
                        );

                // Step 6: Set authentication in SecurityContext
                // Now Spring knows who the user is and what they can do
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication: {}", ex);
        }

        // Step 7: Continue with the request
        filterChain.doFilter(request, response);
    }

    /**
     * Extract JWT token from Authorization header
     * Header format: "Authorization: Bearer <token>"
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            // Remove "Bearer " prefix
            return bearerToken.substring(7);
        }

        return null;
    }
}







//
//// ADMIN ONLY ENDPOINTS (need ADMIN role)
//                        .requestMatchers(HttpMethod.GET, "/api/v1/users").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/api/v1/users/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.PUT, "/api/v1/users/*/role").hasRole("ADMIN")

