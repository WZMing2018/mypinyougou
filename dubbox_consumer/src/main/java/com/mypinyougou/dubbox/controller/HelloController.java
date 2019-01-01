package com.mypinyougou.dubbox.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mypinyougou.dubbox.provider.service.HelloService;
import org.springframework.stereotype.Controller;

@Controller
public class HelloController {

    @Reference
    private HelloService helloService;

    public String sayHello(String name) {
        return helloService.sayHello(name);
    }
}
