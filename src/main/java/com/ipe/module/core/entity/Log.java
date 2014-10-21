package com.ipe.module.core.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ipe.common.entity.IDEntity;
import com.ipe.common.util.Constants;

/**
 * 日志记录器
 * 
 * @author tangdu
 * 
 * @time 2013-5-12 上午8:34:20
 */
@JsonAutoDetect
@Entity
@Table(name = "t_sys_log", schema = "",catalog=Constants.SCHEMA)
public class Log extends IDEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String accessMethod;
	private String accessPerson;
	private String logType;
	private String accessIp;
	private String operate;
	private String remark;
	private Date accessTime;
    private Date leaveTime;
    private String accessUserid;

    @Column(name = "access_userid")
    public String getAccessUserid() {
        return accessUserid;
    }

    public void setAccessUserid(String accessUserid) {
        this.accessUserid = accessUserid;
    }

    @Column(name = "access_method")
	public String getAccessMethod() {
		return accessMethod;
	}

	public void setAccessMethod(String accessMethod) {
		this.accessMethod = accessMethod;
	}

	@Column(name = "access_person")
	public String getAccessPerson() {
		return accessPerson;
	}

	public void setAccessPerson(String accessPerson) {
		this.accessPerson = accessPerson;
	}

	@Column(name = "log_type")
	public String getLogType() {
		return logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	@Column(name = "access_ip")
	public String getAccessIp() {
		return accessIp;
	}

	public void setAccessIp(String accessIp) {
		this.accessIp = accessIp;
	}

	@Column(name = "operate_")
	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	@Column(name = "remark_")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "access_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @OrderBy("desc")
	public Date getAccessTime() {
		return accessTime;
	}

	public void setAccessTime(Date accessTime) {
		this.accessTime = accessTime;
	}

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "leave_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(Date leaveTime) {
        this.leaveTime = leaveTime;
    }
}
