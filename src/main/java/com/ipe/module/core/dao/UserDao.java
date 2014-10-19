package com.ipe.module.core.dao;

import com.ipe.common.dao.BaseDao;
import com.ipe.module.core.entity.Role;
import com.ipe.module.core.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tangdu
 * Date: 13-8-25
 * Time: 下午10:31
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class UserDao extends BaseDao<User, String> {

    public List<User> login(String account, String pwd) {
        return list("from User u where u.userAccount=? and u.userPwd=? and u.enabled='1'", account, pwd);
    }

    public List<User> findUserByAccount(String account) {
        return list("from User u where u.userAccount=?  and u.enabled='1'", account);
    }

    public int updatePwd(String id, String userPwd, String ouserPwd) {
        return execute("update User u set u.userPwd=? where u.id=? and u.userPwd=?", id, userPwd, ouserPwd);
    }

    public List<Role> getUserRole(String userId) {
        return list("select t.role from UserRole t where t.user.id=?", userId);
    }

    public List<String> getUserRoleIds(String userId) {
        return list("select t.role.id from UserRole t where t.user.id=?", userId);
    }

    public List<Role> getUserNotRole(String userId) {
        return list("from Role t0 where t0.id not in(select t.role from UserRole t where t.user.id=?)", userId);
    }
}