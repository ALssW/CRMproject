package com.alsw.crm.workbench.dao;

import com.alsw.crm.settings.domain.User;
import com.alsw.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityDao {

    int saveActivity(Activity activity);

    int getTotalByCondition(Map<String, Object> pageMap);

    List<Activity> getActivityList(Map<String, Object> pageMap);
}
