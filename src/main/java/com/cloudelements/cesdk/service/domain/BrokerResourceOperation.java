package com.cloudelements.cesdk.service.domain;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * It could be one of the following.  Will be helpful to differentiate between Http Methods
 *
 * CREATE, RETRIEVE, UPDATE, UPDATEALL, DELETE, SEARCH.   We call them CRUDS
 */
public enum BrokerResourceOperation implements Serializable {

    INIT("init"),
    CREATE("create"),
    RETRIEVE("retrieve"),
    UPDATE("update"),
    SEARCH("search"),
    DELETE("delete"),
    UPDATEALL("updateall"),
    REFRESH("refresh");

    String method;

    BrokerResourceOperation(String method) {
        this.method = method;
    }

    public static BrokerResourceOperation fromString(String val) {
        if (StringUtils.isBlank(val)) { return null; }
        for (BrokerResourceOperation requestMethod : BrokerResourceOperation.values()) {
            if (StringUtils.equalsIgnoreCase(requestMethod.toString(), val)) {
                return requestMethod;
            }
        }
        return null;
    }
}
