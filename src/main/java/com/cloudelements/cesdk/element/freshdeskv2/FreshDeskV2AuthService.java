package com.cloudelements.cesdk.element.freshdeskv2;

import com.cloudelements.cesdk.service.exception.ServiceException;
import com.cloudelements.cesdk.util.ServiceConstants;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FreshDeskV2AuthService {

    public boolean provision(String authCredentials) {

        boolean provision = false;

        if (null == authCredentials) { return provision; }

        FreshdeskApiDeligate freshdeskApiDeligate = new FreshdeskApiDeligate();
        Map<String, String> headers = new HashMap<>();
        headers.put(ServiceConstants.AUTHORIZATION, authCredentials);
        freshdeskApiDeligate.setHeaders(headers);
        try {
            Object contacts = freshdeskApiDeligate.find("contacts", new HashMap<>());
            if (contacts instanceof List) {
                return provision;
            }
        } catch (Exception e) {
            throw new ServiceException(HttpStatus.UNAUTHORIZED, "Failed to authenticate with the given credentials");
        }

        return provision;
    }
}
