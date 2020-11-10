package com.recocozephyr.rpc.netty;

import java.lang.reflect.Proxy;

/**
 * @AUTHOR: Cyril (https://github.com/Cyrillile)
 * @DATE: 2020/10/20 16:30
 * @DESCRIPTIONS:
 */
public class ClientExecutor {
    private RpcServerLoader rpcServerLoader = RpcServerLoader.getInstance();

    public ClientExecutor(String serverAddress) {
        rpcServerLoader.load(serverAddress);
    }

    public void stop() {
        rpcServerLoader.unload();
    }

    public static <T> T execute(Class<T> rpcServiceInterface) {
        T instance =  (T) Proxy.newProxyInstance(
                rpcServiceInterface.getClassLoader(),
                new Class<?>[]{rpcServiceInterface},
                new ProxyService<T>(rpcServiceInterface)
        );
        return instance;
    }
}
