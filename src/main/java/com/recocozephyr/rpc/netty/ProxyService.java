package com.recocozephyr.rpc.netty;

import com.recocozephyr.rpc.model.CallbackInfo;
import com.recocozephyr.rpc.model.RequestInfo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * @AUTHOR: Cyril (https://github.com/Cyrillile)
 * @DATE: 2020/10/20 16:49
 * @DESCRIPTIONS:
 */
public class ProxyService<T> implements InvocationHandler {
    private Class<T> tClass;

    public ProxyService(Class<T> tClass) {
        this.tClass = tClass;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setSerilizerbleId(UUID.randomUUID().toString());
        requestInfo.setClassName(method.getDeclaringClass().getName());
        requestInfo.setMethodName(method.getName());
        requestInfo.setParametersType(method.getParameterTypes());
        requestInfo.setParametersVal(args);

        ClientChannelHandler clientChannelHandler = RpcServerLoader.getInstance().getClientChannelHandler();
        CallbackInfo callbackInfo = clientChannelHandler.sendRequest(requestInfo);
        return callbackInfo.start();
    }
}
