package com.alsw.crm.workbench.dao;

public interface ActivityRemarkDao {
    
    Integer getRemarkCountByAids(String[] activityIDs);

    Integer deleteRemarkByAids(String[] activityIDs);
}
