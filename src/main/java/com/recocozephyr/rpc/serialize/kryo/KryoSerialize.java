package com.recocozephyr.rpc.serialize.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoPool;
import com.recocozephyr.rpc.serialize.Serialize;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @AUTHOR: Cyril (https://github.com/Cyrillile)
 * @DATE: 2020/11/12 14:38
 * @DESCRIPTIONS:
 */
public class KryoSerialize implements Serialize {
    private KryoPool kryoPool;

    //TODO final
    public KryoSerialize(final KryoPool kryoPool) {
        this.kryoPool = kryoPool;
    }

    public void serialize(OutputStream outputStream, Object object) throws IOException {
        Kryo kryo = kryoPool.borrow();
        Output output = new Output(outputStream);
        //object可能为null，不知道具体class
        kryo.writeClassAndObject(output, object);
        output.close();
        kryoPool.release(kryo);
    }

    public Object deserialize(InputStream inputStream) throws IOException {
        Kryo kryo = kryoPool.borrow();
        Input input = new Input(inputStream);
        //object可能为null，不知道具体class
        Object object = kryo.readClassAndObject(input);
        input.close();
        kryoPool.release(kryo);
        return object;
    }
}
