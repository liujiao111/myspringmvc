package com.liu.framework;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyDispatchServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        //初始化逻辑

        //1.加载配置文件
        String contextConfigLocation = config.getServletContext().getInitParameter("contextConfigLocation");
        doLoadConfig(contextConfigLocation);

        //2.扫码注解
        doScanAnnonation();

        //3.初始化IOC bean容器
        doInitInstance();

        //4.处理bean对象之间依赖
        doAutowired();

        //5.url映射配置
        doInitMappings();

        System.out.println("springmvc初始化完成");

    }

    /**
     * 加载配置文件
     * @param contextConfigLocation
     */
    private void doLoadConfig(String contextConfigLocation) {

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }


}
