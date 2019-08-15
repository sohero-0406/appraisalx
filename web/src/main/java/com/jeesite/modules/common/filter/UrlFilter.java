package com.jeesite.modules.common.filter;

import com.jeesite.modules.common.entity.ExamUser;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

@Component
@WebFilter(urlPatterns = "/*", filterName = "urlFilter")
public class UrlFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String uri = req.getServletPath();
        boolean flag = true;
        //不需要过滤的路径
//        String[] urls = {"/aa/signIn/", ".js", ".css", ".ico", ".jpg", ".png", ".html"};
        String[] urls = {"/"};
        for (String str : urls) {
            if (uri.contains(str)) {
                flag = false;
                break;
            }
        }
        if (!flag) {
            chain.doFilter(request, response);
        } else {
            ExamUser examUser = (ExamUser) req.getSession().getAttribute("examUser");
            if (null != examUser) {
                chain.doFilter(request, response);
            }
            req.getRequestDispatcher("/aa/signIn/timeout").forward(req, response);
        }
    }

    @Override
    public void destroy() {

    }
}
