package com.ipe.module.core.service;

import com.ipe.common.dao.BaseDao;
import com.ipe.common.service.BaseService;
import com.ipe.module.core.dao.RemindDao;
import com.ipe.module.core.entity.Remind;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class RemindService extends BaseService<Remind,String> {
    @Autowired
    private RemindDao remindDao;

    @Override
    public BaseDao<Remind, String> getBaseDao() {
        return remindDao;
    }
}
