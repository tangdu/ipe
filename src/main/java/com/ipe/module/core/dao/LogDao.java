package com.ipe.module.core.dao;

import com.ipe.common.dao.BaseDao;
import com.ipe.module.core.entity.Log;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: tangdu
 * Date: 13-9-14
 * Time: 下午10:58
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class LogDao extends BaseDao<Log,String> {

    public int updateLogoutTime(Date leaveTime, Date accessTime, String accessUserid){
        return execute("update Log u set u.leaveTime=? where u.accessTime=? and u.accessUserid=?",leaveTime,accessTime,accessUserid);
    }

   public Date findMaxAccessTime( String accessUserid){
    return (Date)findOne("select max(accessTime) from Log where accessUserid=?",accessUserid);
   }
}
