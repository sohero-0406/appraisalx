package com.jeesite.modules.common.filter;

import com.jeesite.common.cache.CacheUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.utils.jwt.JwtUtils;
import com.jeesite.modules.common.entity.ExamUser;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@WebFilter(urlPatterns = "/*", filterName = "tokenFilter")
public class TokenFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String uri = httpServletRequest.getServletPath();
        boolean flag = false;
        String[] IGNORE_URI = {"/signIn", ".js", ".css", ".ico", ".jpg", ".png", ".html", ".jpeg"};
        for (String s : IGNORE_URI) {
            if (uri.contains(s)) { // 如果是登陆页面的请求 则放过
                flag = true;
                break;
            }
        }
        if (flag) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
            String token = httpServletRequest.getHeader(JwtUtils.getHeader());
            // 如果header中不存在token，则从参数中获取token
            if (StringUtils.isEmpty(token)) {
                token = httpServletRequest.getParameter(JwtUtils.getHeader());
            }
            Claims claims = JwtUtils.getClaimByToken(token);

            if (claims == null || JwtUtils.isTokenExpired(claims.getExpiration())) {
                httpServletRequest.getRequestDispatcher("/aa/signIn/timeout").forward(httpServletRequest, httpServletResponse);
            } else {
                String userId = claims.getSubject();
                ExamUser examUser = CacheUtils.get("examUser", userId);
                if (null == examUser) {
                    httpServletRequest.getRequestDispatcher("/aa/signIn/timeout").forward(httpServletRequest, httpServletResponse);
                } else {
                    filterChain.doFilter(servletRequest, servletResponse);
                }
            }
        }
    }

    @Override
    public void destroy() {

    }
}
