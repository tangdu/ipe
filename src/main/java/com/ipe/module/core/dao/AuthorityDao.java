package com.ipe.module.core.dao;

import com.ipe.common.dao.BaseDao;
import com.ipe.module.core.entity.Authority;
import com.ipe.module.core.entity.Resource;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: tangdu
 * Date: 13-9-14
 * Time: 下午10:58
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class AuthorityDao extends BaseDao<Authority, String> {

    public int delRoleAuthority(String roleId) {
        return execute("delete from Authority t where t.role.id=?", roleId);
    }

    public Set<Resource> getRoleByAuthority(String roleId) {
        Set<Resource> resources = new HashSet<Resource>();
        List<Resource> dt = list("select t.resource from Authority t where t.role.id=?", roleId);

        for (Resource resource : dt) {
            resources.add(resource);
        }
        return resources;
    }
}

