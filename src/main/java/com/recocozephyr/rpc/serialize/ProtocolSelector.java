package com.recocozephyr.rpc.serialize;

import com.recocozephyr.rpc.common.SerializeProtocol;
import io.netty.channel.ChannelPipeline;

import java.io.IOException;

/**
 * @AUTHOR: Cyril (https://github.com/Cyrillile)
 * @DATE: 2020/11/12 14:09
 * @DESCRIPTIONS: 信息序列化协议选择器接口
 */
public interface ProtocolSelector {
    public void select(SerializeProtocol serializeProtocol, ChannelPipeline channelPipeline) throws IOException;
}
