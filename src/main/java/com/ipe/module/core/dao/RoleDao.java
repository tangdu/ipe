package com.ipe.module.core.dao;

import com.ipe.common.dao.BaseDao;
import com.ipe.module.core.entity.Role;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: tangdu
 * Date: 13-9-7
 * Time: 下午9:53
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class RoleDao extends BaseDao<Role,String> {

    public int delUserRole(String userId){
        return execute("delete from UserRole t where t.user.id=?",userId);
    }
}
