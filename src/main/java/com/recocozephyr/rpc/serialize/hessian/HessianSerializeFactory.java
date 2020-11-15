package com.recocozephyr.rpc.serialize.hessian;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * @AUTHOR: Cyril (https://github.com/Cyrillile)
 * @DATE: 2020/11/13 12:04
 * @DESCRIPTIONS:
 */
public class HessianSerializeFactory extends BasePooledObjectFactory<HessianSerialize> {
    public HessianSerialize create() throws Exception {
        return new HessianSerialize();
    }

    public PooledObject<HessianSerialize> wrap(HessianSerialize hessianSerialize) {
        return new DefaultPooledObject<HessianSerialize>(hessianSerialize);
    }
}
