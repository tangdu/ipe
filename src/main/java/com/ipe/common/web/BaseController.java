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

    public void downFile(HttpServletResponse response){
        try {
            response.setContentType("application/x-download");
            response.setHeader("Pragma", "public");
            response.setHeader("Cache-Control","must-revalidate, post-check=0, pre-check=0");
            response.addHeader("Content-disposition", "attachment;filename="+ new String("下载异常.txt".getBytes("GBK"), "ISO-8859-1"));
            response.getWriter().print("下载文件找不到，请联系系统管理员");
            response.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
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
        response.setContentType("application/x-download");
        response.setHeader("Pragma", "public");
        response.setHeader("Cache-Control","must-revalidate, post-check=0, pre-check=0");
        InputStream  in = new FileInputStream(file);
        fileName = new String(fileName.getBytes("GBK"), "ISO-8859-1");
        response.addHeader("Content-disposition", "attachment;filename=" +SIMPLEDATEFORMAT.format(new Date())+fileName);
        downFile(in,response);
    }
}
