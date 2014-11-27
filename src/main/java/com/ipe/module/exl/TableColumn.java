package com.ipe.module.exl;

import java.io.Serializable;

public class TableColumn implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8848005918792296553L;
	private Integer index;
	private String fieldName;
	private String fieldType;
	private String fieldDesc;

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getFieldDesc() {
		return fieldDesc;
	}

	public void setFieldDesc(String fieldDesc) {
		this.fieldDesc = fieldDesc;
	}
}
