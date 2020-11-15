package com.recocozephyr.rpc.serialize.kryo;

import com.recocozephyr.rpc.serialize.CodecUtil;
import com.recocozephyr.rpc.serialize.Decoder;

/**
 * @AUTHOR: Cyril (https://github.com/Cyrillile)
 * @DATE: 2020/11/12 22:13
 * @DESCRIPTIONS:
 */
public class KryoDecoder extends Decoder {
    public KryoDecoder(CodecUtil codecUtil) {
        super(codecUtil);
    }
}
