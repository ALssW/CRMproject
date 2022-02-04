package com.alsw.crm.workbench.service;

import com.alsw.crm.vo.PaginationVO;
import com.alsw.crm.workbench.domain.Activity;

import java.util.Map;

public interface ActivityService {

    Boolean save(Activity activity);

    PaginationVO<Activity> getActivityList(Map<String, Object> pageMap);

    boolean deleteActivity(String[] activityIDs);

    Activity getActivityByID(String activityID);

    boolean updateActivity(Activity activity);
}
