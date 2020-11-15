package com.recocozephyr.rpc.serialize.hessian;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * @AUTHOR: Cyril (https://github.com/Cyrillile)
 * @DATE: 2020/11/13 12:14
 * @DESCRIPTIONS:
 */
public class HessianSerializePool {
    private GenericObjectPool<HessianSerialize> hessianPool;
    private static HessianSerializePool hessianSerializePool;

    private HessianSerializePool() {
        this.hessianPool = new GenericObjectPool<HessianSerialize>(new HessianSerializeFactory());
    }

    public static HessianSerializePool getInstanceHessianSerializePool() {
        if (hessianSerializePool == null) {
            synchronized (HessianSerializePool.class) {
                if (hessianSerializePool == null) {
                    hessianSerializePool = new HessianSerializePool();
                }
            }
        }
        return hessianSerializePool;
    }

    public HessianSerializePool(final int maxTotal, final int minIdle, final long maxWaitMillis,
                                final long minEvictableIdleTimeMillis) {
        this.hessianPool = new GenericObjectPool<HessianSerialize>(new HessianSerializeFactory());
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        //
        config.setMaxTotal(maxTotal);
        //
        config.setMinIdle(minIdle);
        //最大等待时间
        config.setMaxWaitMillis(maxWaitMillis);
        //退出连接前的最小空闲时间
        config.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        this.hessianPool.setConfig(config);
    }
    //获取一个HessianSerialize对象
    public HessianSerialize borrow() {
        try {
            return hessianPool.borrowObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //还回pool中
    public void returnBack(final HessianSerialize hessianSerialize) {
        hessianPool.returnObject(hessianSerialize);
    }
}
