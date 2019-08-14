package com.mango.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 防止xss注入过滤器
 */
@WebFilter(filterName="xssFilter", urlPatterns="/v1/*")
public class XssFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        filterChain.doFilter(new XssHttpServletRequestWrapper(request),servletResponse);
    }
}
