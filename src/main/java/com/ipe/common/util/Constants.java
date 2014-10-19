package com.ipe.common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * SmartClient 常量
 * @author tangdu
 *
 * @time 2013-5-11 下午9:35:57
 */
public class Constants {

	public final static  String SCHEMA="ipe_db";
	/**
	 * 不需要记录日志的请求
	 */
	public static final Map<String,String> FILTERS_URL;
	static {
		FILTERS_URL=new HashMap<String, String>();
		FILTERS_URL.put("PNG", "png");
		FILTERS_URL.put("JPG", "jpg");
		FILTERS_URL.put("JPEG", "jpeg");
		FILTERS_URL.put("ICO", "ico");
		FILTERS_URL.put("JS", "js");
		FILTERS_URL.put("GIF", "gif");
		FILTERS_URL.put("HTML", "html");
		FILTERS_URL.put("CSS", "css");
	}
	public static final String SESSION_FLAG = "user";//session中保存的信息
	
	public static final int DEFAULT_PAGE_SIZE=10;//默认分页数
	
	public static String BASE_PATH=null;//项目根目录 
}
