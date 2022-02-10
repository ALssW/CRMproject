package com.alsw.crm.workbench.service;

import com.alsw.crm.workbench.domain.Activity;
import com.alsw.crm.workbench.domain.Clue;
import com.alsw.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueService {

    boolean saveClue(Clue clue);

    Clue getDetail(String clueID);

    List<ClueActivityRelation> getDetailRelation(String clueID);

    List<Activity> getDetailActivityList(String clueID);

    boolean removeRelation(String id);

    boolean addDetailRelation(List<ClueActivityRelation> relationList);
}
