package com.recocozephyr.rpc.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @AUTHOR: Cyril (https://github.com/Cyrillile)
 * @DATE: 2020/10/20 16:35
 * @DESCRIPTIONS:
 */
public class ClientServiceTask implements Runnable {
    private EventLoopGroup eventLoopGroup;
    private InetSocketAddress inetSocketAddress;
    private RpcServerLoader rpcServerLoader;

    ClientServiceTask(EventLoopGroup eventLoopGroup, InetSocketAddress inetSocketAddress,
                      RpcServerLoader rpcServerLoader) {
        this.eventLoopGroup = eventLoopGroup;
        this.inetSocketAddress = inetSocketAddress;
        this.rpcServerLoader = rpcServerLoader;
    }

    public void run() {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ClientChannelInitializer());
        ChannelFuture channelFuture = bootstrap.connect(inetSocketAddress);
        channelFuture.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    ClientChannelHandler clientChannelHandler = channelFuture.channel()
                            .pipeline().get(ClientChannelHandler.class);
                    ClientServiceTask.this.rpcServerLoader.setClientChannelHandler(
                            clientChannelHandler);
                }
            }
        });
    }
}
