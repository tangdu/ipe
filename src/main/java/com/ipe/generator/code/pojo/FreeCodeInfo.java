package com.ipe.generator.code.pojo;

import java.util.List;

import com.ipe.module.exl.TableColumn;

public class FreeCodeInfo {
	private String entityName;//实体名字
	private String schema;
	private String tableName;
	private List<TableColumn> columns;
	
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public List<TableColumn> getColumns() {
		return columns;
	}
	public void setColumns(List<TableColumn> columns) {
		this.columns = columns;
	}

	private String path;//生成文件路径
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	private String packageName;//包名
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	
	private boolean cEntity=false;
	private boolean cDao=false;
	private boolean cService=false;
	private boolean cController=false;

	public boolean iscEntity() {
		return cEntity;
	}
	public void setcEntity(boolean cEntity) {
		this.cEntity = cEntity;
	}
	public boolean iscDao() {
		return cDao;
	}
	public void setcDao(boolean cDao) {
		this.cDao = cDao;
	}
	public boolean iscService() {
		return cService;
	}
	public void setcService(boolean cService) {
		this.cService = cService;
	}
	public boolean iscController() {
		return cController;
	}
	public void setcController(boolean cController) {
		this.cController = cController;
	}
}
