package com.ipe.module.exl.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ipe.common.entity.IDEntity;
import com.ipe.common.util.Constants;

/**
 * Excel导出模型模板设置
 * @author tangdu
 *
 */
@Entity
@Table(name = "t_exl_exptpl", schema = "", catalog =Constants.SCHEMA)
@JsonIgnoreProperties(value = { })
public class ExlExptpl extends IDEntity{
	private static final long serialVersionUID = -8448048677162474014L;

	private String code;
	private String name;
	private String sql;
	private String titles;
	private String remark;
	private Date createdDate;
	private Date updatedDate;
	private String enabled;
	
	@Column(name="CODE_")
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	@Column(name="ENABLED_")
	public String getEnabled() {
		return enabled;
	}
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}
	@Column(name="NAME_")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="SQL_")
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	
	@Column(name="TITLES_")
	public String getTitles() {
		return titles;
	}
	public void setTitles(String titles) {
		this.titles = titles;
	}
	
	@Column(name="REMARK_")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Column(name="CREATED_DATE",updatable=false)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	@Column(name="UPDATED_DATE")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
}
