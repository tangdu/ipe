package com.ipe.module.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.ipe.common.entity.IDEntity;
import com.ipe.common.util.Constants;

/**
 * Created by tangdu on 14-1-25.
 */
@JsonAutoDetect
@Entity
@Table(name = "t_sys_dictval", schema = "",catalog=Constants.SCHEMA)
public class DictVal extends IDEntity {
    private static final long serialVersionUID = 1L;
    private String dictValName;
    private String dictValCode;
    private String remark;
    private Integer sno;
    private String dictId;

    @Column(name="dict_id")
    public String getDictId() {
        return dictId;
    }

    public void setDictId(String dictId) {
        this.dictId = dictId;
    }

    @Column(name="dictval_name")
    public String getDictValName() {
        return dictValName;
    }

    public void setDictValName(String dictValName) {
        this.dictValName = dictValName;
    }

    @Column(name="dictval_code")
    public String getDictValCode() {
        return dictValCode;
    }

    public void setDictValCode(String dictValCode) {
        this.dictValCode = dictValCode;
    }

    @Column(name="remark_")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @OrderBy("sno")
    @Column(name="sno")
    public Integer getSno() {
        return sno;
    }

    public void setSno(Integer sno) {
        this.sno = sno;
    }
}
