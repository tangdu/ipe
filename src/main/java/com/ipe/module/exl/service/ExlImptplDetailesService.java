package com.ipe.module.exl.service;

import com.ipe.common.dao.BaseDao;
import com.ipe.common.service.BaseService;
import com.ipe.module.core.dao.ExlImptplDetailesDao;
import com.ipe.module.exl.entity.ExlImptplDetailes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class ExlImptplDetailesService extends BaseService<ExlImptplDetailes,String> {
    @Autowired
    private ExlImptplDetailesDao exlImptplDetailesDao;

    @Override
    public BaseDao<ExlImptplDetailes, String> getBaseDao() {
        return exlImptplDetailesDao;
    }
}
