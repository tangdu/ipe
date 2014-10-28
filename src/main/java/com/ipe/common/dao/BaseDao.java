package com.ipe.common.dao;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Id;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ipe.common.util.ConditionQuery;
import com.ipe.common.util.PageModel;


public abstract class BaseDao<M extends Serializable, PK extends Serializable> {

	private final Class<M> entityClass;
	@SuppressWarnings("unused")
	private String PK_NAME;
	public String HQL_TABNEME;// 表名
	private String HQL_LIST_ALL;// 查询所有
	private String HQL_COUNT_ALL;// COUNT *

	@SuppressWarnings("unchecked")
	public BaseDao() {
		this.entityClass = (Class<M>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
		Field[] fields = this.entityClass.getDeclaredFields();
		for (Field f : fields) {
			if (f.isAnnotationPresent(Id.class)) {
				PK_NAME = f.getName();
			}
		}
		HQL_TABNEME = this.entityClass.getName();
		HQL_LIST_ALL = "from " + HQL_TABNEME;
		HQL_COUNT_ALL = "select count(*) " + HQL_LIST_ALL;
	}

	private @Autowired SessionFactory sessionFactory;

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public M save(M model) {
		getSession().merge(model);
		//getSession().save(model);
		getSession().flush();
		return model;
	}

	public void saveOrUpdate(M model) {
		getSession().saveOrUpdate(model);
		getSession().flush();
	}

	public void update(M model) {
		getSession().merge(model);
		getSession().flush();
	}

	public void merge(M model) {
		getSession().merge(model);
		getSession().flush();
	}

	public void delete(PK id) {
		getSession().delete(get(id));
		getSession().flush();
	}

	public void deleteObject(M model) {
		getSession().delete(model);
		getSession().flush();
	}

	public void deleteAll() {
		execute("delete from " + HQL_TABNEME);
	}

	public void batchDel(PK ids[]) {
		for(PK id :ids){
			if(StringUtils.isNotBlank(id.toString())){
				delete(id);
			}
		}
	}

	public int execute(String hql, Map<String, Object> map) {
		Query query = this.getSession().createQuery(hql);
		setParameter(query, map);
		return query.executeUpdate();
	}

	public int execute(String hql, Object... obj) {
		Query query = this.getSession().createQuery(hql);
		setParameter(query, obj);
		return query.executeUpdate();
	}

	public Object findOne(String hql, Object... obj) {
		Query query = this.getSession().createQuery(hql);
		setParameter(query, obj);
		return query.uniqueResult();
	}

	public Object findOne(String hql, Map<String, Object> map) {
		Query query = this.getSession().createQuery(hql);
		setParameter(query, map);
		return query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public M get(PK id) {
		return (M) getSession().get(this.entityClass, id);
	}

	public Long countAll() {
		Long cot = (Long) findOne(HQL_COUNT_ALL);
		return cot;
	}

	public List<M> listAll() {
		return list(HQL_LIST_ALL);
	}

	@SuppressWarnings("unchecked")
	public List<M> listBySql(String sql, Object... obj) {
		SQLQuery query = this.getSession().createSQLQuery(sql);
		setParameter(query, obj);
		return query.addEntity(entityClass).list();
	}

	public void flush() {
		getSession().flush();
	}

	public void clear() {
		getSession().clear();
	}

	public <T> List<T> list(ConditionQuery query) {
		return list(query, null);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> List<T> list(final ConditionQuery query, PageModel model) {
		// conditions
		Criteria criteria = getSession().createCriteria(this.entityClass);
		query.buildConditions(criteria);
		query.buildOrders(criteria);
		List list = criteria.list();

		if (model != null) {
			int cot = list.size();
			model.setTotalRows(cot);
			criteria.setFirstResult(model.getStartRow());
			criteria.setMaxResults(model.getEndRow());
			list = criteria.list();
			model.setList(list);
		}
		return list;
	}

	public <T> List<T> list(String hql, final Map<String, Object> map) {
		return bulid(hql, null, map);
	}

	public <T> List<T> list(String hql, Object... obj) {
		return bulid(hql, null, obj);
	}

	public <T> List<T> list(String hql, PageModel model, Object... obj) {
		return bulid(hql, model, obj);
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> bulid(String hql, PageModel model,
			final Map<String, Object> map) {
		Query query = getSession().createQuery(hql);
		setParameter(query, map);
		if (model != null) {
			if (model != null) {
				query.setFirstResult(model.getStartRow());
				query.setMaxResults(model.getEndRow());
			}
			hql = hql.replaceFirst("\\*", "count");// replace first *
			Long totalRows = (Long) findOne(hql, map);
			model.setTotalRows(totalRows);
			model.setList(query.list());

			return (List<T>) model.getList();
		}
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> bulid(String hql, PageModel model, final Object... obj) {
		Query query = getSession().createQuery(hql);
		setParameter(query, obj);
		if (model != null) {
			if (model != null) {
				query.setFirstResult(model.getStartRow());
				query.setMaxResults(model.getEndRow());
			}
			if (hql.startsWith("select")) {
				hql = "select count(*) from (" + hql + ")";
			} else {
				hql = "select count(*) " + hql;
			}
			Long totalRows = (Long) findOne(hql, obj);
			model.setTotalRows(totalRows);
			model.setList(query.list());
			return (List<T>) model.getList();
		}
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> list(Criteria criteria) {
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public <T> T unique(Criteria criteria) {
		return (T) criteria.uniqueResult();
	}

	private void setParameter(Query query, Map<String, Object> map) {
		if (map != null) {
			for (Entry<String, Object> entry : map.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}
	}

	private void setParameter(Query query, Object... obj) {
		if (obj != null) {
			for (int i = 0; i < obj.length; i++) {
				query.setParameter(i, obj[i]);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> sqllist(String sql) {
		return (List<T>) this.getSession().createSQLQuery(sql)
				.addEntity(entityClass).list();
	}

	@SuppressWarnings("unchecked")
	public <T> T sqlFindOne(String sql) {
		List<T> ll = (List<T>) this.getSession().createSQLQuery(sql)
				.addEntity(entityClass).list();
		if (ll != null && !ll.isEmpty()) {
			return ll.get(0);
		}
		return null;
	}
}
