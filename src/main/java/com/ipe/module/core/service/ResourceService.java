package com.ipe.module.core.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ipe.common.dao.BaseDao;
import com.ipe.common.service.BaseService;
import com.ipe.module.core.dao.ResourceDao;
import com.ipe.module.core.entity.Resource;

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
    	List<Resource> all =null;
    	if(StringUtils.isBlank(pid)){
    		all = resourceDao.list("from Resource where parent is null");
    	}else{
    		all = resourceDao.list("from Resource where parent.id =?",pid);
    	}
        return all;
    }
    
    
    private Resource getRootResource(List<Resource> resources){
    	Resource root=null;
    	for (Resource resource : resources) {
            if (resource.getParent() == null && resource.getId()!=null) {
                root = new Resource();
                BeanUtils.copyProperties(resource, root);
                break;
            }
        }
    	return root;
    }
    
    void eachResouce(List<Resource> resources, Resource root,Boolean checked) {
        for (Resource m1 : resources) {
        	m1.setChecked(checked);
            if (m1.getParent() != null && root.getId().equals(m1.getParent().getId())) {
                if (root.getRows() == null) {
                    root.setRows(new ArrayList<Resource>());
                }
                root.getRows().add(m1);
                root.setLeaf(false);
                eachResouce(resources, m1,checked);
            }
        }
    }
    
    /**
     * 返回资源树,没有checked复选框
     * @return
     */
    public Resource getTreeResources(){
    	List<Resource> resources=resourceDao.listAll();
    	Resource root=getRootResource(resources);
    	eachResouce(resources, root,null);
    	return root;
    }
    
    /**
     * 返回资源树
     * @return
     */
    public Resource getChecdkboxTreeResources(){
    	List<Resource> resources=resourceDao.listAll();
    	Resource root=getRootResource(resources);
    	root.setChecked(false);
    	eachResouce(resources, root,false);
    	return root;
    }

    /**
     * 保存资源
     * @param resource
     * @return
     */
    public Resource saveResource(Resource resource) {
        Resource parent = resourceDao.get(resource.getParent().getId());
        resource.setParent(parent);
        resource.setSno(resourceDao.getMaxSno());
        resource.setCreatedDate(new Date());
        return resourceDao.save(resource);
    }
    
    @Transactional(readOnly = false)
    public void updateSort(String[] ids, String pid) {
        for (int i = 0; i < ids.length; i++) {
        	resourceDao.updateParent(pid, i, ids[i]);
        }
    }
}
