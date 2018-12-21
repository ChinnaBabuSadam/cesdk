package com.cloudelements.cesdk.service;

import java.io.Serializable;
import java.util.Map;

public interface Service extends Serializable {


    public static final long serialVersionUID = 3608379990562954002L;


    void init();

    Map doBulkDownLoad();

    Map doBulkUpload();

    boolean provision();

    boolean deleteProvision();

}
