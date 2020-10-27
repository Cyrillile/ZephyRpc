package com.recocozephyr.rpc.starter;

import com.recocozephyr.rpc.netty.ClientExecutor;
import org.apache.commons.lang3.time.StopWatch;

import java.util.concurrent.CountDownLatch;

/**
 * @AUTHOR: Cyril (https://github.com/Cyrillile)
 * @DATE: 2020/10/27 11:53
 * @DESCRIPTIONS:
 */
public class ZephyRpcCallTest {
    public static void main(String[] args) throws InterruptedException {
        final ClientExecutor clientExecutor = new ClientExecutor("127.0.0.1:18888");
        int parallel = 10000;

        //计时
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        CountDownLatch signal = new CountDownLatch(1);
        CountDownLatch finish = new CountDownLatch(parallel);

        for (int i = 0; i < parallel; i++) {
            CallServiceTask callServiceTask = new CallServiceTask(signal, finish, clientExecutor, i);
            new Thread(callServiceTask).start();
        }
        System.out.println("Finished!");
        signal.countDown();
        finish.await();

        stopWatch.stop();
        System.out.println("ZephyRpc 10000 calls time: " + stopWatch.getTime());
        clientExecutor.stop();
    }
}
