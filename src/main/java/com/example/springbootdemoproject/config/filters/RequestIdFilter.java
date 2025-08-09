package com.example.springbootdemoproject.config.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class RequestIdFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Generate request ID
        String requestId = UUID.randomUUID().toString();

        // Put it in MDC (Mapped Diagnostic Context) so logs can access it
        MDC.put("requestId", requestId);

        // Also return it in response headers (optional)
        response.setHeader("X-Request-ID", requestId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            // Remove from MDC after request completes
            MDC.remove("requestId");
        }
    }
}
