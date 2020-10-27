package com.recocozephyr.rpc.netty;

import com.recocozephyr.rpc.model.RequestInfo;
import com.recocozephyr.rpc.model.ResponseInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Map;

/**
 * @AUTHOR: Cyril (https://github.com/Cyrillile)
 * @DATE: 2020/10/20 16:31
 * @DESCRIPTIONS: 1从channel读取请求；2构造服务端任务交给server Executor执行
 */
public class ServerChannelHandler extends ChannelInboundHandlerAdapter{
    private final Map<String, Object> handlerMap;

    public ServerChannelHandler(Map<String, Object> handlerMap) {
        System.out.println("serverchannelHandler" );
        this.handlerMap = handlerMap;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(" in active");
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RequestInfo requestInfo = (RequestInfo)msg;
        System.out.println("server get request:" + requestInfo.getMethodName());
        ResponseInfo responseInfo = new ResponseInfo();
        ServerServiceTask serverServiceTask = new ServerServiceTask(requestInfo,responseInfo,ctx,
                handlerMap);
        ServerExecutor.submit(serverServiceTask);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
