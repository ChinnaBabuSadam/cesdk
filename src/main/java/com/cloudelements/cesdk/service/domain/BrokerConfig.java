package com.cloudelements.cesdk.service.domain;

import java.io.Serializable;

public class BrokerConfig implements Serializable {

    private static final long serialVersionUID = 6328020306706954456L;

    String value;

    String key;

    boolean refreshable;

    public BrokerConfig() {}

    public BrokerConfig(String value, String key, boolean refreshable) {
        this.value = value;
        this.key = key;
        this.refreshable = refreshable;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isRefreshable() {
        return refreshable;
    }

    public void setRefreshable(boolean refreshable) {
        this.refreshable = refreshable;
    }
}
