package com.ipe.module.core.web.security;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ipe.module.core.entity.Log;
import com.ipe.module.core.entity.Resource;
import com.ipe.module.core.entity.Role;
import com.ipe.module.core.entity.User;
import com.ipe.module.core.service.LogService;
import com.ipe.module.core.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: tangdu
 * Date: 13-6-30
 * Time: 下午9:26
 * To change this template use File | Settings | File Templates.
 */
public class SystemRealm extends AuthorizingRealm {

    private UserService userService;
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    private LogService logService;
    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        UserInfo user=(UserInfo)getAvailablePrincipal(principalCollection);
        //String userId =(String)principalCollection.fromRealm(getName()).iterator().next();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //User user=userService.get(userId.getId());
        String userId=user.getUserId();

        //得到用户所拥有有角色
        List<Role> roles=userService.getUserRole(userId);
        for(Role role:roles){
            info.addRole(role.getRoleName());
            //得到用户所拥有的资源权限
            Set<Resource> resources=userService.getUserAuthority(role.getId());
            for(Resource resource:resources){
                info.addStringPermission(resource.getResourceUrl());
            }
        }
        return info;
    }

    //验证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        User user = getLoginUser(authenticationToken);
        if (user != null) {
            return new SimpleAuthenticationInfo(new UserInfo(user), user.getUserPwd(), getName());
        }
        return null;
    }

    private User getLoginUser(AuthenticationToken authenticationToken) {
        CustUsernamePasswordToken upToken = (CustUsernamePasswordToken) authenticationToken;
        upToken.setRememberMe(true);
        List<User> users =userService.login(upToken.getUsername(), String.valueOf(upToken.getPassword()));
        User user = users.get(0);
        if (user != null) {
            Session session= SecurityUtils.getSubject().getSession();
            session.setAttribute("user", JSON.toJSONString(user, SerializerFeature.WriteDateUseDateFormat));
            ///保存日志
            Log log = new Log();
            log.setAccessIp(upToken.getHost());
            log.setAccessMethod("login");
            log.setAccessPerson(user.getUserAccount()+"-"+user.getUserName());
            log.setAccessTime(new Date());
            log.setOperate(upToken.getMethod()+"_"+upToken.getAccessUrl());
            log.setLogType("01");
            log.setAccessUserid(user.getId());
            logService.save(log);
            return user;
        }
        return null;
    }

    public static class UserInfo implements Serializable{
        /**
		 * 
		 */
		private static final long serialVersionUID = 6116630372427460576L;
		private String userId;
        private String userAccount;
        private String userName;
        private Date userLoginTime=new Date();

        public UserInfo(User user){
            this.userId=user.getId();
            this.userName=user.getUserName();
            this.userAccount=user.getUserAccount();
        }
        public String getUserId() {
            return userId;
        }
        public String getUserName() {
            return userName;
        }

        public String getUserAccount() {
            return userAccount;
        }

        public Date getUserLoginTime() {
            return userLoginTime;
        }
    }
}
