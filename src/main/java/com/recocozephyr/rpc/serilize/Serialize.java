package com.recocozephyr.rpc.serilize;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @AUTHOR: Cyril (https://github.com/Cyrillile)
 * @DATE: 2020/11/12 13:59
 * @DESCRIPTIONS: 信息序列化、反序列化接口
 */
public interface Serialize {
    void serialize(OutputStream outputStream, Object object) throws IOException;
    Object deserialize(InputStream inputStream) throws IOException;

}
