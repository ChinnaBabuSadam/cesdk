package com.cloudelements.cesdk.framework;

import com.cloudelements.cesdk.service.ElementService;
import com.cloudelements.cesdk.service.exception.ServiceException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

public abstract class AbstractElementService implements ElementService {

    private final static String API_NOT_SUPPORTED = "This API is not currently supported for this element";

    @Override
    public List<Map> find(Object elementQuery, Object... args) {
        throw new ServiceException(HttpStatus.METHOD_NOT_ALLOWED, API_NOT_SUPPORTED);
    }

    @Override
    public List<Map> find(Object elementQuery) {
        throw new ServiceException(HttpStatus.METHOD_NOT_ALLOWED, API_NOT_SUPPORTED);
    }

    @Override
    public Map retrieve(String objectName, String id) {
        throw new ServiceException(HttpStatus.METHOD_NOT_ALLOWED, API_NOT_SUPPORTED);
    }

    @Override
    public Map create(String objectName, Map object) {
        throw new ServiceException(HttpStatus.METHOD_NOT_ALLOWED, API_NOT_SUPPORTED);
    }

    @Override
    public Map update(String objectName, String id, Map object) {
        throw new ServiceException(HttpStatus.METHOD_NOT_ALLOWED, API_NOT_SUPPORTED);
    }

    @Override
    public void delete(String objectName, String id) {
        throw new ServiceException(HttpStatus.METHOD_NOT_ALLOWED, API_NOT_SUPPORTED);
    }

    @Override
    public List<String> findObjects() {
        throw new ServiceException(HttpStatus.METHOD_NOT_ALLOWED, API_NOT_SUPPORTED);
    }
}
