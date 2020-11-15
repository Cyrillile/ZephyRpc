package com.recocozephyr.rpc.netty;

import com.google.common.reflect.Reflection;
import com.recocozephyr.rpc.common.SerializeProtocol;

import java.lang.reflect.Proxy;

/**
 * @AUTHOR: Cyril (https://github.com/Cyrillile)
 * @DATE: 2020/10/20 16:30
 * @DESCRIPTIONS:
 */
public class ClientExecutor {
    private RpcServerLoader rpcServerLoader = RpcServerLoader.getInstance();

    public ClientExecutor() {
    }
    public void setClientExecutor(String serverAddress, SerializeProtocol serializeProtocol) {
        rpcServerLoader.load(serverAddress, serializeProtocol);
    }


    public void stop() {
        rpcServerLoader.unload();
    }

    public static <T> T execute(Class<T> rpcServiceInterface) {
        return (T) Reflection.newProxy(rpcServiceInterface, new ProxyService<T>());
    }
}
