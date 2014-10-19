package com.ipe.module.core.web.controller;

import com.ipe.module.core.entity.Organization;
import com.ipe.module.core.service.OrganizationService;
import com.ipe.module.core.web.util.BodyWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

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
}
