package com.recocozephyr.rpc.model;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @AUTHOR: Cyril (https://github.com/Cyrillile)
 * @DATE: 2020/10/26 21:43
 * @DESCRIPTIONS: 1等待一段时间后返回response中的结果；2填充response
 */
public class CallbackInfo {
    private RequestInfo requestInfo;
    private ResponseInfo responseInfo;
    private Lock lock = new ReentrantLock();
    private Condition signal = lock.newCondition();

    public CallbackInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public Object start() throws InterruptedException {
        try {
            lock.lock();
            signal.await(10 * 1000, TimeUnit.MILLISECONDS);
            if (this.responseInfo != null) {
                System.out.println("callback : " + this.responseInfo.getResult());
                return responseInfo.getResult();
            } else {
                return null;
            }
        }finally {
            lock.unlock();
        }
    }

    public void over(ResponseInfo responseInfo) {
        try {
            lock.lock();
            signal.signal();
            this.responseInfo = responseInfo;
        }finally {
            lock.unlock();
        }
    }
}
