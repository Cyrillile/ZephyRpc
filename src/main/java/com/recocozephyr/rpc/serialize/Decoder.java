package com.recocozephyr.rpc.serialize;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @AUTHOR: Cyril (https://github.com/Cyrillile)
 * @DATE: 2020/11/12 14:24
 * @DESCRIPTIONS:
 */
public class Decoder extends ByteToMessageDecoder {
    public final static int HeadLength = CodecUtil.HEAD_LENGTH;
    private CodecUtil codecUtil;

    public Decoder(final CodecUtil codecUtil) {
        this.codecUtil = codecUtil;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf input, List<Object> list) throws Exception {
        //消息头长度错误，直接返回
        if (input.readableBytes() < HeadLength) {
            return;
        }

        input.markReaderIndex();
        int messageLength = input.readInt();
        if (messageLength < 0) {
            channelHandlerContext.close();
        }

        //读到的消息长度和消息头已知的长度不匹配，充值Bytebuf读索引的位置
        if (input.readableBytes() < messageLength) {
            input.resetReaderIndex();
            return;
        } else {
            byte[] messageBody = new byte[messageLength];
            input.readBytes(messageBody);
            try {
                Object object = codecUtil.decode(messageBody);
                list.add(object);
            } catch (IOException e) {
                Logger.getLogger(Decoder.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
}
