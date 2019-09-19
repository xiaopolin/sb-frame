package com.xpl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan(basePackages = "com.xpl.dao")
@EnableTransactionManagement
@SpringBootApplication
public class App {

    public static void main(String[] args) {
        ApplicationContext ac = SpringApplication.run(App.class, args);
        //随项目启动的方法
        ac.getBean(com.xpl.web.timer.SystemTimer.class).speak();
    }

}
