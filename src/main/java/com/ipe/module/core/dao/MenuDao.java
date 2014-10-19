package com.ipe.module.core.dao;

import com.ipe.common.dao.BaseDao;
import com.ipe.module.core.entity.Menu;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tangdu
 * Date: 13-10-5
 * Time: 下午3:23
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class MenuDao extends BaseDao<Menu, String> {
    public List<Menu> getParentMenu(String pid){
        return list("from Menu m where m.parent.id=?",pid);
    }

    public Integer getMaxSno(){
        return (Integer)findOne("select max(sno)+1 from Menu");
    }

    public void updateParent(String pid, int sno,String id){
         execute("update Menu set parent.id=?,sno=? where id=?",pid,sno,id);
    }
}
