package com.ipe.common.dao;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class SpringJdbcDao {

    private
    @Autowired
    JdbcTemplate jdbcTemplate;

    public void update(String sql,Object[] args){
    	jdbcTemplate.update(sql, args);
    }
    
    public void execute(String sql) {
       jdbcTemplate.execute(sql);
    }

    public List<?> queryList(String sql, Class<?> elementType) {
        return queryForList(sql, null, elementType);
    }

    public List<?> queryList(String sql, List<Object> conditions,
                             Class<?> elementType) {
        return queryForList(sql, conditions, elementType);
    }

    public long queryLong(String sql) {
        return queryForLong(sql, null);
    }

    public long queryLong(String sql, List<Object> conditions) {
        return queryForLong(sql, conditions);
    }

    public List<Map<String, Object>> queryListMap(String sql) {
        return queryForListMap(sql, null);
    }

    public List<Map<String, Object>> queryListMap(String sql,
                                                  List<Object> conditions) {
        return queryForListMap(sql, conditions);
    }

    public Map<String, Object> queryMap(String sql) {
        return queryForMap(sql, null);
    }

    public Map<String, Object> queryMap(String sql, List<Object> conditions) {
        return queryForMap(sql, conditions);
    }

    private List<?> queryForList(String sql, List<Object> conditions,
                                 Class<?> mappedClass) {
    	Field [] f=mappedClass.getFields();
    	if(f.length>1){
    		if (conditions != null && conditions.size() > 0) {
                return jdbcTemplate.query(sql, conditions.toArray(),
                		BeanPropertyRowMapper.newInstance(mappedClass));
            }
            return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(mappedClass));
    	}else{
    		if (conditions != null && conditions.size() > 0) {
                return jdbcTemplate.queryForList(sql, conditions.toArray(),
                		mappedClass);
            }
            return jdbcTemplate.queryForList(sql, mappedClass);
    	}
    }

    private long queryForLong(String sql, List<Object> conditions) {
        if (conditions != null && conditions.size() > 0) {
            return jdbcTemplate.queryForObject(sql, Long.class, conditions);
        }
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    private List<Map<String, Object>> queryForListMap(String sql,
                                                      List<Object> conditions) {
        if (conditions != null && conditions.size() > 0) {
            return jdbcTemplate.queryForList(sql, conditions.toArray());
        }
        return jdbcTemplate.queryForList(sql);
    }

    private Map<String, Object> queryForMap(String sql, List<Object> conditions) {
        if (conditions != null && conditions.size() > 0) {
            return jdbcTemplate.queryForMap(sql, conditions.toArray());
        }
        return jdbcTemplate.queryForMap(sql);
    }
}
