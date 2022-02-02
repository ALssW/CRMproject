package com.alsw.crm.workbench.service.impl;

import com.alsw.crm.utils.SqlSessionUtil;
import com.alsw.crm.workbench.dao.ActivityDao;
import com.alsw.crm.workbench.service.ActivityService;

public class ActivityServiceImpl implements ActivityService {

    private final ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);

}
