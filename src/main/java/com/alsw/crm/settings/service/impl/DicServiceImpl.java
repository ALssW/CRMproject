package com.alsw.crm.settings.service.impl;

import com.alsw.crm.exception.LoginException;
import com.alsw.crm.settings.dao.DicTypeDao;
import com.alsw.crm.settings.dao.DicValueDao;
import com.alsw.crm.settings.dao.UserDao;
import com.alsw.crm.settings.domain.DicType;
import com.alsw.crm.settings.domain.DicValue;
import com.alsw.crm.settings.domain.User;
import com.alsw.crm.settings.service.DicService;
import com.alsw.crm.settings.service.UserService;
import com.alsw.crm.utils.DateTimeUtil;
import com.alsw.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImpl implements DicService {

    private final DicTypeDao dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    private final DicValueDao dicValueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);

    @Override
    public Map<String, List<DicValue>> getAll() {
        Map<String, List<DicValue>> dicMap = new HashMap<>();

        List<DicType> dicTypes = dicTypeDao.getTypeList();

        for (DicType dicType:
             dicTypes) {
            String code = dicType.getCode();
            List<DicValue> dicValues = dicValueDao.getValueByCode(code);

            dicMap.put(code, dicValues);
        }


        return dicMap;
    }
}
