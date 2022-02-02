package com.alsw.crm.workbench.web.controller;

import com.alsw.crm.settings.domain.User;
import com.alsw.crm.settings.service.UserService;
import com.alsw.crm.settings.service.impl.UserServiceImpl;
import com.alsw.crm.utils.PrintJson;
import com.alsw.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ActivityController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("↓ ==== 进入Activity控制器 ==== ↓");

        String path = req.getServletPath();
        if ("/workbench/activity/getNames.do".equals(path)) {
            getNames(req, resp);
        }
    }

    private void getNames(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("↓ ==== 进入获取所有用户控制器 ==== ↓");

        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> userList = userService.getUserList();

        System.out.println("------ 获取的用户列表 ------");
        for (User user :
                userList) {
            System.out.println("------> 用户信息：" + user);
        }

        // 返回用户名json数组
        if (userList.size() > 0) {
            PrintJson.printJsonObj(resp, userList);
        }

        System.out.println("↑ ==== 退出获取所有用户控制器 ==== ↑");
    }


}
