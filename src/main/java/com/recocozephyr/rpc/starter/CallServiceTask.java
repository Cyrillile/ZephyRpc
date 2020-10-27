package com.recocozephyr.rpc.starter;

import com.recocozephyr.rpc.netty.ClientExecutor;
import com.recocozephyr.rpc.service.TellJokes;
import sun.rmi.runtime.Log;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @AUTHOR: Cyril (https://github.com/Cyrillile)
 * @DATE: 2020/10/27 12:04
 * @DESCRIPTIONS:
 */
public class CallServiceTask implements Runnable {
    private CountDownLatch signal;
    private CountDownLatch finish;
    private ClientExecutor clientExecutor;
    private int taskNumber;

    public CallServiceTask(CountDownLatch signal, CountDownLatch finish, ClientExecutor clientExecutor, int taskNumber) {
        this.signal = signal;
        this.finish = finish;
        this.clientExecutor = clientExecutor;
        this.taskNumber = taskNumber;
    }

    public void run() {
        try {
            signal.await();
            TellJokes tellJokes = clientExecutor.execute(TellJokes.class);
            String jokes = tellJokes.tell(2);
            System.out.println("Get jokes:" + jokes);
            finish.countDown();
        } catch (InterruptedException e) {
            Logger.getLogger(CallServiceTask.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
