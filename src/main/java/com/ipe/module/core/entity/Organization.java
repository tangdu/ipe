package com.ipe.module.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

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
    private String orgLevel;
    private String remark;
    private Organization parent;

    @Column(name = "org_code")
    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }
    
    @Column(name = "org_level")
    public String getOrgLevel() {
		return orgLevel;
	}

	public void setOrgLevel(String orgLevel) {
		this.orgLevel = orgLevel;
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
	public Boolean isChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
}
