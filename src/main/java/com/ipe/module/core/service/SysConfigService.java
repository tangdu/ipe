package com.ipe.module.core.service;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ipe.common.dao.BaseDao;
import com.ipe.common.service.BaseService;
import com.ipe.module.core.dao.SysConfigDao;
import com.ipe.module.core.entity.SysConfig;


@Service
@Transactional
public class SysConfigService extends BaseService<SysConfig, String> {
    @Autowired
    private SysConfigDao sysConfigDao;

    @Override
    public BaseDao<SysConfig, String> getBaseDao() {
        return sysConfigDao;
    }

    public void update(String params) {
        if (StringUtils.isNotBlank(params)) {
            sysConfigDao.deleteAll();
            JSONObject jsonObject = JSON.parseObject(params);
            int i=0;
            for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            	i++;
                SysConfig sysConfig = new SysConfig();
                sysConfig.setKey(entry.getKey());
                sysConfig.setValue(entry.getValue()==null ? null :entry.getValue().toString());
                sysConfig.setSno(i);
                sysConfigDao.save(sysConfig);
            }
        }
    }
}
