package com.ipe.module.core.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ipe.common.dao.BaseDao;
import com.ipe.common.dao.SpringJdbcDao;
import com.ipe.common.service.BaseService;
import com.ipe.common.util.CollectionSort;
import com.ipe.module.core.dao.MenuDao;
import com.ipe.module.core.dao.ResourceDao;
import com.ipe.module.core.entity.Menu;
import com.ipe.module.core.entity.Resource;

/**
 * Created with IntelliJ IDEA. User: tangdu Date: 13-10-5 Time: 下午3:24 To change
 * this template use File | Settings | File Templates.
 */
@Service
@Transactional
public class MenuService extends BaseService<Menu, String> {
	@Autowired
	private MenuDao menuDao;
	@Autowired
	private SpringJdbcDao springJdbcDao;
	@Autowired
	private ResourceDao resourceDao;

	@Override
	public BaseDao<Menu, String> getBaseDao() {
		return menuDao;
	}

	public List<Menu> getMenus(final String pid) {
		List<Menu> all = null;
		if (StringUtils.isBlank(pid)) {
			all = menuDao.list("from Menu where parent is null");
		} else {
			all = menuDao.list("from Menu where parent =?", pid);
		}
		eachMenu(all);
		return all;
	}

	/**
	 * 查询菜单关联资源
	 * 
	 * @param menus
	 */
	void eachMenu(List<Menu> menus) {
		for (Menu menu : menus) {
			String resourceId = menu.getResourceId();
			if (StringUtils.isNotBlank(resourceId)) {
				Resource resource = resourceDao
						.sqlFindOne("select * from t_cor_resource where id_='"
								+ resourceId + "'");
				menu.setResource(resource);
			}
		}
	}

	/**
	 * 保存
	 * 
	 * @param menu
	 * @return
	 */
	public Menu saveMenu(Menu menu) {
		menu.setParent(menuDao.get(menu.getParent().getId()));
		menu.setCreatedDate(new Date());
		menu.setSno(menuDao.getMaxSno());
		return menuDao.save(menu);
	}

	/**
	 * 返回菜单树
	 * 
	 * @return
	 */
	public Menu getTreeMenus() {
		List<Menu> menus = menuDao.listAll();
		Menu root = getRootMenu(menus);
		CollectionSort.sortList(menus, "sno", true);
		eachMenu(menus, root);
		return root;
	}

	/**
	 * 返回菜单树+关联资源
	 * 
	 * @return
	 */
	public Menu getTreeResourceMenus() {
		List<Menu> menus = menuDao.listAll();
		eachMenu(menus);
		Menu root = getRootMenu(menus);
		CollectionSort.sortList(menus, "sno", true);
		eachMenu(menus, root);
		return root;
	}

	/**
	 * 根据为空取得菜单根节点
	 * 
	 * @param menus
	 * @return
	 */
	private Menu getRootMenu(List<Menu> menus) {
		Menu root = null;
		for (Menu menu : menus) {
			if (menu.getParent() == null && menu.getId() != null) {
				root = new Menu();
				BeanUtils.copyProperties(menu, root);
				break;
			}
		}
		return root;
	}

