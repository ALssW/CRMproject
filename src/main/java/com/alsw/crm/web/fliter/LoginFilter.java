package com.alsw.crm.web.fliter;

import com.alsw.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("↓ ==== 进入登录验证过滤器 ==== ↓");

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        String reqPath = req.getServletPath();

        if ("/login.jsp".equals(reqPath) || "/settings/user/login.do".equals(reqPath)) {
            System.out.println("↑ ==== 已进入登录页面 ==== ↑");
            filterChain.doFilter(req, res);
            return;
        }

        User user = (User) req.getSession().getAttribute("user");

        if (user != null) {
            System.out.println("↑ ==== 已登录-跳转中 ==== ↑");
            filterChain.doFilter(req, res);
        } else {
            System.out.println("↑ ==== 未登录-跳转登录页面 ==== ↑");
            res.sendRedirect(req.getContextPath() + "/login.jsp");
        }
    }

    @Override
    public void destroy() {

    }
}
