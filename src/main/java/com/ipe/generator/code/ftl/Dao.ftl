package ${packageName}.dao;

import org.springframework.stereotype.Repository;

import com.ipe.common.dao.BaseDao;
import ${packageName}.entity.${entityName?cap_first};

/**
 * Created by tangdu on ${.now}.
 */
@Repository
public class ${entityName?cap_first}Dao extends BaseDao<${entityName},String> {
}
