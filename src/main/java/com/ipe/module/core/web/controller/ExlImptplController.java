package com.ipe.module.core.web.controller;

import com.ipe.common.dao.SpringJdbcDao;
import com.ipe.common.util.ExcelParse;
import com.ipe.common.util.PageModel;
import com.ipe.module.core.entity.ExlImptpl;
import com.ipe.module.core.service.ExlImptplService;
import com.ipe.module.core.web.util.BodyWrapper;
import com.ipe.module.core.web.util.RestRequest;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
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

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Role: tangdu
 * Date: 13-9-7
 * Time: 下午10:27
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/exlImptpl")
public class ExlImptplController extends AbstractController {

    private static final Logger LOG= LoggerFactory.getLogger(ExlImptplController.class);
    @Autowired
    private ExlImptplService exlImptplService;
    @Autowired
    private SpringJdbcDao springJdbcDao;

    @RequestMapping(value = {"/list"})
    public
    @ResponseBody
    BodyWrapper list(ExlImptpl exlImptpl, RestRequest rest) {
        try {
            PageModel page=rest.getPageModel();
            List<ExlImptpl> lists=exlImptplService.where(page);
            for(ExlImptpl imptpl:lists){
                if(StringUtils.isNotBlank(imptpl.getMappingTable())){
                    String sql="select count(*) from "+imptpl.getMappingTable();
                    try {
                        long count=springJdbcDao.queryLong(sql);
                        imptpl.setTableCot(String.valueOf(count));
                    } catch (Exception e) {
                        imptpl.setTableCot("N/A");
                    }
                }else{
                    imptpl.setTableCot("UN");
                }
            }
            return success(page);
        } catch (Exception e) {
            LOG.error("query error",e);
            return failure(e);
        }
    }

    @RequestMapping(value = {"/edit"}, method = RequestMethod.POST)
    public
    @ResponseBody
    BodyWrapper edit(ExlImptpl exlImptpl,String details) {
        try {
            exlImptpl.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            exlImptplService.edit(exlImptpl, details);
            return success(exlImptpl);
        } catch (Exception e) {
            LOG.error("edit error",e);
            return failure(e);
        }
    }

    @RequestMapping(value = {"/add"}, method = RequestMethod.POST)
    public
    @ResponseBody
    BodyWrapper add(ExlImptpl exlImptpl,String details) {
        try {
            exlImptpl.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            exlImptplService.save(exlImptpl,details);
            return success(exlImptpl);
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
            exlImptplService.delete(ids);
            return success();
        } catch (Exception e) {
            LOG.error("del error",e);
            return failure(e);
        }
    }

    @Value("#{app.temp_filepath}")
    private String tempFilePath;

    /**
     * 上传Excel
     * @param multipartHttpServletRequest
     * @return
     */
    @RequestMapping(value = {"/impexcel"})
    @ResponseBody
    public BodyWrapper impexcel(Integer sheetIndex,MultipartHttpServletRequest multipartHttpServletRequest) {
        try {
            MultipartFile multipartFile=multipartHttpServletRequest.getFileMap().get("file");
            String appendixPath=null;
            if(multipartFile!=null && multipartFile.getSize()>0){
                appendixPath=tempFilePath+"/"+multipartFile.getOriginalFilename();
                FileUtils.writeByteArrayToFile(new File(appendixPath), multipartFile.getBytes());
                ExcelParse excelParse=new ExcelParse(appendixPath);
                Map<String,Object> data=new HashMap<>();
                ArrayList<Object[]> arrayList=excelParse.read(sheetIndex);
                data.put("data",arrayList);//TODO max显示2000行
                if(!arrayList.isEmpty()){
                    data.put("collength",arrayList.get(0).length);
                }else{
                    data.put("collength",0);
                }
                data.put("sheets",excelParse.sheet());
                return success(data);
            }
            return failure();
        } catch (Exception e) {
            LOG.error("edit error",e);
            return failure(e);
        }
    }

    /**
     * 导入Excel数据
     * @param id
     * @param multipartHttpServletRequest
     * @return
     */
    @RequestMapping(value = {"/impexcelData"})
    @ResponseBody
    public BodyWrapper impexcelData(String id,MultipartHttpServletRequest multipartHttpServletRequest) {
        try {
            MultipartFile multipartFile=multipartHttpServletRequest.getFileMap().get("file");
            String appendixPath=null;
            if(multipartFile!=null && multipartFile.getSize()>0){
                appendixPath=tempFilePath+"/"+multipartFile.getOriginalFilename();
                FileUtils.writeByteArrayToFile(new File(appendixPath), multipartFile.getBytes());
                String result=exlImptplService.impData(appendixPath,id);
                return success(result);
            }
            return failure();
        } catch (Exception e) {
            LOG.error("edit error",e);
            return failure(e);
        }
    }
}
