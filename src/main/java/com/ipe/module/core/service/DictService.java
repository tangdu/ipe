package com.ipe.module.core.service;

import com.ipe.common.dao.BaseDao;
import com.ipe.common.service.BaseService;
import com.ipe.module.core.dao.DictDao;
import com.ipe.module.core.entity.Dict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@Transactional
public class DictService extends BaseService<Dict,String> {
    @Autowired
    private DictDao dictDao;

    @Override
    public BaseDao<Dict, String> getBaseDao() {
        return dictDao;
    }
}
