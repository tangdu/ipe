package com.ipe.generator.ext.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.ipe.common.util.CollectionSort;
import com.ipe.generator.ext.ExtUtil;
import com.ipe.generator.ext.pojo.ExtColumn;
import com.ipe.generator.ext.pojo.ExtParams;
import com.ipe.generator.ext.pojo.ExtTable;
import com.ipe.module.core.web.controller.AbstractController;
import com.ipe.module.core.web.util.BodyWrapper;

/**
 * Created by tangdu on 14-2-20.
 */
@Controller
@RequestMapping("ext")
public class ExtController extends AbstractController{

	/**
	 * ExtJS4代码生成器 type=1 表单 ctype=10 单列表单 ctype=11 多列表单 type=2 表格
	 *
	 * @return
	 */
	@RequestMapping(value = "generate_execute")
	@ResponseBody
	public Map<String,Object> extgenerateExecute(ExtParams params) {
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			List<ExtColumn> columns = JSON.parseArray(params.getDt(), ExtColumn.class);
			CollectionSort.sortList(columns, "sno", true);// 排序
			Map<String, Object> objectMap = Maps.newHashMap();
			objectMap.put("entityName", params.getEntityName());
			objectMap.put("mpackage", params.getMpackage());
			objectMap.put("formItem", columns);
			map.put("context",ExtUtil.generate(objectMap, params));
			map.put("success", true);
			map.put("className", params.getMpackage() + "." + params.getEntityName());
		} catch (Exception e) {
			LOGGER.error("extgenerateExecute ",e);
			map.put("success", false);
		}
		return map;
	}

	@Autowired
	private SessionFactory sessionFactory;

	/**
	 * ExtJS4代码生成器
	 * 
	 * @param mp
	 * @return
	 */
	@RequestMapping(value = "getEntittys")
	public @ResponseBody BodyWrapper getEntittys(ModelMap mp) {
		// step1:得到系统所有实体对象
		Map<String, ClassMetadata> classMetadatas = sessionFactory
				.getAllClassMetadata();
		List<ExtTable> extTables = new ArrayList<ExtTable>();

		for (Map.Entry<String, ClassMetadata> en : classMetadatas.entrySet()) {
			ExtTable extTable = new ExtTable();
			String entity = en.getValue().getEntityName();
			extTable.setPackageName(entity.substring(0, entity.lastIndexOf(".")));
			extTable.setEntityName(entity.substring(entity.lastIndexOf(".") + 1));
			org.hibernate.type.Type[] types = en.getValue().getPropertyTypes();
			String names[] = en.getValue().getPropertyNames();
			List<ExtColumn> columns = new ArrayList<ExtColumn>();

			ExtColumn extColumn = new ExtColumn();
			extColumn.setAllowBlank(false);
			extColumn.setFieldLabel("");
			extColumn.setJavaType(en.getValue().getIdentifierType().getName());
			extColumn.setSno(-1);
			extColumn.setName(en.getValue().getIdentifierPropertyName());
			columns.add(extColumn);
			
			for (int i = 0; i < types.length; i++) {
				org.hibernate.type.Type ty = types[i];
				extColumn = new ExtColumn();
				extColumn.setName(names[i]);
				extColumn.setMaxLength(0);
				extColumn.setJavaType(ty.getName());
				//extColumn.setXtype("hidden");
				
				if ("String".equalsIgnoreCase(extColumn.getJavaType())) {
					extColumn.setXtype("textfield");
				} else if ("Date".equalsIgnoreCase(extColumn.getJavaType())
						|| "Timestamp".equalsIgnoreCase(extColumn.getJavaType())) {
					extColumn.setXtype("datefield");
				} else if ("Integer".equalsIgnoreCase(extColumn.getJavaType())
						|| "Long".equalsIgnoreCase(extColumn.getJavaType())) {
					extColumn.setXtype("numberfield");
				} else if ("Double".equalsIgnoreCase(extColumn.getJavaType())
						|| "BigDecimal".equalsIgnoreCase(extColumn.getJavaType())) {
					extColumn.setXtype("numberfield");
				} else if (ty.isCollectionType()) {
					continue;
				}
				columns.add(extColumn);
			}
			extTable.setColumns(columns);
			extTables.add(extTable);
		}
		return success(extTables);
	}

	/**
	 * ExtJS 代码预览
	 * 
	 * @return
	 */
	@RequestMapping(value = "code_view")
	public String extCodeView(String htmlId,String className,ModelMap map) {
		map.put("className", className);
		map.put("htmlId", htmlId);
		return "tools/extgenerateview";
	}

}
