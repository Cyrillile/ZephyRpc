package com.recocozephyr.rpc.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;


/**
 * @AUTHOR: Cyril (https://github.com/Cyrillile)
 * @DATE: 2020/10/20 16:33
 * @DESCRIPTIONS: 1初始化channel；2添加channelHandler
 */
public class ClientChannelInitializer extends ChannelInitializer<SocketChannel>{
    final public static int MESSAGE_LENGTH = 4;

    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline channelPipeline = socketChannel.pipeline();
        channelPipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,
                0, ClientChannelInitializer.MESSAGE_LENGTH, 0,
                ClientChannelInitializer.MESSAGE_LENGTH));
        channelPipeline.addLast(new LengthFieldPrepender(ClientChannelInitializer.MESSAGE_LENGTH));
        channelPipeline.addLast(new ObjectEncoder());
        channelPipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE,
                ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
        channelPipeline.addLast(new ClientChannelHandler());
    }
}
