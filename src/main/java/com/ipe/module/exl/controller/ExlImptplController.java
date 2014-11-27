package com.ipe.module.exl.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.ipe.common.dao.SpringJdbcDao;
import com.ipe.common.util.PageModel;
import com.ipe.module.core.web.controller.AbstractController;
import com.ipe.module.core.web.util.BodyWrapper;
import com.ipe.module.core.web.util.RestRequest;
import com.ipe.module.core.web.util.WebUtil;
import com.ipe.module.exl.ExcelParse;
import com.ipe.module.exl.TableColumn;
import com.ipe.module.exl.entity.ExlImptpl;
import com.ipe.module.exl.service.ExlImptplService;

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
            LOGGER.error("query error",e);
            return failure(e);
        }
    }

    @RequestMapping(value = {"/edit"}, method = RequestMethod.POST)
    public
    @ResponseBody
    BodyWrapper edit(ExlImptpl exlImptpl,String details) {
        try {
            exlImptpl.setCreatedDate(new Date());
            exlImptplService.edit(exlImptpl, details);
            return success(exlImptpl);
        } catch (Exception e) {
            LOGGER.error("edit error",e);
            return failure(e);
        }
    }

    @RequestMapping(value = {"/add"}, method = RequestMethod.POST)
    public
    @ResponseBody
    BodyWrapper add(ExlImptpl exlImptpl,String details) {
        try {
            exlImptpl.setCreatedDate(new Date());
            exlImptplService.save(exlImptpl,details);
            return success(exlImptpl);
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
            exlImptplService.delete(ids);
            return success();
        } catch (Exception e) {
            LOGGER.error("del error",e);
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
            LOGGER.error("edit error",e);
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
            LOGGER.error("impexcelData error",e);
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
    public void expexcelData(String id,HttpServletResponse response) {
    	ServletOutputStream outputStream=null;
        try {
        	ExlImptpl exlImptpl = exlImptplService.get(id);
        	outputStream=response.getOutputStream();
        	WebUtil.setDownHeader(response,exlImptpl.getExlName()+".xlsx");
        	exlImptplService.expData(id,outputStream );
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
    
    /**
     * 加载数据表结构
     * @param mappingTable
     * @param tableBelongUser
     * @return
     */
    @RequestMapping(value = {"/loadFieldByTableUser"})
    @ResponseBody
    public BodyWrapper loadFieldByTableUser(String mappingTable,String tableBelongUser) {
        try {
        	List<TableColumn> list=exlImptplService.getTableInfo(mappingTable, tableBelongUser);
        	return success(list);
        } catch (Exception e) {
            LOGGER.error("edit error",e);
            return failure(e);
        }
    }
}
