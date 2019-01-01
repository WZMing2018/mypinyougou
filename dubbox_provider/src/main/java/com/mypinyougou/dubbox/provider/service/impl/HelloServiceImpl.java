package com.mypinyougou.dubbox.provider.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.mypinyougou.dubbox.provider.service.HelloService;

@Service
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return "hello:" + name;
    }
}