	/**
	 * 用户所具有的菜单，关联权限
	 *
	 * @param userId
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<Menu> getUserMenuObject(String userId, String roleId,
			String isAdmin) {
		List<Menu> menus = null;
		if (!"1".equals(isAdmin)) {// 非管理员
			String sql = "select * from ( select t01.* from t_cor_menu t01 join (\n"
					+ "SELECT t4.resource_id from t_cor_user t1 join \n"
					+ "t_cor_user_role t2 on t1.id_=t2.user_id\n"
					+ "join t_cor_role t3 on t2.role_id=t3.id_\n"
					+ "join t_cor_authority t4 on t4.role_id=t3.id_ where t1.id_=? and t3.enabled_='1' and t1.enabled_='1') t02 \n"
					+ "on t01.resource_id=t02.resource_id) t  order by t.sno_ asc";
			menus = menuDao.listBySql(sql, userId);
		} else {
			menus = menuDao.listAll();
		}
		if (menus == null || menus.isEmpty()) {
			return null;
		}
		Menu root = getRootMenu(menus);
		eachMenu(menus, root);
		List<Menu> trees=root.getRows();
		CollectionSort.sortList(trees, "sno", true);
		return trees;
	}

	static final PropertyFilter PROPERTYFILTER = new PropertyFilter() {
		@Override
		public boolean apply(Object source, String name, Object value) {
			if ("id".equals(name)) {
				return false;
			} else if ("menu".equals(name)) {
				return true;
			} else if ("menuUrl".equals(name)) {
				return true;
			} else if ("menuType".equals(name)) {
				return true;
			} else if ("text".equals(name)) {
				return true;
			} else if ("leaf".equals(name)) {
				return true;
			}
			return false;
		}
	};

	/**
	 * 将菜单构建成树
	 * 
	 * @param userId
	 * @param roleId
	 * @param isAdmin
	 * @return
	 */
	@Transactional(readOnly = true)
	public String getUserMenu(String userId, String roleId, String isAdmin) {
		List<Menu> menus = getUserMenuObject(userId, roleId, isAdmin);
		String result = JSON.toJSONString(menus, PROPERTYFILTER,
				SerializerFeature.UseSingleQuotes,
				SerializerFeature.WriteNullListAsEmpty);
		return result;
	}

	/**
	 * 将菜单构建成树html
	 * 
	 * @param userId
	 * @param roleId
	 * @param isAdmin
	 * @return
	 */
	@Transactional(readOnly = true)
	public String getUserMenuHtml(String userId, String roleId, String isAdmin) {
		List<Menu> menus = getUserMenuObject(userId, roleId, isAdmin);
		StringBuffer sb=new StringBuffer();
		sb.append("<div id='ddtopmenubar' class='mattblackmenu' style='height:60px;'><ul>");
		//一级菜单
		for(Menu m1:menus){
			sb.append("<li><a rel='"+m1.getId()+"'>"+m1.getMenuName()+"</a></li>");
		}
		sb.append("</ul></div>");
		//多级菜单
		for(Menu m1:menus){
			if(m1.getRows()!=null && !m1.getRows().isEmpty()){
				sb.append("<ul id='"+m1.getId()+"' class='ddsubmenustyle'>");
				eachHtmlMenu(m1.getRows(),sb);
				sb.append("</ul>");
			}
		}
		return sb.toString();
	}

	/**
	 * 将菜单组装成树
	 * 
	 * @param menus
	 * @param root
	 */
	void eachMenu(List<Menu> menus, Menu root) {
		for (Menu m1 : menus) {
			if (m1.getParent() != null
					&& root.getId().equals(m1.getParent().getId())) {
				if (root.getRows() == null) {
					root.setRows(new ArrayList<Menu>());
				}
				root.getRows().add(m1);
				root.setLeaf(false);
				eachMenu(menus, m1);
			}
		}
	}
	
	void eachHtmlMenu(List<Menu> menus, StringBuffer sbt) {
		for (Menu m1 : menus) {
			if (m1.getRows()!=null && !m1.getRows().isEmpty()) {
				sbt.append("<li>");
				sbt.append("<a>"+m1.getMenuName()+"</a><ul>");
				eachHtmlMenu(m1.getRows(),sbt);
				sbt.append("</ul></li>");
			}else{
				String result = JSON.toJSONString(m1, PROPERTYFILTER,
						SerializerFeature.UseSingleQuotes,
						SerializerFeature.WriteNullListAsEmpty);
				sbt.append("<li><a onclick=ipe.fuc.openMenu2("+result+")>"+m1.getMenuName()+"</a></li>");
			}
		}
	}
	
	/*void eachHtmlMenu(List<Menu> menus, StringBuffer sbt) {
		for (Menu m1 : menus) {
			if (m1.getRows()!=null && !m1.getRows().isEmpty()) {
				sbt.append("<li><a href='#' >"+m1.getMenuName()+"</a><ul>");
				eachHtmlMenu(m1.getRows(),sbt);
				sbt.append("</ul></li>");
			}else{
				sbt.append("<li><a>"+m1.getMenuName()+"</a></li>");
			}
		}
	}*/

	@Transactional(readOnly = false)
	public void updateSort(String[] ids, String pid) {
		for (int i = 0; i < ids.length; i++) {
			menuDao.updateParent(pid, i, ids[i]);
		}
	}
}
