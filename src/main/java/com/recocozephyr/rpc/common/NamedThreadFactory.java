package com.recocozephyr.rpc.common;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @AUTHOR: Cyril (https://github.com/Cyrillile)
 * @DATE: 2020/10/22 21:14
 * @DESCRIPTIONS: 创建线程
 */
public class NamedThreadFactory implements ThreadFactory {
    private static final AtomicInteger threadIndex = new AtomicInteger(1);
    private final String prefix;
    private final ThreadGroup threadGroup;

    public NamedThreadFactory(String prefix) {
        this.prefix = prefix + "-thread-";
        SecurityManager securityManager = System.getSecurityManager();
        threadGroup = (securityManager == null) ? Thread.currentThread().getThreadGroup() :
                securityManager.getThreadGroup();
    }

    public Thread newThread(Runnable r) {
        String threadName = prefix + threadIndex.getAndIncrement();
        Thread thread = new Thread(threadGroup, r, threadName, 0);
        thread.setDaemon(true);
        return thread;
    }
}
