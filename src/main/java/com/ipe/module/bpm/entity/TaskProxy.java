package com.ipe.module.bpm.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ipe.common.entity.IDEntity;
import com.ipe.module.core.entity.User;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by tangdu on 14-2-16.
 */
@JsonAutoDetect
@Entity
@Table(name = "act_ru_taskproxy", schema = "", catalog = "db_work")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class TaskProxy extends IDEntity {
    private User userForm;
    private User userTo;
    private Date startDate;
    private Date endDate;
    private String reason;
    private String status;
    private Date createdDate;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_form")
    public User getUserForm() {
        return userForm;
    }

    public void setUserForm(User userForm) {
        this.userForm = userForm;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_to")
    public User getUserTo() {
        return userTo;
    }

    public void setUserTo(User userTo) {
        this.userTo = userTo;
    }

    @Column(name = "start_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Column(name = "end_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Column(name = "reason")
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "created_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
