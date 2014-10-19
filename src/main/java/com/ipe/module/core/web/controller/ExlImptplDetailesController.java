package com.ipe.module.core.web.controller;

import com.ipe.module.core.entity.ExlImptplDetailes;
import com.ipe.module.core.service.ExlImptplDetailesService;
import com.ipe.module.core.web.util.BodyWrapper;
import com.ipe.module.core.web.util.RestRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Role: tangdu
 * Date: 13-9-7
 * Time: 下午10:27
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/exlImptplDetailes")
public class ExlImptplDetailesController extends AbstractController {

    private static final Logger LOG= LoggerFactory.getLogger(ExlImptplDetailesController.class);
    @Autowired
    private ExlImptplDetailesService exlImptplDetailesService;

    @RequestMapping(value = {"/getByTplId"})
    public
    @ResponseBody
    BodyWrapper getByTplId(@RequestParam final String tplId) {
        try {
            List<ExlImptplDetailes> detaileses=exlImptplDetailesService.where("exlImptpl.id=?", tplId);
            return success(detaileses);
        } catch (Exception e) {
            LOG.error("query error",e);
            return failure(e);
        }
    }

    @RequestMapping(value = {"/edit"}, method = RequestMethod.POST)
    public
    @ResponseBody
    BodyWrapper edit(ExlImptplDetailes exlImptplDetailes, RestRequest rest) {
        try {
            exlImptplDetailesService.save(exlImptplDetailes);
            return success(exlImptplDetailes);
        } catch (Exception e) {
            LOG.error("edit error",e);
            return failure(e);
        }
    }

    @RequestMapping(value = {"/add"}, method = RequestMethod.POST)
    public
    @ResponseBody
    BodyWrapper add(ExlImptplDetailes exlImptplDetailes, RestRequest rest) {
        try {
            exlImptplDetailesService.save(exlImptplDetailes);
            return success(exlImptplDetailes);
        } catch (Exception e) {
            LOG.error("add error",e);
            return failure(e);
        }
    }

    @RequestMapping(value = {"/del"})
    public
    @ResponseBody
    BodyWrapper del(String[] ids, RestRequest rest) {
        try {
            exlImptplDetailesService.delete(ids);
            return success();
        } catch (Exception e) {
            LOG.error("del error",e);
            return failure(e);
        }
    }
}
