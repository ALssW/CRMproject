package com.alsw.crm.settings.service;

import com.alsw.crm.exception.LoginException;
import com.alsw.crm.settings.domain.User;

import java.util.List;

public interface UserService {

    User login(String loginAct, String loginPwd, String ip) throws LoginException;

    List<User> getUserList();

    User getUserByActivityOwner(String owner);
}
