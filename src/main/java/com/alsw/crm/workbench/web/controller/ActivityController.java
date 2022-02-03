package com.alsw.crm.workbench.web.controller;

import com.alsw.crm.settings.domain.User;
import com.alsw.crm.settings.service.UserService;
import com.alsw.crm.settings.service.impl.UserServiceImpl;
import com.alsw.crm.utils.DateTimeUtil;
import com.alsw.crm.utils.PrintJson;
import com.alsw.crm.utils.ServiceFactory;
import com.alsw.crm.utils.UUIDUtil;
import com.alsw.crm.vo.PaginationVO;
import com.alsw.crm.workbench.domain.Activity;
import com.alsw.crm.workbench.service.ActivityService;
import com.alsw.crm.workbench.service.impl.ActivityServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ActivityController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("↓ ==== 进入Activity控制器 ==== ↓");

        String path = req.getServletPath();
        if ("/workbench/activity/getNames.do".equals(path)) {
            getNames(req, resp);
        } else if ("/workbench/activity/save.do".equals(path)) {
            save(req, resp);
        } else if ("/workbench/activity/getActivityList.do".equals(path)) {
            getActivityList(req, resp);
        }
    }

    private void getActivityList(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("↓ ==== 进入查询市场活动列表控制器 ==== ↓");

        String name = req.getParameter("name");
        String owner = req.getParameter("owner");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");

        int pageNo = Integer.parseInt(req.getParameter("pageNo"));
        // 每页展现的记录数
        int pageSize = Integer.parseInt(req.getParameter("pageSize"));
        // 计算略过的记录数
        int skipCount = (pageNo - 1) * pageSize;

        Map<String, Object> pageMap = new HashMap<>();
        pageMap.put("name", name);
        pageMap.put("owner", owner);
        pageMap.put("startDate", startDate);
        pageMap.put("endDate", endDate);
        pageMap.put("skipCount", skipCount);
        pageMap.put("pageSize", pageSize);

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        // 使用VO类存储返回的 查询的总条数 市场活动列表
        PaginationVO<Activity> activityList = activityService.getActivityList(pageMap);

        PrintJson.printJsonObj(resp, activityList);

        System.out.println("↑ ==== 退出查询市场活动列表控制器 ==== ↑");
    }

    private void save(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("↓ ==== 进入市场活动添加控制器 ==== ↓");

        // id 使用uuid
        String id = UUIDUtil.getUUID();
        String owner = req.getParameter("owner");
        String name = req.getParameter("name");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        String cost = req.getParameter("cost");
        String description = req.getParameter("description");

        // 创建时间 当前系统时间
        String createTime = DateTimeUtil.getSysTime();
        // 创建人 当前登录用户
        String createBy = ((User) req.getSession().getAttribute("user")).getName();

        Activity activity = new Activity();
        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setCreateTime(createTime);
        activity.setCreateBy(createBy);

        System.out.println("------添加的活动信息 ------");
        System.out.println(activity);

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        Boolean successFlag = activityService.save(activity);

        System.out.println("------> 执行结果：" + successFlag);

        PrintJson.printJsonFlag(resp, successFlag);

        System.out.println("↑ ==== 退出市场活动添加控制器 ==== ↑");
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
