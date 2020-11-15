package com.recocozephyr.rpc.serialize.hessian;

import com.google.common.io.Closer;
import com.recocozephyr.rpc.serialize.CodecUtil;
import io.netty.buffer.ByteBuf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @AUTHOR: Cyril (https://github.com/Cyrillile)
 * @DATE: 2020/11/13 11:53
 * @DESCRIPTIONS:
 */
public class HessianCodecUtil implements CodecUtil {
    private HessianSerializePool hessianSerializePool = HessianSerializePool.getInstanceHessianSerializePool();
    private static Closer closer = Closer.create();
    public HessianCodecUtil() {
    }

    public void encode(final ByteBuf out, final Object msg) throws IOException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            closer.register(byteArrayOutputStream);
            HessianSerialize hessianSerialize = hessianSerializePool.borrow();
            hessianSerialize.serialize(byteArrayOutputStream, msg);
            byte[] body = byteArrayOutputStream.toByteArray();
            int dataLength = body.length;
            out.writeInt(dataLength);
            out.writeBytes(body);
            hessianSerializePool.returnBack(hessianSerialize);
        }finally {
            closer.close();
        }
    }

    public Object decode(byte[] body) throws IOException {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
            closer.register(byteArrayInputStream);
            HessianSerialize hessianSerialize = hessianSerializePool.borrow();
            Object object = hessianSerialize.deserialize(byteArrayInputStream);
            hessianSerializePool.returnBack(hessianSerialize);
            return object;
        }finally {
            closer.close();
        }
    }
}
