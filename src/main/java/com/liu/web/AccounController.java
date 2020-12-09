package com.liu.web;

import com.liu.com.liu.annonation.MyAutowired;
import com.liu.com.liu.annonation.MyController;
import com.liu.com.liu.annonation.MyRequestParam;
import com.liu.com.liu.annonation.Security;
import com.liu.service.AccountService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@MyController(value = "accounController")
@MyRequestParam(value = "/test")
@Security(value = {"zhangsan", "lisi"})
public class AccounController {

    @MyAutowired(value = "accountServiceImpl")
    private AccountService accountService;

    @MyRequestParam(value = "/test")

    public String test(HttpServletRequest request, HttpServletResponse response, String username) {
        System.out.println("test-->test:" + username);
        accountService.test();
        return "";
    }
}
