package ${packageName}.controller;

import ${packageName}.entity.${entityName?cap_first};
import ${packageName}.service.${entityName?cap_first}Service;
import com.ipe.module.core.web.controller.AbstractController;
import com.ipe.module.core.web.util.BodyWrapper;
import com.ipe.module.core.web.util.RestRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Created by tangdu on ${.now}.
 */
@Controller
@RequestMapping("/${entityName?uncap_first}")
public class ${entityName?cap_first}Controller extends AbstractController {
    @Autowired
    private ${entityName?cap_first}Service ${entityName?uncap_first}Service;

    @RequestMapping(value = {"/list"})
    public
    @ResponseBody
    BodyWrapper list(${entityName} ${entityName?uncap_first}, RestRequest rest) {
        try {
         	${entityName?uncap_first}Service.where(rest.getPageModel());
            return success(rest.getPageModel());
        } catch (Exception e) {
            LOGGER.error("query error",e);
            return failure(e);
        }
    }

    @RequestMapping(value = {"/edit"}, method = RequestMethod.POST)
    public
    @ResponseBody
    BodyWrapper edit(${entityName} ${entityName?uncap_first}, RestRequest rest) {
        try {
            ${entityName?uncap_first}Service.update(${entityName?uncap_first});
            return success(${entityName?uncap_first});
        } catch (Exception e) {
            LOGGER.error("edit error",e);
            return failure(e);
        }
    }

    @RequestMapping(value = {"/add"}, method = RequestMethod.POST)
    public
    @ResponseBody
    BodyWrapper add(${entityName} ${entityName?uncap_first}, RestRequest rest) {
        try {
            ${entityName?uncap_first}Service.save(${entityName?uncap_first});
            return success(${entityName?uncap_first});
        } catch (Exception e) {
            LOGGER.error("add error",e);
            return failure(e);
        }
    }

    @RequestMapping(value = {"/del"})
    public
    @ResponseBody
    BodyWrapper del(String[] ids, RestRequest rest) {
        try {
            ${entityName?uncap_first}Service.delete(ids);
            return success();
        } catch (Exception e) {
            LOGGER.error("del error",e);
            return failure(e);
        }
    }
}
