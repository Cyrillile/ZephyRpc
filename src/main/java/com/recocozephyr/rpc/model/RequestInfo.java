package com.recocozephyr.rpc.model;

/**
 * @AUTHOR: Cyril (https://github.com/Cyrillile)
 * @DATE: 2020/10/20 16:27
 * @DESCRIPTIONS:
 */
public class RequestInfo {
    String serilizerbleId;
    String className;
    String methodName;
    Class<?>[] parametersType;
    Object[] parametersVal;
}
