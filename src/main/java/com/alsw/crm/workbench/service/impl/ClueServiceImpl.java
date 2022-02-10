package com.alsw.crm.workbench.service.impl;

import com.alsw.crm.utils.SqlSessionUtil;
import com.alsw.crm.workbench.dao.ActivityDao;
import com.alsw.crm.workbench.dao.ClueActivityRelationDao;
import com.alsw.crm.workbench.dao.ClueDao;
import com.alsw.crm.workbench.domain.Activity;
import com.alsw.crm.workbench.domain.Clue;
import com.alsw.crm.workbench.domain.ClueActivityRelation;
import com.alsw.crm.workbench.service.ClueService;

import java.util.List;

public class ClueServiceImpl implements ClueService {

    private final ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private final ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
    private final ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);

    @Override
    public boolean saveClue(Clue clue) {
        boolean successFlag = false;

        int count = clueDao.saveClue(clue);
        if (count == 1) {
            successFlag = true;
        }

        return successFlag;
    }

    @Override
    public Clue getDetail(String clueID) {
        Clue clue;

        clue = clueDao.getDetailByID(clueID);

        return clue;
    }

    @Override
    public List<ClueActivityRelation> getDetailRelation(String clueID) {
        List<ClueActivityRelation> relationList;

        relationList = clueActivityRelationDao.getRelationList(clueID);

        return relationList;
    }

    @Override
    public List<Activity> getDetailActivityList(String clueID) {
        List<Activity> activityList;

        activityList = clueActivityRelationDao.getActivityList(clueID);

        return activityList;
    }

    @Override
    public boolean removeRelation(String id) {
        boolean successFlag = false;

        int count = clueActivityRelationDao.removeRelation(id);
        if (count == 1) {
            successFlag = true;
        }

        return successFlag;
    }

    @Override
    public boolean addDetailRelation(List<ClueActivityRelation> relationList) {
        boolean successFlag = false;
        int count = 0;

        for (ClueActivityRelation r :
                relationList) {
            count = clueActivityRelationDao.addDetailRelation(r);
        }
        if (count >= 1) {
            successFlag = true;
        }

        return successFlag;
    }
}
