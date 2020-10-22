package com.recocozephyr.rpc.common;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @AUTHOR: Cyril (https://github.com/Cyrillile)
 * @DATE: 2020/10/22 22:11
 * @DESCRIPTIONS:
 */
public class AbortPolicyWithReport extends ThreadPoolExecutor.AbortPolicy {
    private final String threadName;
    public AbortPolicyWithReport(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        String message = String.format("ZephyRpcServer["
            + "Thread name: %s, Pool size: %d (active: %d, core: %d, max: %d, largest: %d),"
            + "Task: %d (completed: %d),"
            + "Executor status:(isShutdown: %s, siTerminated: %s, isTerminating: %s)]",
                threadName, e.getCorePoolSize(), e.getActiveCount(), e.getCorePoolSize(),
                e.getMaximumPoolSize(), e.getLargestPoolSize(), e.getTaskCount(), e.getCompletedTaskCount(),
                e.isShutdown(), e.isTerminated(), e.isTerminating()
        );
        throw new RejectedExecutionException();
    }
}
