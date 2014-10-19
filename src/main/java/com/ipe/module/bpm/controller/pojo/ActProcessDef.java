package com.ipe.module.bpm.controller.pojo;

import org.activiti.engine.repository.ProcessDefinition;

/**
 * Created with IntelliJ IDEA.
 * User: tangdu
 * Date: 13-12-19
 * Time: 下午9:09
 * To change this template use File | Settings | File Templates.
 */

public class ActProcessDef implements ProcessDefinition {
    private String id;
    private String rev;
    private String category;
    private String name;
    private String key;
    private int version;
    private String resourceName;
    private String diagramResourceName;
    private String description;
    private String deploymentId;
    private boolean startFormKey;
    private boolean suspended;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getDiagramResourceName() {
        return diagramResourceName;
    }

    public void setDiagramResourceName(String diagramResourceName) {
        this.diagramResourceName = diagramResourceName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public boolean hasStartFormKey() {
        return startFormKey;
    }

    public void setStartFormKey(boolean startFormKey) {
        this.startFormKey = startFormKey;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }
}
