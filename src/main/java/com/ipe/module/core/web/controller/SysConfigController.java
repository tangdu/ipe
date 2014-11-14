package com.ipe.module.core.web.controller;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ipe.module.core.entity.SysConfig;
import com.ipe.module.core.service.SysConfigService;
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
@RequestMapping("/sysConfig")
public class SysConfigController extends AbstractController {

    @Autowired
    private SysConfigService sysConfigService;
    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @RequestMapping(value = {"/list"})
    public
    @ResponseBody
    BodyWrapper list(SysConfig sysConfig, RestRequest rest) {
        try {
            List<SysConfig> data = sysConfigService.listAll();
            Map<String, Object> map = new HashMap<String, Object>();
            if (data != null) {
                for (SysConfig obj : data) {
                	map.put(obj.getKey(), obj.getValue());
                }
            }
            return success(map);
        } catch (Exception e) {
            LOGGER.error("query error", e);
            return failure(e);
        }
    }

    @RequestMapping(value = {"/edit"}, method = RequestMethod.POST)
    public
    @ResponseBody
    BodyWrapper edit(String params, RestRequest rest) {
        try {
            sysConfigService.update(params);
            return success();
        } catch (Exception e) {
            LOGGER.error("edit error", e);
            return failure(e);
        }
    }

    @RequestMapping(value = {"/add"}, method = RequestMethod.POST)
    public
    @ResponseBody
    BodyWrapper add(SysConfig sysConfig, RestRequest rest) {
        try {
            sysConfigService.save(sysConfig);
            return success(sysConfig);
        } catch (Exception e) {
            LOGGER.error("add error", e);
            return failure(e);
        }
    }

    @RequestMapping(value = {"/del"})
    public
    @ResponseBody
    BodyWrapper del(String[] ids, RestRequest rest) {
        try {
            sysConfigService.delete(ids);
            return success();
        } catch (Exception e) {
            LOGGER.error("del error", e);
            return failure(e);
        }
    }
}
