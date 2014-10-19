package com.ipe.module.core.web.controller;

import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ipe.module.core.entity.Menu;
import com.ipe.module.core.service.MenuService;
import com.ipe.module.core.web.security.SystemRealm;
import com.ipe.module.core.web.util.BodyWrapper;
import com.ipe.module.core.web.util.RestRequest;

/**
 * Created with IntelliJ IDEA.
 * Menu: tangdu
 * Date: 13-9-7
 * Time: 下午10:27
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/menu")
public class MenuController extends AbstractController {
    private static final Logger LOG= LoggerFactory.getLogger(MenuController.class);

    @Autowired
    private MenuService menuService;

    @RequestMapping(value={"/list"})
    public @ResponseBody BodyWrapper list(Menu menu,RestRequest rest){
        try {
            menuService.where(rest.getPageModel());
            return success(rest.getPageModel());
        }catch (Exception e){
            LOG.error("Exception {}",e);
            return failure(e);
        }
    }


    @RequestMapping(value={"/getMenus"})
    public @ResponseBody BodyWrapper getMenus(String pid){
        try {
            List<Menu> data= menuService.getMenus(pid);
            return success(data);
        }catch (Exception e){
            LOG.error("Exception {}",e);
            return failure(e);
        }
    }

    @RequestMapping(value={"/getRoleMenus"})
    public @ResponseBody BodyWrapper getRoleMenus(){
        try {
            SystemRealm.UserInfo userInfo= (SystemRealm.UserInfo) SecurityUtils.getSubject().getPrincipal();
            String result=menuService.getUserMenu(userInfo.getUserId());
            return success(result);
        }catch (Exception e){
            LOG.error("Exception {}",e);
            return failure(e);
        }
    }

    @RequestMapping(value={"/edit"},method = RequestMethod.POST)
    public @ResponseBody BodyWrapper edit(Menu menu,RestRequest rest){
        try {
            menu.setParent(menuService.get(menu.getParent().getId()));
            menu.setUpdatedDate(new Date());
            menuService.update(menu);
            return success(menu);
        }catch (Exception e){
            LOG.error("Exception {}",e);
            return failure(e);
        }
    }

    @RequestMapping(value={"/add"},method = RequestMethod.POST)
    public @ResponseBody BodyWrapper add(Menu menu,RestRequest rest){
        try {
            menuService.saveMenu(menu);
            return success(menu);
        }catch (Exception e){
            LOG.error("Exception {}",e);
            return failure(e);
        }
    }

    @RequestMapping(value={"/del"})
    public @ResponseBody BodyWrapper del(String [] ids,RestRequest rest){
        try {
            menuService.delete(ids);
            return success();
        }catch (Exception e){
            LOG.error("Exception {}",e);
            return failure(e);
        }
    }

    /**
     * 更新排序及位置
     * @param ids
     * @param pid
     */
    @RequestMapping(value={"/update"})
    public @ResponseBody BodyWrapper update(String [] ids,String pid){
        try {
            menuService.updateMenus(ids,pid);
            return success();
        }catch (Exception e){
            LOG.error("Exception {}",e);
            return failure(e);
        }
    }
}
