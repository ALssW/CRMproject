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
import com.alsw.crm.workbench.domain.ActivityRemark;
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
        } else if ("/workbench/activity/deleteActivity.do".equals(path)) {
            deleteActivity(req, resp);
        } else if ("/workbench/activity/getUserAndActivity.do".equals(path)) {
            getUserAndActivity(req, resp);
        } else if ("/workbench/activity/updateActivity.do".equals(path)) {
            updateActivity(req, resp);
        } else if ("/workbench/activity/detail.do".equals(path)) {
            detail(req, resp);
        } else if ("/workbench/activity/getActivityRemarkList.do".equals(path)) {
            getActivityRemarkList(req, resp);
        } else if ("/workbench/activity/deleteRemark.do".equals(path)) {
            deleteRemark(req, resp);
        } else if ("/workbench/activity/saveRemark.do".equals(path)) {
            saveRemark(req, resp);
        } else if ("/workbench/activity/getActivityRemark.do".equals(path)) {
            getActivityRemark(req, resp);
        } else if ("/workbench/activity/updateRemark.do".equals(path)) {
            updateRemark(req, resp);
        }
    }

    private void updateRemark(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("↓ ==== 进入修改市场活动备注控制器 ==== ↓");

        ActivityRemark activityRemark = new ActivityRemark();
        activityRemark.setId(req.getParameter("remarkID"));
        activityRemark.setNoteContent(req.getParameter("newRemarkText"));
        activityRemark.setEditBy(req.getParameter("editBy"));
        activityRemark.setEditTime(DateTimeUtil.getSysTime());
        activityRemark.setEditFlag("1");


        System.out.println("------ 修改的市场活动备注 ------");
        System.out.println("------> 备注ID：" + activityRemark.getId());
        System.out.println("------> 修改后备注内容：" + activityRemark.getNoteContent());
        System.out.println("------> 备注修改者：" + activityRemark.getEditBy());
        System.out.println("------> 备注修改时间：" + activityRemark.getEditTime());
        System.out.println("------> 备注是否编辑过：" + activityRemark.getEditFlag());

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean successFlag = activityService.updateRemark(activityRemark);
        System.out.println("------> 修改结果：" + successFlag);
        System.out.println("------------------------------");

        Map<String, Object> map = new HashMap<>();
        map.put("success", successFlag);
        map.put("remark", activityRemark);

        PrintJson.printJsonObj(resp, map);

        System.out.println("↑ ==== 退出修改市场活动备注控制器 ==== ↑");
    }

    private void getActivityRemark(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("↓ ==== 进入获取市场活动备注控制器 ==== ↓");

        String remarkID = req.getParameter("remarkID");
        System.out.println("------> 需要获取的备注ID：" + remarkID);


        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        ActivityRemark activityRemark = activityService.getActivityRemarkByID(remarkID);

        System.out.println("------ 获取的市场活动备注 ------");
        System.out.println("------> 备注ID：" + activityRemark.getId());
        System.out.println("------> 备注内容：" + activityRemark.getNoteContent());
        System.out.println("------> 备注创建者：" + activityRemark.getCreateBy());
        System.out.println("------> 备注修改者：" + activityRemark.getEditBy());
        System.out.println("------> 备注是否编辑过：" + activityRemark.getEditFlag());
        System.out.println("------------------------------");

        PrintJson.printJsonObj(resp, activityRemark);

        System.out.println("↑ ==== 退出获取市场活动备注控制器 ==== ↑");
    }

    private void saveRemark(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("↓ ==== 进入保存市场活动备注控制器 ==== ↓");

        ActivityRemark activityRemark = new ActivityRemark();
        activityRemark.setId(UUIDUtil.getUUID());
        activityRemark.setNoteContent(req.getParameter("text"));
        activityRemark.setCreateTime(DateTimeUtil.getSysTime());
        activityRemark.setCreateBy(req.getParameter("createBy"));
        activityRemark.setEditFlag("0");
        activityRemark.setActivityId(req.getParameter("activityID"));

        System.out.println("------> 增加的备注活动ID：" + activityRemark.getActivityId());
        System.out.println("------> 增加的备注ID：" + activityRemark.getId());
        System.out.println("------> 增加的备注内容：" + activityRemark.getNoteContent());
        System.out.println("------> 增加的备注创建者：" + activityRemark.getCreateBy());


        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean successFlag = activityService.saveRemark(activityRemark);

        Map<String, Object> map = new HashMap<>();
        map.put("success", successFlag);
        map.put("remark", activityRemark);


        PrintJson.printJsonObj(resp, map);

        System.out.println("↑ ==== 退出保存市场活动备注控制器 ==== ↑");
    }

    private void deleteRemark(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("↓ ==== 进入删除市场活动备注控制器 ==== ↓");

        String remarkID = req.getParameter("id");

        System.out.println("------> 请求的活动ID：" + remarkID);

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean successFlag = activityService.deleteRemark(remarkID);

        System.out.println("------> 执行结果：" + successFlag);

        PrintJson.printJsonFlag(resp, successFlag);

        System.out.println("↑ ==== 退出删除市场活动备注控制器 ==== ↑");

    }

    private void getActivityRemarkList(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("↓ ==== 进入获取市场活动备注列表控制器 ==== ↓");

        String activityID = req.getParameter("id");
        System.out.println("------> 请求的活动ID：" + activityID);

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<ActivityRemark> remarkList = activityService.getActivityRemarkList(activityID);

        System.out.println("------ 获取的市场活动备注列表 ------");
        for (ActivityRemark ar :
                remarkList) {
            System.out.println("------> 备注ID：" + ar.getId());
            System.out.println("------> 备注内容：" + ar.getNoteContent());
            System.out.println("------> 备注创建者：" + ar.getCreateBy());
            System.out.println("------> 备注是否编辑过：" + ar.getEditFlag());
            System.out.println("------> 备注编辑者：" + ar.getEditBy());
            System.out.println("------                ------");


        }
        System.out.println("--------------------------------");

        PrintJson.printJsonObj(resp, remarkList);

        System.out.println("↑ ==== 退出获取市场活动备注列表控制器 ==== ↑");
    }

    private void detail(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("↓ ==== 进入获取市场活动详情控制器 ==== ↓");

        String activityID = req.getParameter("id");
        System.out.println("------> 请求的活动ID：" + activityID);

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Activity activity = activityService.getActivityAndOwnerByID(activityID);

        System.out.println("------ 获取的市场活动 ------");
        System.out.println("------> 活动ID：" + activity.getId());
        System.out.println("------> 活动名：" + activity.getName());
        System.out.println("------> 活动所有者：" + activity.getOwner());
        System.out.println("------> 活动修改者：" + activity.getEditBy());
        System.out.println("------> 活动描述：" + activity.getDescription());
        System.out.println("-------------------------");

        req.setAttribute("activity", activity);

        try {
            req.getRequestDispatcher("/workbench/activity/detail.jsp").forward(req, resp);

        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }

        System.out.println("↑ ==== 退出获取市场活动详情控制器 ==== ↑");

    }

    private void updateActivity(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("↓ ==== 进入修改市场活动控制器 ==== ↓");

        Activity activity = new Activity();
        activity.setId(req.getParameter("id"));
        activity.setOwner(req.getParameter("owner"));
        activity.setName(req.getParameter("name"));
        activity.setStartDate(req.getParameter("startDate"));
        activity.setEndDate(req.getParameter("endDate"));
        activity.setCost(req.getParameter("cost"));
        activity.setDescription(req.getParameter("description"));
        activity.setEditTime(DateTimeUtil.getSysTime());
        activity.setEditBy(req.getParameter("editBy"));

        System.out.println("------> 修改的活动ID：" + activity.getId());
        System.out.println("------> 修改时间：" + activity.getEditTime());
        System.out.println("------> 修改者：" + activity.getEditBy());

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean successFlag = activityService.updateActivity(activity);

        PrintJson.printJsonFlag(resp, successFlag);

        System.out.println("↑ ==== 退出修改市场活动控制器 ==== ↑");
    }

    private void getUserAndActivity(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("↓ ==== 进入获取市场活动与用户控制器 ==== ↓");
        String activityID = req.getParameter("activityID");

        System.out.println("------> 获取的活动ID：" + activityID);

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Activity activity = activityService.getActivityByID(activityID);

        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        // userService动态代理后第一次使用
        List<User> userList = userService.getUserList();
        // userService.getUserList()调用完毕后
        // 在方法加强 TransactionInvocationHandler 中会自动关闭本次使用的SqlSession 并提交事务
        // 这时进行下一次userService.getUserByActivityOwner()调用 因为SqlSession已经关闭 事务已提交 所以会抛出异常
        // User user = userService.getUserByActivityOwner(activity.getOwner());


        System.out.println("------> 获取的活动名：" + activity.getName());
        System.out.println("------> 获取的活动所有者：" + activity.getOwner());
        System.out.println("------ 获取的用户列表 ------");
        for (User u :
                userList) {
            System.out.println("------> 获取的用户名：" + u.getName());
        }
        System.out.println("--------------------------");

        Map<String, Object> map = new HashMap<>();
        map.put("activity", activity);
        map.put("userList", userList);

        PrintJson.printJsonObj(resp, map);

        System.out.println("↑ ==== 退出获取市场活动与用户控制器 ==== ↑");
    }

    private void deleteActivity(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("↓ ==== 进入删除市场活动控制器 ==== ↓");

        String[] activityIDs = req.getParameterValues("id");
        System.out.println("------ 删除的活动ID ------");
        for (String id :
                activityIDs) {
            System.out.println(id);
        }
        System.out.println("-------------------");

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean successFlag = activityService.deleteActivity(activityIDs);
        System.out.println("------> 执行结果：" + successFlag);

        PrintJson.printJsonFlag(resp, successFlag);

        System.out.println("↑ ==== 退出删除市场活动控制器 ==== ↑");
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
        System.out.println("↓ ==== 进入保存市场活动控制器 ==== ↓");

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

        System.out.println("------ 添加的活动信息 ------");
        System.out.println(activity);
        System.out.println("-------------------");

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        Boolean successFlag = activityService.save(activity);

        System.out.println("------> 执行结果：" + successFlag);

        PrintJson.printJsonFlag(resp, successFlag);

        System.out.println("↑ ==== 退出保存市场活动控制器 ==== ↑");
    }

    private void getNames(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("↓ ==== 进入获取所有用户控制器 ==== ↓");

        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> userList = userService.getUserList();

        System.out.println("------ 获取的用户列表 ------");
        for (User user :
                userList) {
            System.out.println("------> 获取的用户名：" + user.getName());
        }
        System.out.println("-------------------");

        // 返回用户名json数组
        if (userList.size() > 0) {
            PrintJson.printJsonObj(resp, userList);
        }

        System.out.println("↑ ==== 退出获取所有用户控制器 ==== ↑");
    }


}
