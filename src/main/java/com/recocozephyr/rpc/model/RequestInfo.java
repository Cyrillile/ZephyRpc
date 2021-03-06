package com.recocozephyr.rpc.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * @AUTHOR: Cyril (https://github.com/Cyrillile)
 * @DATE: 2020/10/20 16:27
 * @DESCRIPTIONS:
 */
public class RequestInfo implements Serializable{
    private String serilizerbleId;
    private String className;
    private String methodName;
    private Class<?>[] parametersType;
    private Object[] parametersVal;

    public String getSerilizerbleId() {
        return serilizerbleId;
    }

    public void setSerilizerbleId(String serilizerbleId) {
        this.serilizerbleId = serilizerbleId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParametersType() {
        return parametersType;
    }

    public void setParametersType(Class<?>[] parametersType) {
        this.parametersType = parametersType;
    }

    public Object[] getParametersVal() {
        return parametersVal;
    }

    public void setParametersVal(Object[] parametersVal) {
        this.parametersVal = parametersVal;
    }
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("serilizerbleId", serilizerbleId).append("className", className)
                .append("methodName", methodName).toString();
    }
}
