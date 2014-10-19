package com.ipe.module.core.dao;

import com.ipe.common.dao.BaseDao;
import com.ipe.module.core.entity.Resource;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: tangdu
 * Date: 13-9-7
 * Time: 下午9:54
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class ResourceDao extends BaseDao<Resource,String> {
    public Integer getMaxSno(){
        return (Integer)findOne("select max(sno)+1 from Resource");
    }
}
