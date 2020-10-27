package com.recocozephyr.rpc.common;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @AUTHOR: Cyril (https://github.com/Cyrillile)
 * @DATE: 2020/10/27 18:30
 * @DESCRIPTIONS:
 */
public enum SerializeProtocol {
    JDKSERIALIZE("jdknative");
    private String serializeProtocol;

    SerializeProtocol(String serializeProtocol) {
        this.serializeProtocol = serializeProtocol;
    }

    @Override
    public String toString() {
        ReflectionToStringBuilder.setDefaultStyle(ToStringStyle.SHORT_PREFIX_STYLE);
        return ReflectionToStringBuilder.toString(this);
    }

    public String getSerializeProtocol() {
        return serializeProtocol;
    }
}
