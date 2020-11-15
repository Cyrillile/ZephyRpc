package com.recocozephyr.rpc.netty;

import com.recocozephyr.rpc.common.SerializeProtocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.Callable;

/**
 * @AUTHOR: Cyril (https://github.com/Cyrillile)
 * @DATE: 2020/10/20 16:35
 * @DESCRIPTIONS:
 */
public class ClientServiceTask implements Callable<Boolean> {
    private EventLoopGroup eventLoopGroup;
    private InetSocketAddress inetSocketAddress;
    private SerializeProtocol serializeProtocol;

    ClientServiceTask(EventLoopGroup eventLoopGroup, InetSocketAddress inetSocketAddress,
                      SerializeProtocol serializeProtocol) {
        this.eventLoopGroup = eventLoopGroup;
        this.inetSocketAddress = inetSocketAddress;
        this.serializeProtocol = serializeProtocol;
    }

    public Boolean call() throws Exception {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.handler(new ClientChannelInitializer().buildSerializeProtocol(serializeProtocol));
        ChannelFuture channelFuture = bootstrap.connect(inetSocketAddress);
        channelFuture.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                ClientChannelHandler clientChannelHandler = channelFuture.channel().pipeline()
                        .get(ClientChannelHandler.class);
                RpcServerLoader.getInstance().setClientChannelHandler(clientChannelHandler);
            }
        });
        return Boolean.TRUE;
    }
}
