package com.ipe.module.exl.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ipe.common.util.PageModel;
import com.ipe.module.core.web.controller.AbstractController;
import com.ipe.module.core.web.util.BodyWrapper;
import com.ipe.module.core.web.util.RestRequest;
import com.ipe.module.core.web.util.WebUtil;
import com.ipe.module.exl.entity.ExlExptpl;
import com.ipe.module.exl.service.ExlExptplService;

@Controller
@RequestMapping("/exlExptpl")
public class ExlExptplController extends AbstractController{

	@Autowired
	private ExlExptplService exlExptplService;
	
	@RequestMapping(value = {"/list"})
    public
    @ResponseBody
    BodyWrapper list(ExlExptpl exlExptpl, RestRequest rest) {
        try {
            PageModel page=rest.getPageModel();
            exlExptplService.where(page);
            return success(page);
        } catch (Exception e) {
            LOGGER.error("query error",e);
            return failure(e);
        }
    }
	
	
	@RequestMapping(value = {"/edit"}, method = RequestMethod.POST)
    public
    @ResponseBody
    BodyWrapper edit(ExlExptpl exlExptpl,String details) {
        try {
        	exlExptpl.setUpdatedDate(new Date());
        	exlExptplService.update(exlExptpl);
            return success(exlExptpl);
        } catch (Exception e) {
            LOGGER.error("edit error",e);
            return failure(e);
        }
    }

    @RequestMapping(value = {"/add"}, method = RequestMethod.POST)
    public
    @ResponseBody
    BodyWrapper add(ExlExptpl exlExptpl,String details) {
        try {
        	exlExptpl.setCreatedDate(new Date());
        	exlExptplService.save(exlExptpl);
            return success(exlExptpl);
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
        	exlExptplService.delete(ids);
            return success();
        } catch (Exception e) {
            LOGGER.error("del error",e);
            return failure(e);
        }
    }
    
    
    @RequestMapping(value = {"/loadColumns"}, method = RequestMethod.POST)
    public
    @ResponseBody
    BodyWrapper loadColumns(String sqlTpl,Map<String,Object> args) {
        try {
        	Map<String,Object> row=new HashMap<String, Object>();
        	List<String> titles=new ArrayList<String>();
        	List<Map<String,Object>> list=exlExptplService.loadColumns(sqlTpl, args);
        	if(list!=null && !list.isEmpty()){
        		for(Map.Entry<String, Object> en:list.get(0).entrySet()){
        			titles.add(en.getKey());
        		}
        	}
        	row.put("data", list);
        	row.put("titles", titles);
        	return success(row);
        } catch (Exception e) {
            LOGGER.error("add error",e);
            return failure(e);
        }
    }

    /**
     * 导出Excel数据
     * @param id
     * @param multipartHttpServletRequest
     * @return
     */
    @RequestMapping(value = {"/expexcelData"})
    public void expexcelData(String code,Map<String,Object> args,HttpServletResponse response) {
    	ServletOutputStream outputStream=null;
        try {
        	outputStream=response.getOutputStream();
        	WebUtil.setDownHeader(response,code+".xlsx");
        	exlExptplService.exportData(code,args, outputStream);
        } catch (Exception e) {
            LOGGER.error("expexcelData error",e);
            super.downFileError(response);
        } finally{
        	try {
        		if(outputStream!=null){
        			response.flushBuffer();
        			outputStream.flush();
        			outputStream.close();
        		}
			} catch (IOException e) {
				LOGGER.error("expexcelData error",e);
			}
        }
    }
    
}
