package com.example.springbootdemoproject.config.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Component
@Order(1)
public class ResponseCacheFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        ContentCachingResponseWrapper servletResponseWrapper =
                new ContentCachingResponseWrapper((HttpServletResponse) servletResponse);
        filterChain.doFilter(servletRequest, servletResponseWrapper);
        //Copies the response to the client response writer
        servletResponseWrapper.copyBodyToResponse();
    }
}
