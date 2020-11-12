package com.recocozephyr.rpc.serilize.kryo;

import com.recocozephyr.rpc.serilize.CodecUtil;
import com.recocozephyr.rpc.serilize.Decoder;

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
