package com.alsw.crm.workbench.dao;

import com.alsw.crm.workbench.domain.Clue;

public interface ClueDao {

    int saveClue(Clue clue);

    Clue getDetailByID(String clueID);
}
