package com.recocozephyr.rpc.netty;

import com.google.common.util.concurrent.*;
import com.recocozephyr.rpc.common.RpcThreadPool;
import com.recocozephyr.rpc.common.SerializeProtocol;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import jdk.nashorn.internal.runtime.linker.Bootstrap;
import org.omg.SendingContext.RunTime;

import java.net.InetSocketAddress;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @AUTHOR: Cyril (https://github.com/Cyrillile)
 * @DATE: 2020/10/20 16:42
 * @DESCRIPTIONS:
 */
public class RpcServerLoader {
    //singleton
    private volatile static RpcServerLoader rpcServerLoader;
    private SerializeProtocol serializeProtocol = SerializeProtocol.JDKSERIALIZE;

    private final static int parallel = Runtime.getRuntime().availableProcessors() * 2;
    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup(parallel);
    private static ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(
            (ThreadPoolExecutor) RpcThreadPool.getInstance(16, -1));
    private ClientChannelHandler clientChannelHandler;

    private Lock lock = new ReentrantLock();
    private Condition connectStatus = lock.newCondition();
    private Condition handlerStatus = lock.newCondition();

    private RpcServerLoader(){
    }

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

    public void load(String serverAddress, SerializeProtocol serializeProtocol) {
        String[] ipPort = serverAddress.split(":");
        if (ipPort.length == 2) {
            String ip = ipPort[0];
            int port = Integer.parseInt(ipPort[1]);
            final InetSocketAddress inetSocketAddress = new InetSocketAddress(ip, port);
            ListenableFuture<Boolean> listenableFuture = listeningExecutorService.submit(
                    new ClientServiceTask(eventLoopGroup, inetSocketAddress,
                    serializeProtocol));
            Futures.addCallback(listenableFuture, new FutureCallback<Boolean>() {
                public void onSuccess(Boolean aBoolean) {
                    try {
                        lock.lock();
                        if (clientChannelHandler == null) {
                            handlerStatus.await();
                        }

                        //Futures异步回调，唤醒所有rpc等待线程
                        if (aBoolean == Boolean.TRUE && clientChannelHandler != null) {
                            connectStatus.signalAll();
                        }
                    } catch (InterruptedException e) {
                        Logger.getLogger(RpcServerLoader.class.getName()).log(Level.SEVERE, null, e);
                    }finally {
                        lock.unlock();
                    }
                }

                public void onFailure(Throwable throwable) {
                    throwable.printStackTrace();
                }
            }, listeningExecutorService);
        }
    }

    public ClientChannelHandler getClientChannelHandler() throws InterruptedException {
        try {
            lock.lock();
            //netty服务端连接完成前wait
            if (clientChannelHandler == null) {
                connectStatus.await();
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
            connectStatus.signalAll();
        }finally {
            lock.unlock();
        }
    }

    public void unload() {
        clientChannelHandler.close();
        listeningExecutorService.shutdown();
        eventLoopGroup.shutdownGracefully();
    }

    public void setSerializeProtocol(SerializeProtocol serializeProtocol) {
        this.serializeProtocol = serializeProtocol;
    }
}
