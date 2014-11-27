package ${packageName}.service;

import com.ipe.common.service.BaseService;
import com.ipe.common.dao.BaseDao;
import ${packageName}.entity.${entityName?cap_first};
import ${packageName}.dao.${entityName?cap_first}Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by tangdu on ${.now}.
 */
@Service
@Transactional
public class ${entityName?cap_first}Service extends BaseService<${entityName},String> {
    @Autowired
    private ${entityName?cap_first}Dao ${entityName?uncap_first}Dao;

    @Override
    public BaseDao<${entityName?cap_first}, String> getBaseDao() {
        return ${entityName?uncap_first}Dao;
    }
}
