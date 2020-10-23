package com.recocozephyr.rpc.common;

import java.util.concurrent.*;

/**
 * @AUTHOR: Cyril (https://github.com/Cyrillile)
 * @DATE: 2020/10/20 16:51
 * @DESCRIPTIONS: 返回一个ThreadPoolExecutor
 */
public class RpcThreadPool {
    public static Executor getInstance(int threads, int queues) {
        String name = "ZephyRpcThreadPool";
        return new ThreadPoolExecutor(threads, threads, 0, TimeUnit.MILLISECONDS,
                queues == 0 ? new SynchronousQueue<Runnable>() :
                        (queues < 0 ? new LinkedBlockingQueue<Runnable>() :
                        new LinkedBlockingQueue<Runnable>(queues)),
                new NamedThreadFactory(name), new AbortPolicyWithReport(name));
    }
}
