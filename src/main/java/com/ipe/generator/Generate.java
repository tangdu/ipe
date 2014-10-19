package com.ipe.generator;

import com.google.common.collect.Maps;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.StringWriter;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: tangdu
 * Date: 13-7-28
 * Time: 上午11:49
 * To change this template use File | Settings | File Templates.
 */
public class Generate {
    static final String dao_path="F:\\workspace\\jaeespace\\smh2\\src\\main\\java\\com\\ipe\\module\\core\\dao";
    static final String service_path="F:\\workspace\\jaeespace\\smh2\\src\\main\\java\\com\\ipe\\module\\core\\service";
    static final String controller_path="F:\\workspace\\jaeespace\\smh2\\src\\main\\java\\com\\ipe\\module\\core\\web\\controller";

    public static void main(String args[]){
        Configuration cfg = new Configuration();
        try {
            cfg.setDirectoryForTemplateLoading(new File("F:\\workspace\\jaeespace\\smh2\\src\\main\\java\\com\\ipe\\generator"));
            Map<String, String> model = Maps.newHashMap();
            String[]entitys=new String[]{"ExlImptpl","ExlImptplDetailes"};

            StringWriter result =null;
            Template template =null;
            for(String entity:entitys){
                model.put("entityName",entity);
                model.put("entityPackName","com.ipe.module.core.entity");
                model.put("daoPackName","com.ipe.module.core.dao");
                model.put("servicePackName","com.ipe.module.core.service");
                model.put("contrlPackName","com.ipe.module.core.web.controller");

                //dao template
                result = new StringWriter();
                template = cfg.getTemplate("Repository.ftl");
                template.process(model, result);
                FileUtils.write(new File(dao_path+"\\"+entity+"Repository.java"),result.toString(),"UTF-8");

                //service template
                result = new StringWriter();
                template = cfg.getTemplate("Service.ftl");
                template.process(model, result);
                FileUtils.write(new File(service_path+"\\"+entity+"Service.java"),result.toString(),"UTF-8");

                //constroller templdate
                result = new StringWriter();
                template = cfg.getTemplate("Controller.ftl");
                template.process(model, result);
                FileUtils.write(new File(controller_path+"\\"+entity+"Controller.java"),result.toString(),"UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
