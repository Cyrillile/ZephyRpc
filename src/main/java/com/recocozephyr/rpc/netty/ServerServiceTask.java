package com.recocozephyr.rpc.netty;

import com.recocozephyr.rpc.model.RequestInfo;
import com.recocozephyr.rpc.model.ResponseInfo;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang3.reflect.MethodUtils;
import sun.reflect.misc.MethodUtil;

import java.util.Map;

/**
 * @AUTHOR: Cyril (https://github.com/Cyrillile)
 * @DATE: 2020/10/20 16:32
 * @DESCRIPTIONS: 利用反射，将请求的服务执行，向channel中写回带有结果的response
 */
public class ServerServiceTask implements Runnable{
    private ResponseInfo responseInfo;
    private RequestInfo requestInfo;
    private ChannelHandlerContext ctx;
    private Map<String, Object> handlerMap;
    ServerServiceTask(RequestInfo requestInfo, ResponseInfo responseInfo, ChannelHandlerContext ctx,
                      Map<String, Object> handlerMap){
        this.requestInfo = requestInfo;
        this.responseInfo = responseInfo;
        this.ctx = ctx;
        this.handlerMap = handlerMap;
    }
    public void run() {
        responseInfo.setSerilizerbleId(requestInfo.getSerilizerbleId());
        try {
            Object result = reflect(requestInfo);
            responseInfo.setResult(result);
        } catch (Throwable t) {
            responseInfo.setError(t.toString());
            t.printStackTrace();
            System.err.println("ZephyRpc server reflect error!");
        }
        ctx.writeAndFlush(responseInfo).addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                System.out.println("ZephyRpc server responses serilizerbleId: " + responseInfo.getSerilizerbleId());
            }
        });

    }
    private Object reflect(RequestInfo requestInfo) throws Throwable {
        String className = requestInfo.getClassName();
        Object serviceBean = handlerMap.get(className);
        String methodName = requestInfo.getMethodName();
        Object[] parametersVal = requestInfo.getParametersVal();
        return MethodUtils.invokeMethod(serviceBean, methodName, parametersVal);
    }
}
