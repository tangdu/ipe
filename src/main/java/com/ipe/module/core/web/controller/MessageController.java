package com.ipe.module.core.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ipe.module.core.entity.Notice;
import com.ipe.module.core.service.MessageService;
import com.ipe.module.core.web.util.BodyWrapper;
import com.ipe.module.core.web.util.RestRequest;

@Controller
@RequestMapping("/message")
public class MessageController extends AbstractController {

	@Autowired
	MessageService messageService;

	@RequestMapping(value = { "/list" })
	public @ResponseBody BodyWrapper list(Notice notice, RestRequest rest) {
		try {
			messageService.where(rest.getPageModel());
			return success(rest.getPageModel());
		} catch (Exception e) {
			LOGGER.error("query error", e);
			return failure(e);
		}
	}
}
