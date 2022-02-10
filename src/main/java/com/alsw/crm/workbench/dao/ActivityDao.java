package com.alsw.crm.workbench.dao;

import com.alsw.crm.settings.domain.User;
import com.alsw.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityDao {

    int saveActivity(Activity activity);

    int getTotalByCondition(Map<String, Object> pageMap);

    List<Activity> getActivityList(Map<String, Object> pageMap);

    int deleteActivity(String[] activityIDs);

    Activity getActivityByID(String activityID);

    int updateActivity(Activity activity);

    Activity getActivityAndOwnerByID(String activityID);

    List<Activity> getAllActivityByNameNotClueId(Map<String, String> map);

    List<Activity> getAllActivityNotClueId(String clueID);
}
