package com.ipe.module.core.web.controller;

import com.alibaba.fastjson.JSON;
import com.ipe.common.util.PageModel;
import com.ipe.common.web.BaseController;
import com.ipe.module.core.web.util.BodyWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: tangdu
 * Date: 13-9-7
 * Time: 下午9:19
 * To change this template use File | Settings | File Templates.
 */
public class AbstractController extends BaseController {
    protected static final Logger LOGGER= LoggerFactory.getLogger(AbstractController.class);
    private BodyWrapper bodyWrapper;

    public BodyWrapper getWrapper() {
        bodyWrapper = new BodyWrapper();
        return bodyWrapper;
    }

    public BodyWrapper success() {
        return getWrapper();
    }

    public BodyWrapper success(List<?> list) {
        getWrapper();
        bodyWrapper.setRows(list);
        bodyWrapper.setTotal(list!=null ? Long.valueOf(list.size()):0);
        return bodyWrapper;
    }

    public BodyWrapper success(Object obj) {
        getWrapper();
        bodyWrapper.setRows(obj);
        return bodyWrapper;
    }

    public BodyWrapper success(PageModel page) {
        getWrapper();
        bodyWrapper.setRows(page.getList());
        bodyWrapper.setTotal(page.getTotalRows());
        return bodyWrapper;
    }

    public BodyWrapper failure() {
        getWrapper();
        bodyWrapper.setSuccess(false);
        bodyWrapper.setRows("操作失败");
        return bodyWrapper;
    }

    public BodyWrapper failure(String errorMsg) {
        getWrapper();
        bodyWrapper.setSuccess(false);
        bodyWrapper.setRows(errorMsg);
        return bodyWrapper;
    }

    public BodyWrapper failure(Exception error) {
        getWrapper();
        bodyWrapper.setSuccess(false);
        bodyWrapper.setRows(error.getMessage());
        return bodyWrapper;
    }

    public void renderSuccess(String info,HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        if(info!=null){
            try {
                response.getOutputStream().print("{success:true,data:'"+info+"'}");
                response.getOutputStream().close();
            } catch (IOException e) {
                LOGGER.error("Exception {}",e);
            }
        }
    }

    public void renderSuccess(HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        try {
            response.getOutputStream().print("{success:true}");
            response.getOutputStream().close();
        } catch (IOException e) {
            LOGGER.error("Exception {}",e);
        }
    }

    public void renderSuccess(Object info,HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        if(info!=null){
            Map<String,Object> data=getMap();
            data.put("success",true);
            data.put("data", info);
            try {
                response.getOutputStream().print(JSON.toJSONString(data));
                response.getOutputStream().close();
            } catch (IOException e) {
                LOGGER.error("Exception {}", e);
            }
        }
    }

    @SuppressWarnings("rawtypes")
	public void render(Map info,HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        if(info!=null){
            try {
                response.getOutputStream().print(JSON.toJSONString(info));
                response.getOutputStream().close();
            } catch (IOException e) {
                LOGGER.error("Exception {}", e);
            }
        }
    }

    public void renderFailure(String info,HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        if(info!=null){
            try {
                response.getOutputStream().print("{success:false,data:'"+info+"'}");
                response.getOutputStream().close();
            } catch (IOException e) {
                LOGGER.error("Exception {}", e);
            }
        }
    }

    public void renderFailure(Object info,HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        if(info!=null){
            Map<String,Object> data=getMap();
            data.put("success",false);
            data.put("data", info);
            try {
                response.getOutputStream().print(JSON.toJSONString(data));
                response.getOutputStream().close();
            } catch (IOException e) {
                LOGGER.error("Exception {}",e);
            }
        }
    }
}
