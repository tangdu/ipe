package ${packageName}.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ipe.common.entity.IDEntity;

/**
 * Created by tangdu on ${.now}.
 */
@Entity
@Table(name = "${tableName}", schema = "",catalog="${schema}")
public class ${entityName?cap_first} extends IDEntity {
    private static final long serialVersionUID = 1L;
    <#list columns as en>
    //${en.fieldDesc}
    private String ${en.fieldName?replace("\\-","")?replace("_","")?uncap_first};
    </#list>
    <#list columns as en>
    
    @Column(name="${en.fieldName}")
    public String get${en.fieldName?replace("\\-","")?replace("_","")?cap_first}() {
        return ${en.fieldName?replace("\\-","")?replace("_","")?uncap_first};
    }

    public void set${en.fieldName?replace("-","")?replace("_","")?cap_first}(String ${en.fieldName?replace("\\-","")?replace("_","")?uncap_first}) {
        this.${en.fieldName?replace("\\-","")?replace("_","")?uncap_first} = ${en.fieldName?replace("\\-","")?replace("_","")?uncap_first};
    }
    </#list>
    
}
