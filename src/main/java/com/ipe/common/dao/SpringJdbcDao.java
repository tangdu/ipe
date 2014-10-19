package com.ipe.common.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class SpringJdbcDao {

    private
    @Autowired
    JdbcTemplate jdbcTemplate;

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
                                 Class<?> elementType) {
        if (conditions != null && conditions.size() > 0) {
            return jdbcTemplate.queryForList(sql, conditions.toArray(),
                    elementType);
        }
        return jdbcTemplate.queryForList(sql, elementType);
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
