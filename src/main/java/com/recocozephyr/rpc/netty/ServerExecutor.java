package com.recocozephyr.rpc.netty;

import com.recocozephyr.rpc.common.NamedThreadFactory;
import com.recocozephyr.rpc.common.RpcThreadPool;
import com.recocozephyr.rpc.service.TellJokes;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolver;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.nio.channels.spi.SelectorProvider;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;

/**
 * @AUTHOR: Cyril (https://github.com/Cyrillile)
 * @DATE: 2020/10/20 16:29
 * @DESCRIPTIONS: 1、设置netty服务端 2、获取要提供服务的bean 3、执行提交的任务
 */
public class ServerExecutor implements ApplicationContextAware, InitializingBean{
    private static ThreadPoolExecutor threadPoolExecutor;
    private String serverAddress;
    private Map<String, Object> handlerMap = new ConcurrentHashMap<String, Object>();

    public ServerExecutor(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public static void submit(Runnable task){
        if (threadPoolExecutor == null) {
            synchronized (ServerExecutor.class) {
                if (threadPoolExecutor == null) {
                    threadPoolExecutor = (ThreadPoolExecutor) RpcThreadPool.getInstance(16, -1);
                }
            }
        }
        threadPoolExecutor.submit(task);
    }

    public void afterPropertiesSet() throws Exception {
        ThreadFactory threadFactory = new NamedThreadFactory("ZephyRpc Thread Factory");
        int parallel = Runtime.getRuntime().availableProcessors() * 2;
        EventLoopGroup master = new NioEventLoopGroup();
        EventLoopGroup slaves = new NioEventLoopGroup(parallel, threadFactory, SelectorProvider.provider());

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(master, slaves)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ServerChannelInitializer(handlerMap))
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            String[] ipAddress = serverAddress.split(":");
            if (ipAddress.length == 2) {
                String ip = ipAddress[0];
                int port = Integer.parseInt(ipAddress[1]);
                ChannelFuture channelFuture = serverBootstrap.bind(ip, port).sync();
                System.out.println("ZephyRpc server starts successfully, IP: " + ip + ", port: " + port);
                channelFuture.channel().closeFuture().sync();
            } else {
                System.out.println("ZephyRpc server starts failed! ");
            }
        }finally {
            master.shutdownGracefully();
            slaves.shutdownGracefully();
        }
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        try {
            TellJokes tellJokes = (TellJokes) applicationContext.getBean(Class.forName("" +
                    "com.recocozephyr.rpc.service.impl.TellBadJokes"));
            handlerMap.put("com.recocozephyr.rpc.service.TellJokes", tellJokes);
        } catch (ClassNotFoundException e) {
            java.util.logging.Logger.getLogger(ServerExecutor.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
