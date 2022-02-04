package com.alsw.crm.settings.service.impl;

import com.alsw.crm.exception.LoginException;
import com.alsw.crm.settings.dao.UserDao;
import com.alsw.crm.settings.domain.User;
import com.alsw.crm.settings.service.UserService;
import com.alsw.crm.utils.DateTimeUtil;
import com.alsw.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {

    private final UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    @Override
    public User login(String loginAct, String loginPwd, String ip) throws LoginException {
        Map<String, String> map = new HashMap<>();
        map.put("loginAct", loginAct);
        map.put("loginPwd", loginPwd);

        User user = userDao.login(map);

        if (user == null) {
            throw new LoginException("账号密码错误");
        }
        // 执行到此 账号密码正确
        // 继续验证 失效时间 锁定状态 ip
        String expireTime = user.getExpireTime();
        String currenTime = DateTimeUtil.getSysTime();

        if (expireTime.compareTo(currenTime) < 0) {
            throw new LoginException("账号已失效");
        }

        String lockState = user.getLockState();
        if ("0".equals(lockState)) {
            throw new LoginException("账号已锁定");
        }

        String allowIps = user.getAllowIps();
        if (!allowIps.contains(ip)) {
            throw new LoginException("ip地址受限");
        }


        return user;
    }

    @Override
    public List<User> getUserList() {
        return userDao.getUserList();
    }

    @Override
    public User getUserByActivityOwner(String owner) {
        User user = null;
        user = userDao.getUserByActivityOwner(owner);

        return user;
    }

}
