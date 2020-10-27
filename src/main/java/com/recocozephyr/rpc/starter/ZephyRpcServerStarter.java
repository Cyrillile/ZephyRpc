package com.recocozephyr.rpc.starter;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @AUTHOR: Cyril (https://github.com/Cyrillile)
 * @DATE: 2020/10/27 15:14
 * @DESCRIPTIONS:
 */
public class ZephyRpcServerStarter {
    public static void main(String[] args) {
        ApplicationContext  applicationContext = new ClassPathXmlApplicationContext(
                "spring-config.xml");
    }
}
