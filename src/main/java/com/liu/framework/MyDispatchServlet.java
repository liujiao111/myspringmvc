package com.liu.framework;

import com.liu.com.liu.annonation.MyAutowired;
import com.liu.com.liu.annonation.MyController;
import com.liu.com.liu.annonation.MyService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public class MyDispatchServlet extends HttpServlet {

    private Properties properties = new Properties();

    private List<String> classNames = new ArrayList<String>();

    private Map<String, Object> ioc = new HashMap<String, Object>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        //初始化逻辑

        //1.加载配置文件
        String contextConfigLocation = config.getServletContext().getInitParameter("contextConfigLocation");

        doLoadConfig(contextConfigLocation);

        //2.扫码注解
        doScanAnnonation(properties.getProperty("scanPackage"));

        //3.初始化IOC bean容器
        try {
            doInitInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        //4.处理bean对象之间依赖
        doAutowired();

        //5.url映射配置
        doInitMappings();

        System.out.println("springmvc初始化完成");

    }

    /**
     * url映射配置
     */
    private void doInitMappings() {

    }

    /**
     * 处理bean对象之间的依赖关系
     */
    private void doAutowired() {
        if(ioc.isEmpty()) {return;}

        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            final Field[] declaredFields = entry.getValue().getClass().getDeclaredFields();
            for (Field declaredField : declaredFields) {
                if(! declaredField.isAnnotationPresent(MyAutowired.class)) {
                    continue;
                }

                MyAutowired annotation = declaredField.getAnnotation(MyAutowired.class);
                String beanName = annotation.value();
                if("".equals(beanName.trim())) {
                    //如果没有配置bean id，则需要根据当前字段类型进行注入
                    beanName = declaredField.getType().getName();
                }

                declaredField.setAccessible(true);
                try {
                    declaredField.set(entry.getValue(), ioc.get(beanName));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 初始化IOC bean容器
     */
    private void doInitInstance() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        if(classNames.size() == 0) {return;}

        for (int i = 0; i < classNames.size(); i++) {
            String className = classNames.get(i);
            final Class<?> aClass = Class.forName(className);

            //区分controller和service
            if(aClass.isAnnotationPresent(MyController.class)) {
                String simpleName = aClass.getSimpleName();
                String lowerFirstName = lowerFirst(simpleName);
                final Object o = aClass.newInstance();
                ioc.put(lowerFirstName, o);
            } else if(aClass.isAnnotationPresent(MyService.class)) {
                final MyService annotation = aClass.getAnnotation(MyService.class);
                String beanName = annotation.value();

                //如果指定了ID，就以指定的id为bean name
                if(! "".equalsIgnoreCase(beanName.trim())) {
                    ioc.put(beanName, aClass.newInstance());
                } else {
                    //如果没有指定id,就以类名首字母小写为bean name
                    beanName = lowerFirst(aClass.getSimpleName());
                    ioc.put(beanName, aClass.newInstance());
                }
            }

            //service层往往是有接口的，因此需要再以接口名为ID，存入一份数据放入IOC容器中，便于后期跟进接口类型注入
            final Class<?>[] interfaces = aClass.getInterfaces();
            for (Class<?> anInterface : interfaces) {
                final String interfaceName = anInterface.getName();
                ioc.put(interfaceName, aClass.newInstance());
            }
        }
    }

    /**
     * 将字符串首字母转换为小写
     * @param simpleName
     * @return
     */
    private String lowerFirst(String simpleName) {
        final char[] chars = simpleName.toCharArray();
        if('A' <= chars[0] && chars[0] <= 'Z') {
            chars[0] += 32;
        }
        return String.valueOf(chars);
    }

    /**
     * 扫码注解
     * @param scanPackage
     */
    private void doScanAnnonation(String scanPackage) {
        String scanPackagePath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + scanPackage.replaceAll("\\.", "/");
        File pack = new File(scanPackagePath);
        final File[] files = pack.listFiles();
        for (File file : files) {
            if(file.isDirectory()) {
                doScanAnnonation(scanPackage + "." + file.getName());
            } else if(file.getName().endsWith(".class")){
                String className = scanPackage + "." + file.getName().replaceAll(".class", "");
                classNames.add(className);
            }
        }
    }

    /**
     * 加载配置文件
     * @param contextConfigLocation
     */
    private void doLoadConfig(String contextConfigLocation) {
        try {
            properties.load(this.getClass().getClassLoader().getResourceAsStream(contextConfigLocation));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
