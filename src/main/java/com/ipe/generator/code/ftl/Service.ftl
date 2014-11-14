package ${servicePackName};

import com.ipe.common.dao.CustomerRepository;
import com.ipe.common.service.BaseService;
import ${entityPackName}.${entityName};
import ${daoPackName}.${entityName}Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;



@Service
@Transactional
public class ${entityName}Service extends BaseService<${entityName},String> {
    @Autowired
    private ${entityName}Dao ${entityName?uncap_first}Dao;

    @Override
    public BaseDao<${entityName}, String> getBaseDao() {
        return ${entityName?uncap_first}Dao;
    }
}
