package com.cloudelements.cesdk.service.domain;

import org.apache.commons.lang3.StringUtils;

public enum RequestMethod {

    CREATE("create"),
    RETRIEVE("retrieve"),
    UPDATE("update"),
    SEARCH("search"),
    DELETE("delete");

    String method;

    RequestMethod(String method) {
        this.method = method;
    }

    public static RequestMethod fromString(String val) {
        if (StringUtils.isBlank(val)) { return null; }
        for (RequestMethod requestMethod : RequestMethod.values()) {
            if (StringUtils.equalsIgnoreCase(requestMethod.toString(), val)) {
                return requestMethod;
            }
        }
        return null;
    }
}
