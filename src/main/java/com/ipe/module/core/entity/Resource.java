package com.ipe.module.core.entity;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ipe.common.entity.IDEntity;
import com.ipe.common.util.Constants;

/**
 * Created with IntelliJ IDEA.
 * User: tangdu
 * Date: 13-9-7
 * Time: 下午9:10
 * To change this template use File | Settings | File Templates.
 */
@Table(name = "t_cor_resource", schema = "", catalog = Constants.SCHEMA)
@Entity
@JsonIgnoreProperties({"authorities", "parent"})
public class Resource extends IDEntity {
    /**
	 * 
	 */
	private static final long serialVersionUID = -5157433421510610754L;
	private String resourceName;

    @Column(name = "resource_name", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
    @Basic
    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    private String resourceType;

    @Column(name = "resource_type", nullable = true, insertable = true, updatable = true, length = 2, precision = 0)
    @Basic
    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    private String resourceUrl;

    @Column(name = "resource_url", nullable = true, insertable = true, updatable = true, length = 100, precision = 0)
    @Basic
    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    private Date createdDate;

    @Column(name = "created_date", nullable = true, insertable = true, updatable = true, length = 19, precision = 0)
    @Basic
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    private Date updatedDate;

    @Column(name = "updated_date", nullable = true, insertable = true, updatable = true, length = 19, precision = 0)
    @Basic
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    private Integer sno;

    @Column(name = "sno_", nullable = true, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public Integer getSno() {
        return sno;
    }

    public void setSno(Integer sno) {
        this.sno = sno;
    }

    private String remark;

    @Column(name = "remark_", nullable = true, insertable = true, updatable = true, length = 1000, precision = 0)
    @Basic
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    private String enabled;

    @Column(name = "enabled")
    @Basic
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

    ///tree 配置
    /*private String pid;
    @Column(name="pid",insertable = false,updatable = false)
    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }*/
    private Resource parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pid")
    public Resource getParent() {
        return parent;
    }

    public void setParent(Resource parent) {
        this.parent = parent;
    }

    private Set<Resource> rows;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "parent", fetch = FetchType.LAZY)
    @OrderBy(value = "sno")
    public Set<Resource> getRows() {
        return rows;
    }

    public void setRows(Set<Resource> rows) {
        this.rows = rows;
        if (rows == null || rows.isEmpty()) {
            this.leaf = true;
        }
    }

    private boolean leaf = false;

    @Transient
    public boolean isLeaf() {
        return leaf;
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
}
