package com.alsw.crm.workbench.service.impl;

import com.alsw.crm.utils.SqlSessionUtil;
import com.alsw.crm.vo.PaginationVO;
import com.alsw.crm.workbench.dao.ActivityDao;
import com.alsw.crm.workbench.domain.Activity;
import com.alsw.crm.workbench.service.ActivityService;

import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {

    private final ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);

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
        System.out.println("------> 查询到的活动个数：" + dataList.size());

        // 数据封装入vo类中
        activityList.setTotal(total);
        activityList.setDataList(dataList);

        return activityList;
    }
}
