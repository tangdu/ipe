package ${daoPackName};

import com.ipe.common.dao.CustomerRepository;
import ${entityPackName}.${entityName};
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
* Created with IntelliJ IDEA.
* User: tangdu
* Date: 13-9-7
* Time: 下午9:56
* To change this template use File | Settings | File Templates.
*/
@Repository
public interface ${entityName}Repository extends CustomerRepository<${entityName},String> {
}
