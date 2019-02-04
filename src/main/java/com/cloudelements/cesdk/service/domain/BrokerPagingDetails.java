package com.cloudelements.cesdk.service.domain;

import java.io.Serializable;

public class BrokerPagingDetails implements Serializable {

    private static final long serialVersionUID = -4732750508199398702L;

    int page;

    int pageSize;

    int nextPageToken;

    public BrokerPagingDetails() {}

    public BrokerPagingDetails(int page, int pageSize, int nextPageToken) {
        this.page = page;
        this.pageSize = pageSize;
        this.nextPageToken = nextPageToken;
    }

    public BrokerPagingDetails(int page, int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(int nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

}
