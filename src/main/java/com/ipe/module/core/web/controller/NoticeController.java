package com.ipe.module.core.web.controller;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.ipe.module.core.entity.Notice;
import com.ipe.module.core.service.NoticeService;
import com.ipe.module.core.web.security.SystemRealm;
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
@RequestMapping("/notice")
public class NoticeController extends AbstractController {

    private static final Logger LOG= LoggerFactory.getLogger(NoticeController.class);
    @Autowired
    private NoticeService noticeService;
    private static final String PATH="tools/";
    

    @RequestMapping(value = {"/list"})
    public
    @ResponseBody
    BodyWrapper list(Notice notice, RestRequest rest) {
        try {
            noticeService.where(rest.getPageModel());
            return success(rest.getPageModel());
        } catch (Exception e) {
            LOG.error("query error",e);
            return failure(e);
        }
    }
    
    @RequestMapping(value = {"/getNotice"})
    public ModelAndView getNotice(String id) {
       Notice notice=noticeService.get(id);
       Map<String,Object> mp=new HashMap<String,Object>();
       mp.put("data", notice);
       return new ModelAndView(PATH+"view_notice", mp);
    }


    @Value("#{app.notice_filepath}")
    private String noticeFilePath;

    @RequestMapping(value = {"/edit"}, method = RequestMethod.POST)
    public
    @ResponseBody
    BodyWrapper edit(Notice notice, MultipartHttpServletRequest multipartHttpServletRequest) {
        try {
            MultipartFile multipartFile=multipartHttpServletRequest.getFileMap().get("file");
            String appendixPath=null;
            if(multipartFile!=null && multipartFile.getSize()>0){
                notice.setAppendixName(multipartFile.getOriginalFilename());
                appendixPath=noticeFilePath+"/"+multipartFile.getOriginalFilename();
                FileUtils.writeByteArrayToFile(new File(appendixPath),multipartFile.getBytes());
            }
            SystemRealm.UserInfo user =(SystemRealm.UserInfo) SecurityUtils.getSubject().getPrincipal();
            notice.setUserId(user.getUserId());
            notice.setAppendixPath(appendixPath);
            notice.setUpdatedDate(new Date());
            noticeService.update(notice);
            return success(notice);
        } catch (Exception e) {
            LOG.error("edit error",e);
            return failure(e);
        }
    }

    @RequestMapping(value = {"/add"}, method = RequestMethod.POST)
    public
    @ResponseBody
    BodyWrapper add(Notice notice, MultipartHttpServletRequest multipartHttpServletRequest) {
        try {
            MultipartFile multipartFile=multipartHttpServletRequest.getFileMap().get("file");
            String appendixPath=null;
            if(multipartFile!=null && multipartFile.getSize()>0){
                notice.setAppendixName(multipartFile.getOriginalFilename());
                appendixPath=noticeFilePath+"/"+multipartFile.getOriginalFilename();
                FileUtils.writeByteArrayToFile(new File(appendixPath),multipartFile.getBytes());
            }
            SystemRealm.UserInfo user =(SystemRealm.UserInfo) SecurityUtils.getSubject().getPrincipal();
            notice.setUserId(user.getUserId());
            notice.setCreatedDate(new Date());
            notice.setAppendixPath(appendixPath);
            noticeService.save(notice);
            return success(notice);
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
            noticeService.delete(ids);
            return success();
        } catch (Exception e) {
            LOG.error("del error",e);
            return failure(e);
        }
    }
}
