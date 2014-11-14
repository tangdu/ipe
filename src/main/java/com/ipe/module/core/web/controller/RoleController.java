package com.ipe.module.core.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ipe.module.core.entity.Resource;
import com.ipe.module.core.entity.Role;
import com.ipe.module.core.service.RoleService;
import com.ipe.module.core.web.util.BodyWrapper;
import com.ipe.module.core.web.util.RestRequest;


/**
 * Created with IntelliJ IDEA.
 * Role: tangdu
 * Date: 13-9-7
 * Time: 下午10:27
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/role")
public class RoleController extends AbstractController {

    @Autowired
    private RoleService roleService;

    @RequestMapping(value = {"/list"})
    public
    @ResponseBody
    BodyWrapper list(Role role, RestRequest rest) {
        try {
        	StringBuffer wh=new StringBuffer();
        	List<Object> params=new ArrayList<Object>();
        	if(role!=null){
        		if(StringUtils.isNotBlank(role.getRoleName())){
        			wh.append(" and roleName like ?");
        			params.add("%"+role.getRoleName());
        		}
        		if(StringUtils.isNotBlank(role.getRoleCode())){
        			wh.append(" and roleCode like ?");
        			params.add("%"+role.getRoleCode());
        		}
        		if(StringUtils.isNotBlank(role.getEnabled())){
        			wh.append(" and enabled= ?");
        			params.add(role.getEnabled());
        		}
        	}
            roleService.where(rest.getPageModel(),wh.toString(),params);
            return success(rest.getPageModel());
        } catch (Exception e) {
            LOGGER.error("query error",e);
            return failure(e);
        }
    }

    @RequestMapping(value = {"/edit"}, method = RequestMethod.POST)
    public
    @ResponseBody
    BodyWrapper edit(Role role, RestRequest rest) {
        try {
            role.setUpdatedDate(new Date());
            roleService.update(role);
            return success(role);
        } catch (Exception e) {
            LOGGER.error("edit error",e);
            return failure(e);
        }
    }

    @RequestMapping(value = {"/add"}, method = RequestMethod.POST)
    public
    @ResponseBody
    BodyWrapper add(Role role, RestRequest rest) {
        try {
            role.setCreatedDate(new Date());
            roleService.save(role);
            return success(role);
        } catch (Exception e) {
            LOGGER.error("add error",e);
            return failure(e);
        }
    }

    @RequestMapping(value = {"/del"})
    public
    @ResponseBody
    BodyWrapper del(String id, RestRequest rest) {
        try {
            roleService.delete(id);
            return success();
        } catch (Exception e) {
            LOGGER.error("del error",e);
            return failure(e);
        }
    }

    /**
     * 用户配置角色
     * @param urids
     * @param userId
     * @return
     */
    @RequestMapping(value = {"/addUserRole"})
    public
    @ResponseBody
    BodyWrapper addUserRole(String[] urids,@RequestParam String userId) {
        try {
            roleService.saveUserRole(urids,userId);
            return success();
        } catch (Exception e) {
            LOGGER.error("addRole error",e);
            return failure(e);
        }
    }

    /**
     * 角色配置权限
     * @param ids
     * @param roleId
     * @return
     */
    @RequestMapping(value = {"/addAuthority"})
    public
    @ResponseBody
    BodyWrapper addAuthority(String[] ids,@RequestParam String roleId) {
        try {
            roleService.saveAuthority(ids,roleId);
            return success();
        } catch (Exception e) {
            LOGGER.error("addAuthority error",e);
            return failure(e);
        }
    }

    /**
     * 角色拥有权限
     * @param roleId
     * @return
     */
    @RequestMapping(value = {"/getAuthority"})
    public
    @ResponseBody
    BodyWrapper getAuthority(@RequestParam String roleId) {
        try {
            Resource root=roleService.getAuthoritys(roleId);
            return success(root);
        } catch (Exception e) {
            LOGGER.error("getAuthority error",e);
            return failure(e);
        }
    }
    
    /**
     * 得到用户所有角色列表
     * @param roleId
     * @return
     */
    @RequestMapping(value = {"/getUserRoles"})
    public
    @ResponseBody
    BodyWrapper getUserRoles(@RequestParam String userId) {
        try {
            Resource root=roleService.getAuthoritys(userId);
            return success(root);
        } catch (Exception e) {
            LOGGER.error("getAuthority error",e);
            return failure(e);
        }
    }
}
