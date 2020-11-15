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
import java.util.Map;

/**
 * @AUTHOR: Cyril (https://github.com/Cyrillile)
 * @DATE: 2020/11/13 14:29
 * @DESCRIPTIONS:
 */
public class ServerProtocolSelector implements ProtocolSelector {
    private Map<String, Object> handlerMap;

    public ServerProtocolSelector(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    public void select(SerializeProtocol serializeProtocol, ChannelPipeline channelPipeline) throws IOException {
        switch (serializeProtocol) {
            case JDKSERIALIZE:{
                channelPipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,
                        CodecUtil.HEAD_LENGTH, 0, CodecUtil.HEAD_LENGTH));
                channelPipeline.addLast(new LengthFieldPrepender(CodecUtil.HEAD_LENGTH));
                channelPipeline.addLast(new ObjectEncoder());
                channelPipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE,
                        ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
                channelPipeline.addLast(new ServerChannelHandler(handlerMap));
                break;
            }
            case KRYOSERIALIZE:{
                KryoCodecUtil kryoCodecUtil = new KryoCodecUtil(KryoPoolFactory.getInstanceKryoPoolFactory());
                channelPipeline.addLast(new KryoEncoder(kryoCodecUtil));
                channelPipeline.addLast(new KryoDecoder(kryoCodecUtil));
                channelPipeline.addLast(new ServerChannelHandler(handlerMap));
                break;
            }
            case HESSIANSERIALIZE:{
                HessianCodecUtil hessianCodecUtil = new HessianCodecUtil();
                channelPipeline.addLast(new HessianEncoder(hessianCodecUtil));
                channelPipeline.addLast(new HessianDecoder(hessianCodecUtil));
                channelPipeline.addLast(new ServerChannelHandler(handlerMap));
                break;
            }
        }
    }
}
