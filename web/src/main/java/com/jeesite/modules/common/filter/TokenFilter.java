package com.jeesite.modules.common.filter;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.utils.jwt.JwtUtils;
import io.jsonwebtoken.Claims;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

//@Component
//@WebFilter(urlPatterns = "/*", filterName = "tokenFilter")
public class TokenFilter implements Filter {


    private static final String[] IGNORE_URI = {"/signIn"};

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String uri = httpServletRequest.getServletPath();
        System.out.println(uri);
        boolean flag = false;
        for (String s : IGNORE_URI) {
            if (uri.contains(s)) { // 如果是登陆页面的请求 则放过
                flag = true;
                break;
            }
        }
        System.out.println(flag);
        if (flag) {
            Map<String, String[]> map = httpServletRequest.getParameterMap();
            System.out.println("xxx");
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
            String token = httpServletRequest.getHeader(JwtUtils.getHeader());
            // 如果header中不存在token，则从参数中获取token
            if (StringUtils.isEmpty(token)) {
                token = httpServletRequest.getParameter(JwtUtils.getHeader());
            }
            Claims claims = JwtUtils.getClaimByToken(token);

            //claims.get
            if (claims == null || JwtUtils.isTokenExpired(claims.getExpiration())) {
                if (claims == null) {
                    httpServletResponse.sendRedirect("/platform/common/commonUser/unLogin");
                } else {
                    httpServletResponse.sendRedirect("/platform/common/commonUser/tokenExpired");
                }
            } else {
                if (uri.contains("/add") || uri.contains("/save")) {
                    Enumeration<String> list = httpServletRequest.getParameterNames();
                    Map<String, String[]> map = httpServletRequest.getParameterMap();
                    System.out.println("xxx");
                }
                filterChain.doFilter(servletRequest, servletResponse);
            }
        }
    }

    @Override
    public void destroy() {

    }
}
