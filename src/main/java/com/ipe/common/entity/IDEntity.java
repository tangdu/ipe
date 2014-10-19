package com.ipe.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

/**
 * Created with IntelliJ IDEA.
 * User: tangdu
 * Date: 13-8-26
 * Time: 上午12:13
 * To change this template use File | Settings | File Templates.
 */
@MappedSuperclass
public abstract class IDEntity implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 3793056349652429328L;
	private String id;
    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @GeneratedValue(generator = "system-uuid")
    @Column(name = "id_",unique = true, nullable = false)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
