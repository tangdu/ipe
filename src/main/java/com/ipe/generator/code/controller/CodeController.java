package com.ipe.generator.code.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ipe.generator.code.CodeGenerate;
import com.ipe.generator.code.pojo.FreeCodeInfo;
import com.ipe.module.core.web.controller.AbstractController;
import com.ipe.module.core.web.util.BodyWrapper;

@Controller
@RequestMapping("code")
public class CodeController  extends AbstractController{

	@Autowired
	private CodeGenerate codeGenerate;
	
	@RequestMapping(value = {"/create"})
    public
    @ResponseBody
    BodyWrapper create(FreeCodeInfo codeInfo) {
        try {
        	codeGenerate.loadTable(codeInfo);
            return success();
        } catch (Exception e) {
            LOGGER.error("query error",e);
            return failure(e);
        }
    }
}
