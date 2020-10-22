package com.recocozephyr.rpc.netty;

import com.recocozephyr.rpc.model.ResponseInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @AUTHOR: Cyril (https://github.com/Cyrillile)
 * @DATE: 2020/10/20 16:34
 * @DESCRIPTIONS: 1向channel中写入请求；2读取channel中的回复
 */
public class ClientChannelHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ResponseInfo responseInfo = (ResponseInfo)msg;
        String serilizerbleId = responseInfo.getSerilizerbleId();
    }
}
