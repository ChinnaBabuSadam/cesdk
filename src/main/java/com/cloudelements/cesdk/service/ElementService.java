package com.cloudelements.cesdk.service;

import com.cloudelements.cesdk.util.HttpResponse;

import java.util.List;
import java.util.Map;

public interface ElementService extends Service {

    /**
     * This is to perform Search call like GET
     * @param elementQuery for ex: Select * from contacts
     * @param args can be page, pageSize, queryparams.
     * @return
     */
    List<Map> find(Object elementQuery, Object... args);

    List<Map> find(Object elementQuery);


    /**
     * This is to perform Search call like GET
     * @param objectName is a resource/entity name to fetch from the Service Provider.
     * @param query can be page, pageSize, and other supported queryparams.
     * @return
     */
    List<Map> find(String objectName, Map<String, Object> query);

    Map retrieve(String objectName, String id);

    Map create(String objectName, Map object);

    Map update(String objectName, String id, Map object);

    void delete(String objectName, String id);

    List<String> findObjects();

    Map doFileUpload(String objectName, Object object);

    HttpResponse retrieveFile (String objectName, String fileId);
}
