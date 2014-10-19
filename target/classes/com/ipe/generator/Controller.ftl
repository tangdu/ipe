package ${contrlPackName};

import ${entityPackName}.${entityName};
import ${servicePackName}.${entityName}Service;
import com.ipe.module.core.web.controller.AbstractController;
import com.ipe.module.core.web.util.BodyWrapper;
import com.ipe.module.core.web.util.RestRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * Role: tangdu
 * Date: 13-9-7
 * Time: 下午10:27
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/${entityName?uncap_first}")
public class ${entityName}Controller extends AbstractController {

    private static final Logger LOG= LoggerFactory.getLogger(${entityName}Controller.class);
    @Autowired
    private ${entityName}Service ${entityName?uncap_first}Service;

    @RequestMapping(value = {"/list"})
    public
    @ResponseBody
    BodyWrapper list(${entityName} ${entityName?uncap_first}, RestRequest rest) {
        try {
            int startRow = rest.getStart();
            int endRow = rest.getLimit();
            Page<${entityName}> page = ${entityName?uncap_first}Service.findAll(null, new PageRequest(startRow, endRow));
            return success(page);
        } catch (Exception e) {
            LOG.error("query error",e);
            return failure(e);
        }
    }

    @RequestMapping(value = {"/edit"}, method = RequestMethod.POST)
    public
    @ResponseBody
    BodyWrapper edit(${entityName} ${entityName?uncap_first}, RestRequest rest) {
        try {
            ${entityName?uncap_first}Service.save(${entityName?uncap_first});
            return success(${entityName?uncap_first});
        } catch (Exception e) {
            LOG.error("edit error",e);
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
            LOG.error("add error",e);
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
            LOG.error("del error",e);
            return failure(e);
        }
    }
}
