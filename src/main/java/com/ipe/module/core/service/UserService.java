package com.ipe.module.core.service;

import com.ipe.common.dao.BaseDao;
import com.ipe.common.service.BaseService;
import com.ipe.module.core.dao.AuthorityDao;
import com.ipe.module.core.dao.LogDao;
import com.ipe.module.core.dao.UserDao;
import com.ipe.module.core.entity.Resource;
import com.ipe.module.core.entity.Role;
import com.ipe.module.core.entity.User;
import com.ipe.module.core.web.security.SystemRealm;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: tangdu
 * Date: 13-8-25
 * Time: 下午10:38
 * To change this template use File | Settings | File Templates.
 */
@Service
@Transactional(readOnly = true)
public class UserService extends BaseService<User, String> {
    @Autowired
    private UserDao userDao;
    @Autowired
    private AuthorityDao authorityDao;
    @Autowired
    private LogDao logDao;

    @Override
    public BaseDao<User, String> getBaseDao() {
        return userDao;
    }

    public List<User> login(String useraccount, String userpwd) {
        return userDao.login(useraccount, userpwd);
    }

    @Transactional
    public void logout() {
        SystemRealm.UserInfo user = null;
        if (SecurityUtils.getSubject().getPrincipal() != null) {
            user = (SystemRealm.UserInfo) SecurityUtils.getSubject().getPrincipal();
            //最大日志
            Date maxDate = logDao.findMaxAccessTime(user.getUserId());
            logDao.updateLogoutTime(new Date(), maxDate, user.getUserId());
        }
    }

    @Transactional
    public boolean updatePwd(String id, String userPwd, String ouserPwd) {
        int cot = userDao.updatePwd(id, userPwd, ouserPwd);
        return cot > 0 ? true : false;
    }

    public List<Role> getUserRole(String userId) {
        return userDao.getUserRole(userId);
    }

    public List<Role> getUserNotRole(String userId) {
        return userDao.getUserNotRole(userId);
    }

    public Set<Resource> getUserAuthority(String roleId) {
        return authorityDao.getRoleByAuthority(roleId);
    }

    public User findUserByAccount(String userAccount) {
        return userDao.findUserByAccount(userAccount).get(0);
    }
}
