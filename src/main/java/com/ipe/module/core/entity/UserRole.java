package com.ipe.module.core.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ipe.common.entity.IDEntity;
import com.ipe.common.util.Constants;

/**
 * Created with IntelliJ IDEA.
 * User: tangdu
 * Date: 13-9-7
 * Time: 下午9:10
 * To change this template use File | Settings | File Templates.
 */
@Table(name = "t_cor_user_role", schema = "", catalog = Constants.SCHEMA)
@Entity
public class UserRole extends IDEntity {
    /**
	 * 
	 */
	private static final long serialVersionUID = -7490830091549504364L;
	//关联角色
    private Role role;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id_")
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }


    //关联用户
    private User user;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id_")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
