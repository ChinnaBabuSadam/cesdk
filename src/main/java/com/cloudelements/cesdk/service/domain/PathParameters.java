package com.cloudelements.cesdk.service.domain;

import java.io.Serializable;

public class PathParameters implements Serializable {

    private static final long serialVersionUID = -6329172248532243573L;

    String id;

    String parentId;

    String childId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }
}
