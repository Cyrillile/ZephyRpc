package com.recocozephyr.rpc.serialize.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.pool.KryoFactory;
import com.esotericsoftware.kryo.pool.KryoPool;
import com.recocozephyr.rpc.model.RequestInfo;
import com.recocozephyr.rpc.model.ResponseInfo;
import org.objenesis.strategy.StdInstantiatorStrategy;

/**
 * @AUTHOR: Cyril (https://github.com/Cyrillile)
 * @DATE: 2020/11/12 14:54
 * @DESCRIPTIONS: kryoPool 工厂
 */
public class KryoPoolFactory {
    private static KryoPoolFactory kryoPoolFactory;
    //匿名内部类实现KryoFactory接口
    private KryoFactory kryoFactory = new KryoFactory() {
        public Kryo create() {
            Kryo kryo = new Kryo();
            //不会存在循环引用，可以关闭提高性能
            kryo.setReferences(false);
            //将class注册以提高序列化效率
            kryo.register(RequestInfo.class);
            kryo.register(ResponseInfo.class);
            //设置实例化的策略，std使用JVM的APIs实现，不会调用构造器
            kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
            return kryo;
        }
    };

    private KryoPool kryoPool = new KryoPool.Builder(kryoFactory).build();

    //private保证单例
    private KryoPoolFactory() {
    }

    public static KryoPool getInstanceKryoPoolFactory() {
        if (kryoPoolFactory == null) {
            synchronized (KryoPoolFactory.class) {
                if (kryoPoolFactory == null) {
                    kryoPoolFactory = new KryoPoolFactory();
                }
            }
        }
        return kryoPoolFactory.kryoPool;
    }

}
