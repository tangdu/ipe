package com.ipe.module.core.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ipe.common.entity.IDEntity;
import com.ipe.common.util.Constants;

@Table(name = "t_sys_message", schema = "", catalog = Constants.SCHEMA)
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties({ "parent" })
public class Message extends IDEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5700122913075197740L;

	private String userId;
	private String msgType;
	private String read;
	private String msgContent;
	private Date createdDate;

	@Column(name = "msg_content")
	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	@Column(name = "user_id")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "msg_type")
	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	@Column(name = "read")
	public String getRead() {
		return read;
	}

	public void setRead(String read) {
		this.read = read;
	}

	@Column(name = "created_date", updatable = false)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this,SerializerFeature.UseSingleQuotes,SerializerFeature.UseISO8601DateFormat);
	}
}
