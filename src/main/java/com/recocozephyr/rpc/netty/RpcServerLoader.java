package com.recocozephyr.rpc.netty;

import com.recocozephyr.rpc.common.RpcThreadPool;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.omg.SendingContext.RunTime;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @AUTHOR: Cyril (https://github.com/Cyrillile)
 * @DATE: 2020/10/20 16:42
 * @DESCRIPTIONS:
 */
public class RpcServerLoader {
    //singleton
    private volatile static RpcServerLoader rpcServerLoader;

    private final static int parallel = Runtime.getRuntime().availableProcessors() * 2;
    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup(parallel);
    private static ThreadPoolExecutor executor = (ThreadPoolExecutor) RpcThreadPool.getInstance(16, -1);
    private ClientChannelHandler clientChannelHandler;

    private Lock lock = new ReentrantLock();
    private Condition signal = lock.newCondition();

    public static RpcServerLoader getInstance() {
        if (rpcServerLoader == null) {
            synchronized (RpcServerLoader.class) {
                if (rpcServerLoader == null) {
                    rpcServerLoader = new RpcServerLoader();
                }
            }
        }
        return rpcServerLoader;
    }

    public void load(String serverAddress) {
        String[] ipPort = serverAddress.split(":");
        if (ipPort.length == 2) {
            String ip = ipPort[0];
            int port = Integer.parseInt(ipPort[1]);
            final InetSocketAddress inetSocketAddress = new InetSocketAddress(ip, port);
            executor.submit(new ClientServiceTask(eventLoopGroup, inetSocketAddress,
                    this));
        }
    }

    public ClientChannelHandler getClientChannelHandler() throws InterruptedException {
        try {
            lock.lock();
            //wait
            if (clientChannelHandler == null) {
                signal.await();
            }
            return clientChannelHandler;
        }finally {
            lock.unlock();
        }
    }

    public void setClientChannelHandler(ClientChannelHandler clientChannelHandler) {
        try {
            lock.lock();
            this.clientChannelHandler = clientChannelHandler;
            signal.signalAll();
        }finally {
            lock.unlock();
        }
    }

    public void unload() {
        clientChannelHandler.close();
    }
}
