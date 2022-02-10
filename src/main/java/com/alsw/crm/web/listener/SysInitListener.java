package com.alsw.crm.web.listener;

import com.alsw.crm.settings.domain.DicValue;
import com.alsw.crm.settings.service.DicService;
import com.alsw.crm.settings.service.impl.DicServiceImpl;
import com.alsw.crm.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SysInitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("==== 服务器开始初始化-上下文对象 ====");

        ServletContext application = sce.getServletContext();

        DicService dicService = (DicService) ServiceFactory.getService(new DicServiceImpl());

        Map<String, List<DicValue>> dicKVs = dicService.getAll();

        Set<String> dicKs = dicKVs.keySet();

        for (String dicK :
                dicKs) {
            application.setAttribute(dicK, dicKVs.get(dicK));
        }

        System.out.println("==== 服务器完成初始化-上下文对象 ====");

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
