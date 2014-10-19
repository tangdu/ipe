package com.ipe.module.core.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.ipe.common.entity.IDEntity;
import com.ipe.common.util.Constants;

/**
 * Created with IntelliJ IDEA.
 * User: tangdu
 * Date: 13-10-20
 * Time: 上午9:16
 * To change this template use File | Settings | File Templates.
 */
@Table(name = "t_cor_config", schema = "", catalog = Constants.SCHEMA)
@Entity
public class SysConfig extends IDEntity {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1241579886117243093L;
	private String key;
    private String value;
    private String remark;
    private String type;//数据类型
    private Integer sno;//序号

    @OrderBy(value = "sno")
    @Column(name = "sno")
    @Basic
    public Integer getSno() {
        return sno;
    }

    public void setSno(Integer sno) {
        this.sno = sno;
    }

    @Column(name = "type_", nullable = false)
    @Basic
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "key_", nullable = false)
    @Basic
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Column(name = "value_", nullable = false)
    @Basic
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Column(name = "remark", nullable = false)
    @Basic
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
