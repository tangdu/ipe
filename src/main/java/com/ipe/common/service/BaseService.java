package com.ipe.common.service;

import com.ipe.common.dao.BaseDao;
import com.ipe.common.util.ConditionQuery;
import com.ipe.common.util.PageModel;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Transactional
public abstract class BaseService<M extends Serializable, PK extends Serializable> {


    public abstract BaseDao<M, PK> getBaseDao();

    @Transactional(readOnly = false)
    public M save(M model) {
        return (M) getBaseDao().save(model);
    }

    @Transactional(readOnly = false)
    public void saveOrUpdate(M model) {
        getBaseDao().saveOrUpdate(model);
    }

    @Transactional(readOnly = false)
    public void update(M model) {
        getBaseDao().update(model);
    }

    @Transactional(readOnly = false)
    public void merge(M model) {
        getBaseDao().merge(model);
    }

    @Transactional(readOnly = false)
    public void delete(PK id) {
        getBaseDao().delete(id);
    }

    @Transactional(readOnly = false)
    public void delete(PK[] ids) {
        getBaseDao().batchDel(ids);
    }

    @Transactional(readOnly = false)
    public void deleteObject(M model) {
        getBaseDao().deleteObject(model);
    }

    @Transactional(readOnly = true)
    public M get(PK id) {
        return getBaseDao().get(id);
    }

    @Transactional(readOnly = true)
    public Long countAll() {
        return getBaseDao().countAll();
    }

    @Transactional(readOnly = true)
    public List<M> listAll() {
        return getBaseDao().listAll();
    }

    @Transactional(readOnly = true)
    public M get(String hql, Map<String, Object> map) {
        return (M) getBaseDao().findOne(hql, map);
    }

    @Transactional(readOnly = true)
    public List<M> list(ConditionQuery conditionQuery) {
        return getBaseDao().list(conditionQuery);
    }

    @Transactional(readOnly = true)
    public List<M> list(ConditionQuery conditionQuery, PageModel pageModel) {
        return getBaseDao().list(conditionQuery, pageModel);
    }

    @Transactional(readOnly = true)
    public Long countBy(String hql, Map<String, Object> map) {
        return (Long) getBaseDao().findOne(hql, map);
    }

    @Transactional(readOnly = true)
    public List<M> list(String hql, Object... obj) {
        return getBaseDao().list(hql, obj);
    }

    @Transactional(readOnly = true)
    public List<M> list(String hql, Map<String, Object> map) {
        return getBaseDao().list(hql, map);
    }

    @Transactional(readOnly = true)
    public List<M> list(String hql, PageModel pageModel, Map<String, Object> map) {
        return getBaseDao().list(hql, map, pageModel);
    }

    @Transactional(readOnly = true)
    public List<M> where(PageModel pageModel, String where, Map<String, Object> map) {
        if (where == null || "".equals(where)) {
            return getBaseDao().list("from " + getBaseDao().HQL_TABNEME + " where 1=1 ", pageModel, null);
        } else {
            return getBaseDao().list("from " + getBaseDao().HQL_TABNEME + " where 1=1 " + where, pageModel, map);
        }
    }

    @Transactional(readOnly = true)
    public List<M> where(PageModel pageModel, String where, Object... obj) {
        if (where == null || "".equals(where)) {
            return getBaseDao().list("from " + getBaseDao().HQL_TABNEME + " where 1=1 ", pageModel, null);
        } else {
            return getBaseDao().list("from " + getBaseDao().HQL_TABNEME + " where 1=1 " + where, pageModel, obj);
        }
    }

    @Transactional(readOnly = true)
    public List<M> where(String where, Object... obj) {
        return where(null, where, obj);
    }

    @Transactional(readOnly = true)
    public List<M> where(String where, Map<String, Object> map) {
        return where(where, null, map);
    }

    @Transactional(readOnly = true)
    public List<M> where(PageModel pageModel) {
        return where(pageModel, null);
    }
}
