package com.ipe.module.core.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ipe.module.core.entity.Organization;
import com.ipe.module.core.service.OrganizationService;
import com.ipe.module.core.web.util.BodyWrapper;
import com.ipe.module.core.web.util.RestRequest;

/**
 * Created with IntelliJ IDEA.
 * User: tangdu
 * Date: 13-12-14
 * Time: 下午12:10
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/org")
public class OrganizationController extends AbstractController {
    @Autowired
    private OrganizationService organizationService;


    @RequestMapping(value={"/getTree"})
    public @ResponseBody
    BodyWrapper getTree(String pid){
        try {
            List<Organization> data= organizationService.getTree(pid);
            return success(data);
        }catch (Exception e){
            return failure(e);
        }
    }
    
    
    @RequestMapping(value={"/edit"},method = RequestMethod.POST)
    public @ResponseBody BodyWrapper edit(Organization organization,RestRequest rest){
        try {
        	Organization parent=organizationService.get(organization.getParent().getId());
        	organization.setParent(parent);
        	organizationService.update(organization);
            return success(organization);
        }catch (Exception e){
            LOGGER.error("ERROR",e);
            return failure(e);
        }
    }

    @RequestMapping(value={"/add"},method = RequestMethod.POST)
    public @ResponseBody BodyWrapper add(Organization organization,RestRequest rest){
        try {
        	organizationService.save(organization);
            return success(organization);
        }catch (Exception e){
            LOGGER.error("ERROR",e);
            return failure(e);
        }
    }

    @RequestMapping(value={"/del"})
    public @ResponseBody BodyWrapper del(String [] ids,RestRequest rest){
        try {
        	organizationService.delete(ids);
            return success();
        }catch (Exception e){
            LOGGER.error("ERROR",e);
            return failure(e);
        }
    }

}
