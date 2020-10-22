package com.recocozephyr.rpc.netty;

import com.recocozephyr.rpc.common.RpcThreadPool;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @AUTHOR: Cyril (https://github.com/Cyrillile)
 * @DATE: 2020/10/20 16:29
 * @DESCRIPTIONS:
 */
public class ServerExecutor implements ApplicationContextAware, InitializingBean{
    private static ThreadPoolExecutor theadPoolExecutor;
    private String serverAddress;
    private Map<String, Object> handlerMap = new ConcurrentHashMap<String, Object>();
    public static void submit(Runnable task){
        if (theadPoolExecutor == null) {
            synchronized (ServerExecutor.class) {
                if (theadPoolExecutor == null) {
                    theadPoolExecutor = (ThreadPoolExecutor) RpcThreadPool.getInstance(16, -1);
                }
            }
        }

    }

    public void afterPropertiesSet() throws Exception {

    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }
}
