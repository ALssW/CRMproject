package com.alsw.crm.workbench.service;

import com.alsw.crm.vo.PaginationVO;
import com.alsw.crm.workbench.domain.Activity;
import com.alsw.crm.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityService {

    Boolean save(Activity activity);

    PaginationVO<Activity> getActivityList(Map<String, Object> pageMap);

    boolean deleteActivity(String[] activityIDs);

    Activity getActivityByID(String activityID);

    boolean updateActivity(Activity activity);

    Activity getActivityAndOwnerByID(String activityID);

    List<ActivityRemark> getActivityRemarkList(String activityID);

    boolean deleteRemark(String remarkID);

    boolean saveRemark(ActivityRemark activityRemark);

    ActivityRemark getActivityRemarkByID(String remarkID);

    boolean updateRemark(ActivityRemark id);

    List<Activity> getAllActivityNotClueId(String clueID);

    List<Activity> getAllActivityByNameNotClueId(Map<String, String> map);
}
