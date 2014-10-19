package com.ipe.module.core.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ipe.common.entity.IDEntity;
import com.ipe.common.util.Constants;

/**
 * Created by tangdu on 14-2-16.
 */
@JsonAutoDetect
@Entity
@Table(name = "t_sys_notice", schema = "", catalog = Constants.SCHEMA)
public class Notice extends IDEntity {

    /**
	 * 
	 */
	private static final long serialVersionUID = -4765306111286151445L;
	private String content;
    private String appendixPath;
    private String appendixName;
    private Date createdDate;
    private String userId;

    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(name = "appendix_path")
    public String getAppendixPath() {
        return appendixPath;
    }

    public void setAppendixPath(String appendixPath) {
        this.appendixPath = appendixPath;
    }

    @Column(name = "appendix_name")
    public String getAppendixName() {
        return appendixName;
    }

    public void setAppendixName(String appendixName) {
        this.appendixName = appendixName;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Column(name = "user_id")

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
