package com.recocozephyr.rpc.netty;

import com.recocozephyr.rpc.common.SerializeProtocol;
import com.recocozephyr.rpc.serialize.CodecUtil;
import com.recocozephyr.rpc.serialize.ProtocolSelector;
import com.recocozephyr.rpc.serialize.hessian.HessianCodecUtil;
import com.recocozephyr.rpc.serialize.hessian.HessianDecoder;
import com.recocozephyr.rpc.serialize.hessian.HessianEncoder;
import com.recocozephyr.rpc.serialize.kryo.KryoCodecUtil;
import com.recocozephyr.rpc.serialize.kryo.KryoDecoder;
import com.recocozephyr.rpc.serialize.kryo.KryoEncoder;
import com.recocozephyr.rpc.serialize.kryo.KryoPoolFactory;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.io.IOException;

/**
 * @AUTHOR: Cyril (https://github.com/Cyrillile)
 * @DATE: 2020/11/13 11:35
 * @DESCRIPTIONS:
 */
public class ClientProtocolSelctor implements ProtocolSelector {
       public void select(SerializeProtocol serializeProtocol, ChannelPipeline channelPipeline) throws IOException {
        switch (serializeProtocol) {
            case JDKSERIALIZE:{
                channelPipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,
                        CodecUtil.HEAD_LENGTH, 0, CodecUtil.HEAD_LENGTH));
                channelPipeline.addLast(new LengthFieldPrepender(CodecUtil.HEAD_LENGTH));
                channelPipeline.addLast(new ObjectEncoder());
                channelPipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE,
                        ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
                channelPipeline.addLast(new ClientChannelHandler());
                break;
            }
            case KRYOSERIALIZE:{
                KryoCodecUtil kryoCodecUtil = new KryoCodecUtil(KryoPoolFactory.getInstanceKryoPoolFactory());
                channelPipeline.addLast(new KryoEncoder(kryoCodecUtil));
                channelPipeline.addLast(new KryoDecoder(kryoCodecUtil));
                channelPipeline.addLast(new ClientChannelHandler());
                break;
            }
            case HESSIANSERIALIZE:{
                HessianCodecUtil hessianCodecUtil = new HessianCodecUtil();
                channelPipeline.addLast(new HessianEncoder(hessianCodecUtil));
                channelPipeline.addLast(new HessianDecoder(hessianCodecUtil));
                channelPipeline.addLast(new ClientChannelHandler());
                break;
            }
        }
    }
}
