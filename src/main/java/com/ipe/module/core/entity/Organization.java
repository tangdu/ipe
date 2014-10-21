package com.ipe.module.core.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.ipe.common.entity.IDEntity;
import com.ipe.common.util.Constants;

/**
 * Created with IntelliJ IDEA.
 * User: tangdu
 * Date: 13-12-14
 * Time: 上午11:59
 * To change this template use File | Settings | File Templates.
 */
@Table(name = "t_cor_organization", schema = "", catalog = Constants.SCHEMA)
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Organization extends IDEntity {

    /**
	 * 
	 */
	private static final long serialVersionUID = -4572277317171222085L;
	private String orgCode;
    private String orgName;
    private String remark;
    private Organization parent;
    private Set<Organization> rows;

    @Column(name = "org_code")
    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    @Column(name = "org_name")
    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    @Column(name = "remark_")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    private Integer sno;

    @Column(name = "sno_")
    public Integer getSno() {
        return sno;
    }

    public void setSno(Integer sno) {
        this.sno = sno;
    }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pid")
    public Organization getParent() {
        return parent;
    }

    public void setParent(Organization parent) {
        this.parent = parent;
    }

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "parent", fetch = FetchType.LAZY)
    @OrderBy("sno asc")
    public Set<Organization> getRows() {
        return rows;
    }

    public void setRows(Set<Organization> rows) {
        this.rows = rows;
    }
}
