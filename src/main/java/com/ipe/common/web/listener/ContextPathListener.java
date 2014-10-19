package com.ipe.common.web.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 上下wen
 * @author tangdu
 *
 * @time 2013-6-2 上午11:11:01
 */
public class ContextPathListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ServletContext sc = sce.getServletContext();
		sc.removeAttribute("base");
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext sc = sce.getServletContext();
		sc.setAttribute("base", getContextPath(sc));
	}

	private String getContextPath(ServletContext sc) {
		return sc.getContextPath();
	}
}
