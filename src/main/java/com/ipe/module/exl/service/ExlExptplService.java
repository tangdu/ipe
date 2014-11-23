package com.ipe.module.exl.service;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ipe.common.dao.BaseDao;
import com.ipe.common.service.BaseService;
import com.ipe.module.exl.ExcelCreate;
import com.ipe.module.exl.dao.ExlExptplDao;
import com.ipe.module.exl.entity.ExlExptpl;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
@Transactional
public class ExlExptplService extends BaseService<ExlExptpl, String>{
	static Configuration CONFIGURATION=null;
	final static String  DEFALUT_CODE="CDOE";
	static{
		CONFIGURATION=new Configuration();
	}
	
	@Autowired
	private ExlExptplDao exlExptplDao;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public BaseDao<ExlExptpl, String> getBaseDao() {
		return exlExptplDao;
	}

	public List<Map<String,Object>>  loadColumns(String sql,Map<String,Object> args) throws IOException, TemplateException{
		//TODO 加载Freemarker模板
		StringTemplateLoader loader=new StringTemplateLoader();
		CONFIGURATION.setTemplateLoader(loader);
		loader.putTemplate(DEFALUT_CODE, sql);
		Template temp = CONFIGURATION.getTemplate(DEFALUT_CODE,"UTF-8");
		Writer out = new StringWriter(2048);
		temp.process(args, out);
		out.close();
		out.flush();
		sql=out.toString();
		return jdbcTemplate.queryForList(sql);
	}
	
	public void exportData(String code,Map<String,Object> args,OutputStream outputStream) throws Exception{
		ExlExptpl exlExptpl=(ExlExptpl) exlExptplDao.findOne("from ExlExptpl where code=?", code);
		String sql=exlExptpl.getSql();
		try {
			List<Map<String,Object>> data=loadColumns(sql,args);
			String titles=exlExptpl.getTitles();
			List<String> titleList=new ArrayList<String>();
			if(StringUtils.isNotBlank(titles) && !"[]".equals(titles)){
				JSONArray array=JSONArray.parseArray(titles);
				for(int i=0;i<array.size();i++){
					JSONObject j=array.getJSONObject(i);
					titleList.add(j.getString("name"));
				}
			}
			ExcelCreate.getInstance().createDefault2(data, titleList, outputStream);
		} catch (Exception e) {
			throw e;
		}
	}
}
