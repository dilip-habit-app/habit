package com.habittracker.habit.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7).trim();
        String username;
        try {
            username = jwtUtil.extractUsername(token);
        } catch (Exception ex) {
            // invalid token — let security handle it downstream (or return 401 immediately)
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid JWT token: " + ex.getMessage());
            return;
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // extract roles if present (optional)
            var claims = jwtUtil.extractAllClaimsMap(token);
            Object rolesObj = claims.get("roles"); // roles may be a list or CSV
            List<SimpleGrantedAuthority> authorities = List.of();
            if (rolesObj != null) {
                if (rolesObj instanceof String s) {
                    authorities = List.of(s.split(",")).stream()
                            .map(r -> new SimpleGrantedAuthority("ROLE_" + r.trim()))
                            .collect(Collectors.toList());
                } else if (rolesObj instanceof java.util.Collection<?> coll) {
                    authorities = coll.stream()
                            .map(Object::toString)
                            .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                            .collect(Collectors.toList());
                }
            }

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);
            // optionally attach userId as details
            Long userId = jwtUtil.extractUserId(token);
            if (userId != null) {
                authToken.setDetails(userId);
            }

            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }
}
