package com.ipe.generator.ext;

import java.io.File;
import java.io.StringWriter;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.ipe.generator.ext.pojo.ExtParams;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * Created by tangdu on 14-2-20.
 */
public class ExtUtil {

	public static String generate(Map<String, Object> objectMap,
			ExtParams params) throws Exception {
		Configuration cfg = new Configuration();
		// cfg.setDirectoryForTemplateLoading(new File(PATH));
		cfg.setClassForTemplateLoading(ExtUtil.class, "ftl");
		StringWriter result = new StringWriter();
		Template template = cfg.getTemplate(params.getType() + ".ftl");
		if (template != null) {
			template.process(objectMap, result);
			if (params.getCreateFile() != null && params.getCreateFile()
					&& StringUtils.isNotBlank(params.getPath())) {
				FileUtils.write(
						new File(params.getPath() + "\\"
								+ objectMap.get("entityName") + ".js"),
						result.toString(), "UTF-8");
			}
		}
		return result.toString();
	}
}
