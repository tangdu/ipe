package com.ipe.common.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import com.ipe.common.web.support.DateEditor;
import com.ipe.module.core.web.util.WebUtil;


/**
 * 基本controller
 * @author tangdu
 *
 * @time 2013-5-12 上午8:08:08
 */
public abstract class BaseController {

    protected static final SimpleDateFormat SIMPLEDATEFORMAT=new SimpleDateFormat("yyyyMMdd");
    protected static final SimpleDateFormat FORMATER=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    protected static final SimpleDateFormat FORMATERYMD=new SimpleDateFormat("yyyy-MM-dd");
	protected Map<String, Object> getMap() {
		return new HashMap<String, Object>();
	}

	//定义解析
	@InitBinder
	public void initBinder(WebDataBinder binder) {
        // String类型转换，防止XSS攻击
        /*binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
            }
        });*/
		binder.registerCustomEditor(Date.class, new DateEditor());
		binder.registerCustomEditor(Integer.class, new CustomNumberEditor(
				Integer.class, true));
		binder.registerCustomEditor(Double.class, new CustomNumberEditor(
                Number.class, true));
		binder.registerCustomEditor(BigDecimal.class, new CustomNumberEditor(
				BigDecimal.class, true));
		binder.registerCustomEditor(MultipartFile.class,
				new ByteArrayMultipartFileEditor());
	}


    public void downFile(File file,HttpServletResponse response)throws Exception{
        downFile(file,file.getName(),response);
    }

    public void downFile(String filepath,HttpServletResponse response)throws Exception{
        File file=new File(filepath);
        downFile(file,file.getName(),response);
    }

    public void downFileError(HttpServletResponse response){
    	OutputStream out=null;
        try {
        	out=response.getOutputStream();
            WebUtil.setDownHeader(response,"下载异常.txt");
            out.write("下载文件找不到，请联系系统管理员".getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
        	if(out!=null){
        		try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }
    }

    /**
     * 文件下载
     * @param in
     * @param response
     * @throws Exception
     */
    public void downFile(InputStream in,HttpServletResponse response)throws Exception{
        OutputStream out=response.getOutputStream();
        try {
            int len = in.available();
            byte[] b = new byte[len];
            in.read(b);
            out.write(b);
            out.flush();
        } catch (Exception e) {
            throw new Exception("下载失败!");
        } finally {
            if(in!=null){
                in.close();
            }
            if(out!=null){
                out.close();
            }
        }
    }
    /**
     * 文件下载方法
     * @param file
     * @param fileName
     * @param response
     * @throws Exception
     */
    public void downFile(File file,String fileName,HttpServletResponse response) throws Exception{
        InputStream  in = new FileInputStream(file);
        WebUtil.setDownHeader(response,fileName);
        downFile(in,response);
    }
}
