package com.ipe.generator.ext.pojo;

public class ExtParams {
	String type;
	String dt;
	String entityName;
	String mpackage;
	Boolean createFile;
	String path;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDt() {
		return dt;
	}
	public void setDt(String dt) {
		this.dt = dt;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public String getMpackage() {
		return mpackage;
	}
	public void setMpackage(String mpackage) {
		this.mpackage = mpackage;
	}
	public Boolean getCreateFile() {
		return createFile;
	}
	public void setCreateFile(Boolean createFile) {
		this.createFile = createFile;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
}
