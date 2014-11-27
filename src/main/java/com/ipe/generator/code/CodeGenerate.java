package com.ipe.generator.code;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ipe.common.service.CommonUtil;
import com.ipe.generator.code.pojo.FreeCodeInfo;
import com.ipe.module.exl.TableColumn;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Repository
public class CodeGenerate {
	@Autowired
	private CommonUtil commonUtil;

	static Configuration CFG = new Configuration();
	static {
		CFG.setClassForTemplateLoading(CodeGenerate.class, "ftl");
	}

	public void create(FreeCodeInfo codeInfo) {
		if(codeInfo.iscEntity()){
			loadTemplate("Entity.ftl", codeInfo);
		}
		if(codeInfo.iscDao()){
			loadTemplate("Dao.ftl", codeInfo);
		}
		if(codeInfo.iscService()){
			loadTemplate("Service.ftl", codeInfo);
		}
		if(codeInfo.iscController()){
			loadTemplate("Controller.ftl", codeInfo);
		}
	}

	public void loadTable(FreeCodeInfo codeInfo) {
		List<TableColumn> columns = commonUtil.getTableInfo(
				codeInfo.getTableName(), codeInfo.getSchema());
		codeInfo.setColumns(columns);
		create(codeInfo);
	}

	public void loadTemplate(String tpl, FreeCodeInfo codeInfo) {
		StringWriter result = null;
		try {
			String entity=tpl.substring(0,tpl.lastIndexOf("."));
			Template template = CFG.getTemplate(tpl);
			result = new StringWriter();
			template.process(codeInfo, result);
			String file = codeInfo.getPath() + "/"
					+ codeInfo.getPackageName().replaceAll("\\.", "/") + "/"
					+ entity.toLowerCase()+"/"
					+ codeInfo.getEntityName() +(tpl.contains("Entity") ? "":entity)+ ".java";
			FileUtils.write(new File(file), result.toString(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (result != null) {
				try {
					result.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
