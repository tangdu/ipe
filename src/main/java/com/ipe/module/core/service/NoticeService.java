package com.ipe.module.core.service;

import com.ipe.common.dao.BaseDao;
import com.ipe.common.service.BaseService;
import com.ipe.module.core.dao.NoticeDao;
import com.ipe.module.core.entity.Notice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class NoticeService extends BaseService<Notice, String> {
    @Autowired
    private NoticeDao noticeDao;

    @Override
    public BaseDao<Notice, String> getBaseDao() {
        return noticeDao;
    }
}
