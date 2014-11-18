package com.ipe.module.core.web.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ipe.module.core.entity.DictVal;
import com.ipe.module.core.service.DictValService;
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
@RequestMapping("/dictVal")
public class DictValController extends AbstractController {

    @Autowired
    private DictValService dictValService;

    @RequestMapping(value = {"/list"})
    public
    @ResponseBody
    BodyWrapper list(final String dictId,String code, RestRequest rest) {
        try {
        	if(StringUtils.isNotBlank(code)){
        		dictValService.where(rest.getPageModel()," and dictId=? and dictValCode=?",dictId,code);
        	}else{
        		dictValService.where(rest.getPageModel()," and dictId=?",dictId);
        	}
            return success(rest.getPageModel());
        } catch (Exception e) {
            LOGGER.error("query error",e);
            return failure(e);
        }
    }

    @RequestMapping(value = {"/edit"}, method = RequestMethod.POST)
    public
    @ResponseBody
    BodyWrapper edit(DictVal dictVal, RestRequest rest) {
        try {
            dictValService.update(dictVal);
            return success(dictVal);
        } catch (Exception e) {
            LOGGER.error("edit error",e);
            return failure(e);
        }
    }

    @RequestMapping(value = {"/add"}, method = RequestMethod.POST)
    public
    @ResponseBody
    BodyWrapper add(DictVal dictVal, RestRequest rest) {
        try {
            dictValService.save(dictVal);
            return success(dictVal);
        } catch (Exception e) {
            LOGGER.error("add error",e);
            return failure(e);
        }
    }

    @RequestMapping(value = {"/del"})
    public
    @ResponseBody
    BodyWrapper del(String[] ids, RestRequest rest) {
        try {
            dictValService.delete(ids);
            return success();
        } catch (Exception e) {
            LOGGER.error("del error",e);
            return failure(e);
        }
    }
    
    @RequestMapping(value = {"/saveSort"})
    public
    @ResponseBody
    BodyWrapper saveSort(String[] ids, RestRequest rest) {
        try {
            dictValService.updateSno(ids);
            return success();
        } catch (Exception e) {
            LOGGER.error("del error",e);
            return failure(e);
        }
    }
}
