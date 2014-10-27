package com.ipe.module.core.entity;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ipe.common.entity.IDEntity;
import com.ipe.common.util.Constants;

/**
 * Created with IntelliJ IDEA. User: tangdu Date: 13-9-7 Time: 下午9:10 To change
 * this template use File | Settings | File Templates.
 */
@Table(name = "t_cor_resource", schema = "", catalog = Constants.SCHEMA)
@Entity
@JsonIgnoreProperties({ "authorities", "parent"})
public class Resource extends IDEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5157433421510610754L;
	private String resourceName;

	@Column(name = "resource_name")
	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	private String resourceType;

	@Column(name = "resource_type")
	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	private String resourceUrl;

	@Column(name = "resource_url")
	public String getResourceUrl() {
		return resourceUrl;
	}

	public void setResourceUrl(String resourceUrl) {
		this.resourceUrl = resourceUrl;
	}

	private Date createdDate;

	@Column(name = "created_date", updatable = false)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	private Date updatedDate;

	@Column(name = "updated_date")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	private Integer sno;

	@Column(name = "sno_")
	public Integer getSno() {
		return sno;
	}

	public void setSno(Integer sno) {
		this.sno = sno;
	}

	private String remark;

	@Column(name = "remark_")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	private String enabled;

	@Column(name = "enabled")
	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	private Collection<Authority> authorities;

	@OneToMany(mappedBy = "resource")
	public Collection<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Collection<Authority> authorities) {
		this.authorities = authorities;
	}

	private Resource parent;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pid")
	public Resource getParent() {
		return parent;
	}

	public void setParent(Resource parent) {
		this.parent = parent;
	}

	private boolean leaf = false;

	@Transient
	public boolean isLeaf() {
		if(this.rows==null || this.rows.isEmpty()){
			this.leaf=true;
		}
		return this.leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	private boolean expanded = true;

	@Transient
	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	private Boolean checked = null;

	@Transient
	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	private List<Resource> rows;

	@Transient
	public List<Resource> getRows() {
		if(this.rows==null || this.rows.isEmpty()){
			this.leaf=true;
		}else{
			this.leaf=false;
		}
		return rows;
	}

	public void setRows(List<Resource> rows) {
		this.rows = rows;
	}
}
