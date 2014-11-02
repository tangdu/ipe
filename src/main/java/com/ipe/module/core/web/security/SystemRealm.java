package com.ipe.module.core.web.security;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ipe.module.core.entity.Log;
import com.ipe.module.core.entity.Resource;
import com.ipe.module.core.entity.Role;
import com.ipe.module.core.entity.User;
import com.ipe.module.core.service.LogService;
import com.ipe.module.core.service.UserService;

/**
 * Created with IntelliJ IDEA. User: tangdu Date: 13-6-30 Time: 下午9:26 To change
 * this template use File | Settings | File Templates.
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

	// 授权
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principalCollection) {
		UserInfo user = (UserInfo) getAvailablePrincipal(principalCollection);
		// String userId
		// =(String)principalCollection.fromRealm(getName()).iterator().next();
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		// User user=userService.get(userId.getId());
		String userId = user.getUserId();

		// 得到用户所拥有有角色
		List<Role> roles = userService.getUserRole(userId);
		for (Role role : roles) {
			info.addRole(role.getRoleCode());
			// 得到用户所拥有的资源权限
			Set<Resource> resources = userService
					.getUserAuthority(role.getId());
			for (Resource resource : resources) {
				if (StringUtils.isNotBlank(resource.getResourceUrl())
						&& !"null".equals(resource.getResourceUrl())) {
					info.addStringPermission(resource.getResourceUrl());
				}
			}
		}
		return info;
	}

	// 验证
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authenticationToken)
			throws AuthenticationException {
		CustUsernamePasswordToken upToken = (CustUsernamePasswordToken) authenticationToken;
		upToken.setRememberMe(true);
		String captcha = upToken.getCaptcha();
		//验证码功能
		String exitCode = (String) SecurityUtils.getSubject().getSession().getAttribute("captcha");
		if (null == captcha || !captcha.equalsIgnoreCase(exitCode)) {
             throw new CaptchaException("验证码错误");
        }

		User user = getLoginUser(upToken);
		if (user != null) {
			return new SimpleAuthenticationInfo(new UserInfo(user,upToken.getAccessIp()),
					user.getUserPwd(), getName());
		}
		return null;
	}

	private User getLoginUser(AuthenticationToken authenticationToken) {
		CustUsernamePasswordToken upToken = (CustUsernamePasswordToken) authenticationToken;
		List<User> users = userService.login(upToken.getUsername(),
				String.valueOf(upToken.getPassword()));
		User user = users.get(0);
		if (user != null) {
			Session session = SecurityUtils.getSubject().getSession();
			session.setAttribute("user", JSON.toJSONString(user,
					SerializerFeature.WriteDateUseDateFormat));
			// /保存日志
			Log log = new Log();
			log.setAccessIp(upToken.getHost());
			log.setAccessMethod("login");
			log.setAccessPerson(user.getUserAccount() + "-"
					+ user.getUserName());
			log.setAccessTime(new Date());
			log.setOperate(upToken.getMethod() + "_" + upToken.getAccessUrl());
			log.setLogType("L1");
			log.setAccessUserid(user.getId());
			logService.save(log);
			return user;
		}
		return null;
	}

	public static class UserInfo implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 6116630372427460576L;
		private String userId;
		private String userAccount;
		private String admin;
		private String roleId;
		private String userName;
		private Date userLoginTime = new Date();
		private String accessIp;

		public UserInfo(User user,String accessIp) {
			this.userId = user.getId();
			this.admin=user.getAdmin();
			this.userName = user.getUserName();
			this.userAccount = user.getUserAccount();
			this.accessIp = accessIp;
		}
		public UserInfo(User user,String accessIp,String roleId) {
			this.userId = user.getId();
			this.roleId=roleId;
			this.admin=user.getAdmin();
			this.userName = user.getUserName();
			this.userAccount = user.getUserAccount();
			this.accessIp = accessIp;
		}

		public String getRoleId() {
			return roleId;
		}


		public void setRoleId(String roleId) {
			this.roleId = roleId;
		}


		public String getAdmin() {
			return admin;
		}


		public void setAdmin(String admin) {
			this.admin = admin;
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

		public String getAccessIp() {
			return accessIp;
		}

		public void setAccessIp(String accessIp) {
			this.accessIp = accessIp;
		}

	}
}
