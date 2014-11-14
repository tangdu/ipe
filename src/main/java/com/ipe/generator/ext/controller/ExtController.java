package com.ipe.generator.ext.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.ipe.common.util.CollectionSort;
import com.ipe.generator.ext.ExtUtil;
import com.ipe.generator.ext.pojo.ExtColumn;
import com.ipe.generator.ext.pojo.ExtTable;

/**
 * Created by tangdu on 14-2-20.
 */
@Controller
@RequestMapping("ext")
public class ExtController {

    /**
     * ExtJS4代码生成器
     * type=1 表单
     *  ctype=10 单列表单
     *  ctype=11 多列表单
     * type=2 表格
     *
     * @return
     */
    @RequestMapping(value="generate_execute")
    @ResponseBody
    public String extgenerateExecute(@RequestParam String type,@RequestParam String  dt,@RequestParam String entityName,@RequestParam String mpackage,
        Boolean createFile,String path){
        List<ExtColumn> columns=JSON.parseArray(dt,ExtColumn.class);
        CollectionSort.sortList(columns,"sno",true);//排序
        Map<String,Object> objectMap= Maps.newHashMap();
        objectMap.put("entityName",entityName);
        objectMap.put("mpackage",mpackage);
        objectMap.put("formItem",columns);
        return ExtUtil.generate(objectMap,type,createFile,path);
    }

    @Autowired
    private SessionFactory sessionFactory;
    /**
     * ExtJS4代码生成器
     * @param mp
     * @return
     */
    @RequestMapping(value="generate_view")
    public String extGenerate(ModelMap mp){
       //step1:得到系统所有实体对象
    	Map<String,ClassMetadata> classMetadatas=sessionFactory.getAllClassMetadata();
        List<ExtTable> extTables= new ArrayList<ExtTable>();
        
        for(Map.Entry<String, ClassMetadata> en:classMetadatas.entrySet()){
        	ExtTable extTable=new ExtTable();
        	String entity=en.getValue().getEntityName();
        	extTable.setPackageName(entity.substring(0,entity.lastIndexOf(".")));
            extTable.setEntityName(entity.substring(entity.lastIndexOf(".")+1));
            org.hibernate.type.Type [] types=en.getValue().getPropertyTypes();
            List<ExtColumn> columns=new ArrayList<ExtColumn>();
            
            for(org.hibernate.type.Type ty:types){
            	 ExtColumn extColumn = new ExtColumn();
                 extColumn.setName(ty.getName());
                 extColumn.setMaxLength(0);
                 extColumn.setJavaType(ty.getName());
                 if("String".equals(extColumn.getJavaType())){
                     extColumn.setXtype("textfield");
                 }else if("Date".equals(extColumn.getJavaType()) || "Timestamp".equals(extColumn.getJavaType())){
                     extColumn.setXtype("datefield");
                 }else if("Integer".equals(extColumn.getJavaType()) || "Long".equals(extColumn.getJavaType())){
                     extColumn.setXtype("numberfield");
                 }else if("Double".equals(extColumn.getJavaType()) || "BigDecimal".equals(extColumn.getJavaType())){
                     extColumn.setXtype("numberfield");
                 }else if(extColumn.getJavaType().equals("java.util.Set") || extColumn.getJavaType().equals("java.util.List")
                         || extColumn.getJavaType().equals("java.util.Collection")){
                     continue;
                 }
                 columns.add(extColumn);
            }
            extTable.setColumns(columns);
            extTables.add(extTable);
        }
        //step2:
        mp.put("entities",extTables);
        mp.put("entitiesStr",JSON.toJSONString(extTables));
        return "tools/extgenerate";
    }

    /**
     * ExtJS 代码预览
     * @return
     */
    @RequestMapping(value="code_view")
    public String extCodeView(){
        return "tools/extgenerateview";
    }

}
