package com.ipe.module.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ipe.common.dao.BaseDao;
import com.ipe.common.service.BaseService;
import com.ipe.module.core.dao.MessageDao;
import com.ipe.module.core.entity.Message;

@Service
@Transactional
public class MessageService extends BaseService<Message, String> {
	@Autowired
	private MessageDao messageDao;

	@Override
	public BaseDao<Message, String> getBaseDao() {
		return messageDao;
	}
}
