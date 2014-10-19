package com.ipe.module.ext.pojo;

import java.util.List;

/**
 * Created by tangdu on 14-2-20.
 */
public class ExtTable {

    private String entityName;
    private String packageName;
    private List<ExtColumn> columns;

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<ExtColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<ExtColumn> columns) {
        this.columns = columns;
    }
}
