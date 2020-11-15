package com.recocozephyr.rpc.starter;

import com.recocozephyr.rpc.common.SerializeProtocol;
import com.recocozephyr.rpc.netty.ClientExecutor;
import com.recocozephyr.rpc.serialize.Serialize;
import org.apache.commons.lang3.time.StopWatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @AUTHOR: Cyril (https://github.com/Cyrillile)
 * @DATE: 2020/10/27 11:53
 * @DESCRIPTIONS:
 */
public class ZephyRpcCallTest {
    public static void parallelTask(ClientExecutor clientExecutor, int parallel,
                                    String serverAddresss,
                                    SerializeProtocol serializeProtocol) throws InterruptedException {
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
        System.out.println(String.format("ZephyRpc[%s] 10000 calls time:%s ",
                serializeProtocol, stopWatch.getTime()));

    }

    public static void jdknativeTask(ClientExecutor clientExecutor, int parallel) throws InterruptedException {
        String serverAddress = "127.0.0.1:18887";
        SerializeProtocol serializeProtocol = SerializeProtocol.JDKSERIALIZE;
        clientExecutor.setClientExecutor(serverAddress, serializeProtocol);
        ZephyRpcCallTest.parallelTask(clientExecutor, parallel, serverAddress, serializeProtocol);
        TimeUnit.SECONDS.sleep(3);
    }
    public static void kryoTask(ClientExecutor clientExecutor, int parallel) throws InterruptedException {
        String serverAddress = "127.0.0.1:18888";
        SerializeProtocol serializeProtocol = SerializeProtocol.KRYOSERIALIZE;
        clientExecutor.setClientExecutor(serverAddress, serializeProtocol);
        ZephyRpcCallTest.parallelTask(clientExecutor, parallel, serverAddress, serializeProtocol);
        TimeUnit.SECONDS.sleep(3);
    }
    public static void hessianTask(ClientExecutor clientExecutor, int parallel) throws InterruptedException {
        String serverAddress = "127.0.0.1:18889";
        SerializeProtocol serializeProtocol = SerializeProtocol.HESSIANSERIALIZE;
        clientExecutor.setClientExecutor(serverAddress, serializeProtocol);
        ZephyRpcCallTest.parallelTask(clientExecutor, parallel, serverAddress, serializeProtocol);
        TimeUnit.SECONDS.sleep(3);
    }
    public static void main(String[] args) throws Exception {
        final ClientExecutor clientExecutor = new ClientExecutor();
        int parallel = 10000;

        for (int i = 0; i < 10; i++) {
            jdknativeTask(clientExecutor, parallel);
            kryoTask(clientExecutor, parallel);
            hessianTask(clientExecutor, parallel);
            System.out.println("ZephyRpc parallel test round " + i + " is over!");
        }
        clientExecutor.stop();


    }
}
