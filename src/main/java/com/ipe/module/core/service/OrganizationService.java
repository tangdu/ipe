package com.ipe.module.core.service;

import com.ipe.common.dao.BaseDao;
import com.ipe.common.service.BaseService;
import com.ipe.module.core.dao.OrganizationDao;
import com.ipe.module.core.entity.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tangdu
 * Date: 13-9-14
 * Time: 下午11:00
 * To change this template use File | Settings | File Templates.
 */
@Service
@Transactional
public class OrganizationService extends BaseService<Organization,String> {
    @Autowired
    private OrganizationDao organizationDao;

    @Override
    public BaseDao<Organization, String> getBaseDao() {
        return organizationDao;
    }

    public List<Organization> getTree(final String pid){
        List<Organization> all= organizationDao.list("from Organization where parent is null");
        return all;
    }
}
