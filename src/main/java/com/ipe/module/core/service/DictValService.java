package com.ipe.module.core.service;

import com.ipe.common.dao.BaseDao;
import com.ipe.common.service.BaseService;
import com.ipe.module.core.dao.DictValDao;
import com.ipe.module.core.entity.DictVal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class DictValService extends BaseService<DictVal,String> {
    @Autowired
    private DictValDao dictValDao;

    @Override
    public BaseDao<DictVal, String> getBaseDao() {
        return dictValDao;
    }
    
    @Transactional
    public void updateSno(String [] ids){
    	if(ids!=null){
    		for(int i=0;i<ids.length;i++){
    			DictVal d=dictValDao.get(ids[i]);
    			d.setSno(i+1);
    			dictValDao.update(d);
    		}
    	}
    }
}
