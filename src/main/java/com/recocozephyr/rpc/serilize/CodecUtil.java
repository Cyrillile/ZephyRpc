package com.recocozephyr.rpc.serilize;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * @AUTHOR: Cyril (https://github.com/Cyrillile)
 * @DATE: 2020/11/12 14:03
 * @DESCRIPTIONS: 信息编码、解码接口
 */
public interface CodecUtil {
    public final static int HeadLenght = 4;
    public void encode(final ByteBuf out, final Object msg) throws IOException;
    public Object decode(byte[] body) throws IOException;
}
