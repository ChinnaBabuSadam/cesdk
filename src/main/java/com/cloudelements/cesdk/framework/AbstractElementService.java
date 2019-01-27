package com.cloudelements.cesdk.framework;

import com.cloudelements.cesdk.service.ElementService;
import com.cloudelements.cesdk.service.domain.BrokerExpression;
import com.cloudelements.cesdk.service.exception.ServiceException;
import com.mashape.unirest.http.HttpResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public abstract class AbstractElementService implements ElementService {

    private final static String API_NOT_SUPPORTED = "This API is not currently supported for this element";


    @Override
    public List<Map> find(Object elementQuery, List<BrokerExpression> brokerExpressions) {
        return null;
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

    public abstract List<Map> find(String objectName, List<BrokerExpression> brokerExpressions);

    @Override
    public List<String> findObjects() {
        throw new ServiceException(HttpStatus.METHOD_NOT_ALLOWED, API_NOT_SUPPORTED);
    }

    @Override
    public Map doBulkDownLoad() {
        throw new ServiceException(HttpStatus.NOT_IMPLEMENTED, "The API not yet implemented");
    }

    @Override
    public Map doBulkUpload() {
        throw new ServiceException(HttpStatus.NOT_IMPLEMENTED, "The API not yet implemented");
    }

    @Override
    public boolean provision() {
        throw new ServiceException(HttpStatus.NOT_IMPLEMENTED, "The API not yet implemented");
    }

    @Override
    public boolean deleteProvision() {
        throw new ServiceException(HttpStatus.NOT_IMPLEMENTED, "The API not yet implemented");
    }

    @Override
    public Map doFileUpload(String objectName, Object object) {
        throw new ServiceException(HttpStatus.METHOD_NOT_ALLOWED, API_NOT_SUPPORTED);
    }

    @Override
    public com.cloudelements.cesdk.util.HttpResponse retrieveFile(String objectName, String fileId) {
        throw new ServiceException(HttpStatus.METHOD_NOT_ALLOWED, API_NOT_SUPPORTED);
    }

    protected void handleErrorResponse(HttpResponse httpResponse) {
        if (httpResponse == null) {
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }

        if (isErrorResponse(httpResponse)) {
            String errorMessage = fetchErrorResponseFromStream(httpResponse);
            if (StringUtils.isBlank(errorMessage)) {
                errorMessage = httpResponse.getStatusText();
            }
            throw new ServiceException(HttpStatus.valueOf(httpResponse.getStatus()), errorMessage);
        }
    }

    protected boolean isErrorResponse(HttpResponse httpResponse) {
        int statusCode = httpResponse.getStatus();
        return !(statusCode >= 200 && statusCode <= 207);
    }

    protected static <T> String fetchErrorResponseFromStream(HttpResponse<T> httpResponse) {
        try (BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(new
                BufferedInputStream(httpResponse.getRawBody())))) {
            String line = "";
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = responseStreamReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }

            return stringBuilder.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
