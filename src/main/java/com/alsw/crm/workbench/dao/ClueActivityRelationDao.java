package com.alsw.crm.workbench.dao;

import com.alsw.crm.workbench.domain.Activity;
import com.alsw.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationDao {

    List<ClueActivityRelation> getRelationList(String clueID);

    List<Activity> getActivityList(String clueID);

    int removeRelation(String id);

    int addDetailRelation(ClueActivityRelation relationList);
}
