package com.recocozephyr.rpc.serialize;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @AUTHOR: Cyril (https://github.com/Cyrillile)
 * @DATE: 2020/11/12 14:12
 * @DESCRIPTIONS: 编码器
 */
public class Encoder extends MessageToByteEncoder {
    private CodecUtil codecUtil;

    //TODO final
    public Encoder(final CodecUtil codecUtil) {
        this.codecUtil = codecUtil;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object msg, ByteBuf output) throws Exception {
        codecUtil.encode(output, msg);
    }
}
