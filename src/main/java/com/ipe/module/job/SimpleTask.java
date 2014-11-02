package com.ipe.module.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ipe.module.core.dao.MenuDao;

/**
 * 自定义简单任务类
 * @author tangdu
 *
 */
@Component
@Transactional(readOnly=false)
public class SimpleTask {
	@Autowired
	private MenuDao menuDao;

	@Scheduled(cron = "9 9 *  * * ? ")
	public void myTest() {
		System.out.println("-----------"+menuDao.listAll().size());
	}
}
