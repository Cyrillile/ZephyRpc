package com.recocozephyr.rpc.serialize.hessian;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.recocozephyr.rpc.serialize.Serialize;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @AUTHOR: Cyril (https://github.com/Cyrillile)
 * @DATE: 2020/11/13 11:55
 * @DESCRIPTIONS:
 */
public class HessianSerialize implements Serialize {
    public void serialize(OutputStream outputStream, Object object){
        Hessian2Output hessian2Output = new Hessian2Output(outputStream);
        try {
            hessian2Output.startMessage();
            hessian2Output.writeObject(object);
            hessian2Output.completeMessage();
            hessian2Output.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object deserialize(InputStream inputStream) throws IOException {
        Object object = null;
        try {
            Hessian2Input hessian2Input = new Hessian2Input(inputStream);
            hessian2Input.startMessage();
            object = hessian2Input.readObject();
            hessian2Input.completeMessage();
            hessian2Input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return object;
    }
}
