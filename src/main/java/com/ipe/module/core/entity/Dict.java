package com.ipe.module.core.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ipe.common.entity.IDEntity;
import com.ipe.common.util.Constants;

import javax.persistence.*;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: tangdu
 * Date: 13-12-16
 * Time: 下午9:10
 * To change this template use File | Settings | File Templates.
 */
@JsonAutoDetect
@Entity
@Table(name = "t_sys_dict", schema = "",catalog=Constants.SCHEMA)
@JsonIgnoreProperties({"dictVals"})
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Dict  extends IDEntity {
    private static final long serialVersionUID = 1L;
    private String dictName;
    private String dictCode;
    private String remark;
    private Integer sno;
    private String status;

    @Column(name="dict_name")
    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    @Column(name="dict_code")
    public String getDictCode() {
        return dictCode;
    }

    public void setDictCode(String dictCode) {
        this.dictCode = dictCode;
    }

    @Column(name="remark_")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name="sno")
    public Integer getSno() {
        return sno;
    }

    public void setSno(Integer sno) {
        this.sno = sno;
    }

    @Column(name="status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private Set<DictVal> dictVals;
    //关联库值
    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name="dict_id",referencedColumnName = "id_",updatable = false)
    public Set<DictVal> getDictVals() {
        return dictVals;
    }

    public void setDictVals(Set<DictVal> dictVals) {
        this.dictVals = dictVals;
    }
}
