package com.ipe.module.core.service;

import com.ipe.common.dao.BaseDao;
import com.ipe.common.service.BaseService;
import com.ipe.module.core.dao.ResourceDao;
import com.ipe.module.core.entity.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
public class ResourceService extends BaseService<Resource, String> {
    @Autowired
    private ResourceDao resourceDao;

    @Override
    public BaseDao<Resource, String> getBaseDao() {
        return resourceDao;
    }

    public List<Resource> getResources(final String pid) {
        List<Resource> all = resourceDao.list("from Resource where parent is null");
        return all;
    }

    public Resource saveResource(Resource resource) {
        Resource parent = resourceDao.get(resource.getParent().getId());
        resource.setParent(parent);
        resource.setSno(resourceDao.getMaxSno());
        resource.setCreatedDate(new Date());
        return resourceDao.save(resource);
    }
}
