package com.alsw.crm.workbench.dao;

import com.alsw.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkDao {
    
    Integer getRemarkCountByAids(String[] activityIDs);

    Integer deleteRemarkByAids(String[] activityIDs);

    int getRemarkCountByAID(String activityID);

    List<ActivityRemark> getRemarkListByAID(String activityID);

    int deleteRemark(String remarkID);

    int saveRemark(ActivityRemark activityRemark);

    ActivityRemark getActivityRemarkByID(String remarkID);

    int updateRemark(ActivityRemark remark);
}
