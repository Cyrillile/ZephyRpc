package com.recocozephyr.rpc.model;

/**
 * @AUTHOR: Cyril (https://github.com/Cyrillile)
 * @DATE: 2020/10/20 16:28
 * @DESCRIPTIONS:
 */
public class ResponseInfo {
    private String serilizerbleId;
    private String error;
    private Object result;

    public String getSerilizerbleId() {
        return serilizerbleId;
    }

    public String getError() {
        return error;
    }

    public Object getResult() {
        return result;
    }

    public void setSerilizerbleId(String serilizerbleId) {
        this.serilizerbleId = serilizerbleId;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
