package com.alsw.crm.settings.web.controller;

import com.alsw.crm.exception.LoginException;
import com.alsw.crm.settings.domain.User;
import com.alsw.crm.settings.service.UserService;
import com.alsw.crm.settings.service.impl.UserServiceImpl;
import com.alsw.crm.utils.MD5Util;
import com.alsw.crm.utils.PrintJson;
import com.alsw.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("+↓ ==== 进入User控制器 ==== ↓+");

        String path = req.getServletPath();

        if ("/settings/user/login.do".equals(path)) {
            login(req, resp);
        }

    }

    private void login(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("-↓ ==== 进入登录控制器 ==== ↓-");
        String loginAct = request.getParameter("loginAct");
        System.out.println("------> 账号为：" + loginAct);

        // 将明文密码装为MD5密码
        String loginPwd = request.getParameter("loginPwd");
        System.out.println("------> 明文密码为：" + loginPwd);
        loginPwd = MD5Util.getMD5(loginPwd);
        System.out.println("------> MD5密码为：" + loginPwd);

        // 接收ip地址
        String ip = request.getRemoteAddr();
        System.out.println("------> IP为：" + ip);

        // 动态代理创建业务对象
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());


        try {
            User user = us.login(loginAct, loginPwd, ip);

            request.getSession().setAttribute("user", user);
            // 执行到此 业务层没有在上面的调用中抛出异常
            // 登录成功
            System.out.println("↑ ==== 登录成功 ==== ↑");
            /*
                {"success":true}
             */
            PrintJson.printJsonFlag(response, true);
        } catch (LoginException e) {
            // 执行到此 业务层在上面的调用中抛出异常
            // 登录失败
            System.out.println("↑ ==== 登录失败 ==== ↑");
             /*
                {
                "success":false,
                "msg":"错误信息"
                }

                如何设置msg 两种方式
                    1）map
                    2）vo
             */
            String msg = e.getMessage();
            Map<String, Object> map = new HashMap<>();
            map.put("success", false);
            map.put("msg", msg);
            PrintJson.printJsonObj(response, map);
            e.printStackTrace();
        }

    }

}
