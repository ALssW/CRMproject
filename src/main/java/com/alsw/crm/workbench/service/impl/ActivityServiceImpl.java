package com.alsw.crm.workbench.service.impl;

import com.alsw.crm.utils.SqlSessionUtil;
import com.alsw.crm.vo.PaginationVO;
import com.alsw.crm.workbench.dao.ActivityDao;
import com.alsw.crm.workbench.dao.ActivityRemarkDao;
import com.alsw.crm.workbench.domain.Activity;
import com.alsw.crm.workbench.domain.ActivityRemark;
import com.alsw.crm.workbench.service.ActivityService;

import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {

    private final ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private final ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);

    @Override
    public Boolean save(Activity activity) {
        boolean flag = true;

        int count = activityDao.saveActivity(activity);

        System.out.println("------> 影响行数：" + count);

        if (count != 1) {
            flag = false;
        }

        return flag;
    }

    @Override
    public PaginationVO<Activity> getActivityList(Map<String, Object> pageMap) {
        PaginationVO<Activity> activityList = new PaginationVO<>();

        // 获取查询总记录数
        int total = activityDao.getTotalByCondition(pageMap);
        System.out.println("------> 总记录数：" + total);

        // 获取dataList
        List<Activity> dataList = activityDao.getActivityList(pageMap);
        for (Activity ac :
                dataList) {
            System.out.println("------> 查询到的活动名：" + ac.getName());
            System.out.println("------> 查询到的活动ID：" + ac.getId());
        }
        System.out.println("------> 查询到的活动个数：" + dataList.size());

        // 数据封装入vo类中
        activityList.setTotal(total);
        activityList.setDataList(dataList);

        return activityList;
    }

    @Override
    public boolean deleteActivity(String[] activityIDs) {
        boolean successFlag = false;

        // 删除市场活动的同时，还要删除相应的备注
        // 查询出需要删除的备注的数量
        Integer needCount = activityRemarkDao.getRemarkCountByAids(activityIDs);
        System.out.println("------> 需要删除的备注数：" + needCount);

        // 删除备注 并返回实际删除的数量
        Integer deleteCount = activityRemarkDao.deleteRemarkByAids(activityIDs);
        System.out.println("------> 实际删除的备注数：" + deleteCount);

        // 删除市场活动
        int count = activityDao.deleteActivity(activityIDs);
        System.out.println("------> 删除的市场活动数：" + count);
        if (count == activityIDs.length && needCount == deleteCount) {
            successFlag = true;
        }

        return successFlag;
    }

    @Override
    public Activity getActivityByID(String activityID) {
        Activity activity = null;
        activity = activityDao.getActivityByID(activityID);

        return activity;
    }

    @Override
    public boolean updateActivity(Activity activity) {
        boolean successFlag = false;

        int count = activityDao.updateActivity(activity);

        if (count == 1) {
            successFlag = true;
        }

        return successFlag;
    }
}
