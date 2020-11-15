package com.recocozephyr.rpc.serialize.kryo;

import com.esotericsoftware.kryo.pool.KryoPool;
import com.google.common.io.Closer;
import com.recocozephyr.rpc.serialize.CodecUtil;
import io.netty.buffer.ByteBuf;

import java.io.*;

/**
 * @AUTHOR: Cyril (https://github.com/Cyrillile)
 * @DATE: 2020/11/12 15:11
 * @DESCRIPTIONS:
 */
public class KryoCodecUtil implements CodecUtil {
    private KryoPool kryoPool;
    private static Closer closer = Closer.create();

    public KryoCodecUtil(KryoPool kryoPool) {
        this.kryoPool = kryoPool;
    }

    //TODO final
    public void encode(final ByteBuf out, final Object object) throws IOException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            closer.register(byteArrayOutputStream);
            KryoSerialize kryoSerialize = new KryoSerialize(kryoPool);
            kryoSerialize.serialize(byteArrayOutputStream, object);
            byte[] body = byteArrayOutputStream.toByteArray();
            int dataLength = body.length;
            out.writeInt(dataLength);
            out.writeBytes(body);
        }finally {
            closer.close();
        }
    }

    public Object decode(byte[] body) throws IOException {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
            closer.register(byteArrayInputStream);
            KryoSerialize kryoSerialize = new KryoSerialize(kryoPool);
            Object object = kryoSerialize.deserialize(byteArrayInputStream);
            return object;
        }finally {
            closer.close();
        }
    }
}
