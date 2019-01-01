package com.mypinyougou.dubbox.consumer;

import com.mypinyougou.dubbox.controller.HelloController;
import com.mypinyougou.dubbox.provider.service.HelloService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class HelloConsumer {
    public static void main(String[] args) {
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");

        /*HelloService helloService = (HelloService) ac.getBean("helloService");
        String result = helloService.sayHello("小明");
        System.out.println(result);*/

        HelloController helloController = (HelloController) ac.getBean("helloController");
        String result = helloController.sayHello("小花");
        System.out.println(result);
    }
}
