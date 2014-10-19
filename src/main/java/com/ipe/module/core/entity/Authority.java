package com.ipe.module.core.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ipe.common.entity.IDEntity;
import com.ipe.common.util.Constants;

/**
 * Created with IntelliJ IDEA.
 * User: tangdu
 * Date: 13-9-7
 * Time: 下午9:10
 * To change this template use File | Settings | File Templates.
 */
@Table(name = "t_cor_authority", schema = "", catalog = Constants.SCHEMA)
@Entity
@JsonInclude(Include.NON_NULL)
public class Authority extends IDEntity {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1637226613216296857L;
	private Resource resource;

    @ManyToOne
    @JoinColumn(name = "resource_id", referencedColumnName = "id_")
    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    private Role role;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id_")
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
