package com.alsw.crm.workbench.web.controller;

import com.alsw.crm.settings.domain.User;
import com.alsw.crm.settings.service.UserService;
import com.alsw.crm.settings.service.impl.UserServiceImpl;
import com.alsw.crm.utils.DateTimeUtil;
import com.alsw.crm.utils.PrintJson;
import com.alsw.crm.utils.ServiceFactory;
import com.alsw.crm.utils.UUIDUtil;
import com.alsw.crm.workbench.domain.Activity;
import com.alsw.crm.workbench.domain.Clue;
import com.alsw.crm.workbench.domain.ClueActivityRelation;
import com.alsw.crm.workbench.service.ActivityService;
import com.alsw.crm.workbench.service.ClueService;
import com.alsw.crm.workbench.service.impl.ActivityServiceImpl;
import com.alsw.crm.workbench.service.impl.ClueServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("↓ ==== 进入Clue控制器 ==== ↓");

        String path = req.getServletPath();
        if ("/workbench/clue/getUserList.do".equals(path)) {
            getUserList(req, resp);
        } else if ("/workbench/clue/saveClue.do".equals(path)) {
            saveClue(req, resp);
        } else if ("/workbench/clue/detail.do".equals(path)) {
            detail(req, resp);
        } else if ("/workbench/clue/getDetailRelation.do".equals(path)) {
            getDetailRelation(req, resp);
        } else if ("/workbench/clue/removeRelation.do".equals(path)) {
            removeRelation(req, resp);
        } else if ("/workbench/clue/getAllActivity.do".equals(path)) {
            getAllActivity(req, resp);
        } else if ("/workbench/clue/addDetailRelation.do".equals(path)) {
            addDetailRelation(req, resp);
        }
    }

    private void addDetailRelation(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("==== 添加线索关联市场活动 ====");

        String[] activityIDs = req.getParameterValues("activityID");
        String clueID = req.getParameter("clueID");
        List<ClueActivityRelation> relationList = new ArrayList<>();

        for (String activityID :
                activityIDs) {
            ClueActivityRelation relation = new ClueActivityRelation();

            relation.setId(UUIDUtil.getUUID());
            relation.setClueId(clueID);
            relation.setActivityId(activityID);
            relationList.add(relation);
        }

        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean successFlag = clueService.addDetailRelation(relationList);

        PrintJson.printJsonFlag(resp, successFlag);
    }

    private void getAllActivity(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("===== 搜索activity =====");
        String name = req.getParameter("name");
        String clueID = req.getParameter("clueID");
        boolean successFlag = false;
        Map<String, String> map1 = new HashMap<>();

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> activityList;
        if ("".equals(name)) {
            activityList = activityService.getAllActivityNotClueId(clueID);
        } else {
            map1.put("name",name);
            map1.put("clueID",clueID);
            activityList = activityService.getAllActivityByNameNotClueId(map1);
        }
        if (activityList.size() != 0) {
            successFlag = true;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("success", successFlag);
        map.put("activityList", activityList);

        PrintJson.printJsonObj(resp, map);

    }

    private void removeRelation(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("===== 移除线索关系 =====");

        String id = req.getParameter("id");
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean successFlag = clueService.removeRelation(id);
        PrintJson.printJsonFlag(resp, successFlag);

    }

    private void getDetailRelation(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("===== 获取线索关系 =====");

        String clueID = req.getParameter("clueID");

        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        List<Activity> activityList = clueService.getDetailActivityList(clueID);

        PrintJson.printJsonObj(resp, activityList);
    }

    private void detail(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("↓ ==== 进入查找线索控制器 ==== ↓");

        String clueID = req.getParameter("id");

        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Clue clue = clueService.getDetail(clueID);

        req.setAttribute("clue", clue);
        try {
            req.getRequestDispatcher("/workbench/clue/detail.jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }

        System.out.println("↑ ==== 退出查找线索控制器 ==== ↑");

    }

    private void saveClue(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("↓ ==== 进入保存线索控制器 ==== ↓");

        Clue clue = new Clue();
        clue.setId(UUIDUtil.getUUID());
        clue.setFullname(req.getParameter("fullname"));
        clue.setAppellation(req.getParameter("appellation"));
        clue.setOwner(req.getParameter("owner"));
        clue.setCompany(req.getParameter("company"));
        clue.setJob(req.getParameter("job"));
        clue.setEmail(req.getParameter("email"));
        clue.setPhone(req.getParameter("phone"));
        clue.setWebsite(req.getParameter("website"));
        clue.setMphone(req.getParameter("mphone"));
        clue.setState(req.getParameter("state"));
        clue.setSource(req.getParameter("source"));
        clue.setCreateBy(req.getParameter("createBy"));
        clue.setCreateTime(DateTimeUtil.getSysTime());
        clue.setDescription(req.getParameter("description"));
        clue.setContactSummary(req.getParameter("contactSummary"));
        clue.setNextContactTime(req.getParameter("nextContactTime"));
        clue.setAddress(req.getParameter("address"));

        System.out.println("---- 保存的线索 ----");
        System.out.println("----> 线索的ID：" + clue.getId());
        System.out.println("----> 线索的所有者：" + clue.getOwner());
        System.out.println("----> 线索的姓名：" + clue.getFullname());

        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean successFlag = clueService.saveClue(clue);

        PrintJson.printJsonFlag(resp, successFlag);

        System.out.println("↑ ==== 退出保存线索控制器 ==== ↑");
    }

    private void getUserList(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("↓ ==== 进入获取用户列表控制器 ==== ↓");

        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> userList = userService.getUserList();

        System.out.println("---- 获取的用户列表 ----");

        for (User u :
                userList) {
            System.out.println("----> 获取的用户名：" + u.getName());
            System.out.println("----> 获取的用户ID：" + u.getId());
            System.out.println("---- ------- ----");

        }

        PrintJson.printJsonObj(resp, userList);

        System.out.println("↑ ==== 退出获取用户列表控制器 ==== ↑");

    }

}
