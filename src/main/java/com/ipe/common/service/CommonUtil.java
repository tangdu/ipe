package com.ipe.common.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ipe.common.dao.SpringJdbcDao;
import com.ipe.module.exl.TableColumn;

/**
 * 封装公用方法
 * @author tangdu
 *
 */
@Repository
public class CommonUtil {

	@Autowired
	private SpringJdbcDao springJdbcDao;
	
	/**
	 * 得到MySQL表定义
	 * @param mappingTable
	 * @param tableBelongUser
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TableColumn> getTableInfo(String mappingTable,String tableBelongUser){
    	String sql="select t.ORDINAL_POSITION as 'index',t.COLUMN_NAME as fieldName,t.COLUMN_TYPE as fieldType,t.COLUMN_COMMENT as fieldDesc "
    			+ "from information_schema.`COLUMNS` t where t.TABLE_SCHEMA=? and t.TABLE_NAME=?";
    	List<Object> conditions=new ArrayList<Object>();
    	conditions.add(tableBelongUser);
    	conditions.add(mappingTable);
    	return (List<TableColumn>) springJdbcDao.queryList(sql, conditions, TableColumn.class);
    }
}
