package com.alsw.crm.settings.dao;

import com.alsw.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueDao {

    List<DicValue> getValueByCode(String code);
    
}
