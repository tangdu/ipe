package com.ipe.module.core.web.util;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: tangdu
 * Date: 13-11-17
 * Time: 下午5:37
 * To change this template use File | Settings | File Templates.
 */
public class WebUtil {

	protected static final SimpleDateFormat SIMPLEDATEFORMAT=new SimpleDateFormat("yyyyMMdd");
	public static void setDownHeader(HttpServletResponse response,final String filename){
		 response.setContentType("application/x-download");
         response.setHeader("Pragma", "public");
         response.setHeader("Cache-Control","must-revalidate, post-check=0, pre-check=0");
         try {
        	 if(StringUtils.isNotBlank(filename)){
        		 response.addHeader("Content-disposition", "attachment;filename="+
        				 SIMPLEDATEFORMAT.format(new Date())+"_"+new String(filename.getBytes("GBK"), "ISO-8859-1"));
        	 }else{
        		 response.addHeader("Content-disposition", "attachment;filename="+ 
        				 SIMPLEDATEFORMAT.format(new Date())+"_"+new String("下载文件".getBytes("GBK"), "ISO-8859-1"));
        	 }
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	public static void setExcelHeader(HttpServletResponse response){
		response.setContentType("application/x-download");
	}
	
    public static String getIpAddr(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
