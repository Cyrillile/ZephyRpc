package com.recocozephyr.rpc.netty;

import com.recocozephyr.rpc.model.CallbackInfo;
import com.recocozephyr.rpc.model.RequestInfo;
import com.recocozephyr.rpc.model.ResponseInfo;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;

import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @AUTHOR: Cyril (https://github.com/Cyrillile)
 * @DATE: 2020/10/20 16:34
 * @DESCRIPTIONS: 1向channel中写入请求；2读取channel中的回复
 */
public class ClientChannelHandler extends ChannelInboundHandlerAdapter {
    private volatile Channel channel;
    private SocketAddress socketAddress;
    private ConcurrentHashMap<String, CallbackInfo> callback = new ConcurrentHashMap<String, CallbackInfo>();

    public SocketAddress getSocketAddress() {
        return socketAddress;
    }

    public Channel getChannel() {
        return channel;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ResponseInfo responseInfo = (ResponseInfo) msg;
        String serilizerbleId = responseInfo.getSerilizerbleId();
        CallbackInfo callbackInfo = callback.get(serilizerbleId);
        if (callbackInfo != null) {
            callback.remove(serilizerbleId);
            callbackInfo.over(responseInfo);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        this.channel = ctx.channel();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.socketAddress = this.channel.remoteAddress();
    }

    public void close() {
        channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    public CallbackInfo sendRequest(RequestInfo requestInfo) {
        CallbackInfo callbackInfo = new CallbackInfo(requestInfo);
        callback.put(requestInfo.getSerilizerbleId(), callbackInfo);
        channel.writeAndFlush(requestInfo);
        System.out.println("sendrequest : " + requestInfo);
        return callbackInfo;
    }
}
