package com.ipe.module.core.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ipe.common.util.Logs;
import com.ipe.module.core.entity.Dict;
import com.ipe.module.core.service.DictService;
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
@RequestMapping("/dict")
public class DictController extends AbstractController {

    private static final Logger LOG= LoggerFactory.getLogger(DictController.class);
    @Autowired
    private DictService dictService;

    @RequestMapping(value = {"/list"})
    public
    @ResponseBody
    BodyWrapper list(Dict dict, RestRequest rest) {
        try {
            dictService.where(rest.getPageModel());
            return success(rest.getPageModel());
        } catch (Exception e) {
            LOG.error("query error",e);
            return failure(e);
        }
    }

    @Logs(opdesc = "编辑字典库")
    @RequestMapping(value = {"/edit"}, method = RequestMethod.POST)
    public
    @ResponseBody
    BodyWrapper edit(Dict dict, RestRequest rest) {
        try {
            dictService.update(dict);
            return success(dict);
        } catch (Exception e) {
            LOG.error("edit error",e);
            return failure(e);
        }
    }

    @Logs(opdesc = "添加字典库")
    @RequestMapping(value = {"/add"}, method = RequestMethod.POST)
    public
    @ResponseBody
    BodyWrapper add(Dict dict, RestRequest rest) {
        try {
            dictService.save(dict);
            return success(dict);
        } catch (Exception e) {
            LOG.error("add error",e);
            return failure(e);
        }
    }

    @Logs(opdesc = "删除字典库")
    @RequestMapping(value = {"/del"})
    public
    @ResponseBody
    BodyWrapper del(String[] ids, RestRequest rest) {
        try {
            dictService.delete(ids);
            return success();
        } catch (Exception e) {
            LOG.error("del error",e);
            return failure(e);
        }
    }
}
