package com.ipe.module.ext.web.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.ipe.common.util.CollectionSort;
import com.ipe.module.ext.pojo.ExtColumn;
import com.ipe.module.ext.web.ExtUtil;

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

    /**
     * ExtJS4代码生成器
     * @param mp
     * @return
     */
    @RequestMapping(value="generate_view")
    public String extGenerate(ModelMap mp){
       /* //step1:得到系统所有实体对象
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean= SpringContextHolder.getBean(LocalContainerEntityManagerFactoryBean.class);
        EntityManagerFactory entityManagerFactory=localContainerEntityManagerFactoryBean.getNativeEntityManagerFactory();
        Metamodel metamodel=entityManagerFactory.getMetamodel();
        Iterator<EntityType<?>> entityTypes= metamodel.getEntities().iterator();
        List<ExtTable> extTables= Lists.newArrayList();

        while(entityTypes.hasNext()){
            EntityTypeImpl entityType=(EntityTypeImpl)entityTypes.next();
            Iterator<Attribute> attributes=entityType.getDeclaredAttributes().iterator();
            String entity=entityType.getJavaType().getName();
            ExtTable extTable=new ExtTable();
            extTable.setPackageName(entity.substring(0,entity.lastIndexOf(".")));
            extTable.setEntityName(entity.substring(entity.lastIndexOf(".")+1));
            List<ExtColumn> columns=Lists.newArrayList();
            while(attributes.hasNext()){
                Attribute attribute=attributes.next();
                ExtColumn extColumn = new ExtColumn();
                extColumn.setName(attribute.getName());
                extColumn.setMaxLength(0);
                extColumn.setJavaType(attribute.getJavaType().getSimpleName());
                if("String".equals(extColumn.getJavaType())){
                    extColumn.setXtype("textfield");
                }else if("Date".equals(extColumn.getJavaType()) || "Timestamp".equals(extColumn.getJavaType())){
                    extColumn.setXtype("datefield");
                }else if("Integer".equals(extColumn.getJavaType()) || "Long".equals(extColumn.getJavaType())){
                    extColumn.setXtype("numberfield");
                }else if("Double".equals(extColumn.getJavaType()) || "BigDecimal".equals(extColumn.getJavaType())){
                    extColumn.setXtype("numberfield");
                }else if("Set".equals(extColumn.getJavaType()) || "List".equals(extColumn.getJavaType())
                        || "Collection".equals(extColumn.getJavaType())){
                    continue;
                }
                columns.add(extColumn);
            }
            extTable.setColumns(columns);
            extTables.add(extTable);
        }

        //step2:
        mp.put("entities",extTables);
        mp.put("entitiesStr",JSON.toJSONString(extTables));*/
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
