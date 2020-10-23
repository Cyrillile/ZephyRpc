package com.recocozephyr.rpc.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.util.Map;

/**
 * @AUTHOR: Cyril (https://github.com/Cyrillile)
 * @DATE: 2020/10/20 16:30
 * @DESCRIPTIONS:1初始化channel；2添加channelHandler
 */
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    private Map<String, Object> handlerMap;
    public ServerChannelInitializer(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline channelPipeline = socketChannel.pipeline();
        //添加自适应长度解码器，长度设为整数最大值，长度域为4
        channelPipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,
                4, 0, 4, 0));
        //添加编码器,设置报头长度4
        channelPipeline.addLast(new LengthFieldPrepender(4));
        //用来序列化Java对象
        channelPipeline.addLast(new ObjectEncoder());
        //反序列化对象
        channelPipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE,
                ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
        //添加处理器
        channelPipeline.addLast(new ServerChannelHandler(handlerMap));
    }
}
