package com.recocozephyr.rpc.serilize.kryo;

import com.recocozephyr.rpc.serilize.CodecUtil;
import com.recocozephyr.rpc.serilize.Encoder;

/**
 * @AUTHOR: Cyril (https://github.com/Cyrillile)
 * @DATE: 2020/11/12 15:26
 * @DESCRIPTIONS:
 */
public class KryoEncoder extends Encoder {
    public KryoEncoder(CodecUtil codecUtil) {
        super(codecUtil);
    }
}
