package com.liu.service.impl;

import com.liu.com.liu.annonation.MyService;
import com.liu.service.AccountService;

@MyService(value = "accountServiceImpl")
public class AccountServiceImpl implements AccountService {
    @Override
    public void test() {
    }
}
