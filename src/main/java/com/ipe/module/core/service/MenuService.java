package com.ipe.module.core.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.service.spi.ServiceException;
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
 * Created with IntelliJ IDEA.
 * User: tangdu
 * Date: 13-10-5
 * Time: 下午3:24
 * To change this template use File | Settings | File Templates.
 */
@Service
@Transactional
public class MenuService extends BaseService<Menu, String> {
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private SpringJdbcDao springJdbcDao;
    @Autowired
    private ResourceDao  resourceDao;
    
    @Override
    public BaseDao<Menu, String> getBaseDao() {
        return menuDao;
    }

    public List<Menu> getMenus(final String pid) {
    	List<Menu> all=null;
    	if(StringUtils.isBlank(pid)){
    		all=menuDao.list("from Menu where parent is null");
    	}else{
    		all=menuDao.list("from Menu where parent =?",pid);
    	}
        eachMenu(all);
        return all;
    }
    
    /**
     * 查询菜单关联资源
     * @param menus
     */
    void eachMenu(List<Menu> menus) {
    	for(Menu menu :menus){
    		String resourceId=menu.getResourceId();
			if(StringUtils.isNotBlank(resourceId)){
				Resource resource=resourceDao.sqlFindOne("select * from t_cor_resource where id_='"+resourceId+"'");
				menu.setResource(resource);
			}
    	}
    }

    /**
     * 保存
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
     * @return
     */
    public Menu getTreeMenus(){
    	List<Menu> menus=menuDao.listAll();
    	Menu root=getRootMenu(menus);
    	eachMenu(menus, root);
    	return root;
    }
    
    /**
     * 返回菜单树+关联资源
     * @return
     */
    public Menu getTreeResourceMenus(){
    	List<Menu> menus=menuDao.listAll();
    	eachMenu(menus);
    	Menu root=getRootMenu(menus);
    	eachMenu(menus, root);
    	return root;
    }
    
    /**
     * 根据为空取得菜单根节点
     * @param menus
     * @return
     */
    private Menu getRootMenu(List<Menu> menus){
    	Menu root=null;
    	for (Menu menu : menus) {
            if (menu.getParent() == null && menu.getId()!=null) {
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
    public String getUserMenu(String userId,String roleId,String isAdmin) {
        List<Menu> menus=null;
        Menu root=null;
        if (!"1".equals(isAdmin)) {
            String sql = "select * from ( select t01.* from t_cor_menu t01 join (\n" +
                    "SELECT t4.resource_id from t_cor_user t1 join \n" +
                    "t_cor_user_role t2 on t1.id_=t2.user_id\n" +
                    "join t_cor_role t3 on t2.role_id=t3.id_\n" +
                    "join t_cor_authority t4 on t4.role_id=t3.id_ where t1.id_=? and t3.enabled_='1' and t1.enabled_='1') t02 \n" +
                    "on t01.resource_id=t02.resource_id) t  order by t.sno_ asc";
			menus = menuDao.listBySql(sql, userId);
        } else {
        	menus=menuDao.listAll();
        }
        
        if (menus == null || menus.isEmpty()) {
            return "[]";
        }
        root=getRootMenu(menus);
        if (root == null) {
            throw new ServiceException("root is null");
        }
        eachMenu(menus, root);
        
        PropertyFilter propertyFilter = new PropertyFilter() {
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
        CollectionSort.sortList(menus, "sno", true);
        String result=JSON.toJSONString(menus, propertyFilter, SerializerFeature.UseSingleQuotes, SerializerFeature.WriteNullListAsEmpty);
        return result;
    }

    /**
     * 将菜单组装成树
     * @param menus
     * @param root
     */
    void eachMenu(List<Menu> menus, Menu root) {
        for (Menu m1 : menus) {
            if (m1.getParent() != null && root.getId().equals(m1.getParent().getId())) {
                if (root.getRows() == null) {
                    root.setRows(new ArrayList<Menu>());
                }
                root.getRows().add(m1);
                eachMenu(menus, m1);
            }
        }
    }

    /*void eachTreeMenu(Menu root, StringBuffer sbt) {
        sbt.append("{text:'" + root.getMenuName() + "',scope:this");
        if (!root.isLeaf()) {
            StringBuffer sbr = new StringBuffer(",menu:[");
            for (Menu m : root.getRows()) {
                eachTreeMenu(m, sbr);
            }
            String t = sbr.substring(0, sbr.lastIndexOf(","));
            sbt.append(t).append("]");
        } else {
            sbt.append(",handler:this.menuClick,attr:{menuUrl:'" + root.getMenuUrl() + "',menuType:'" + root.getMenuType() + "'}");
        }
        sbt.append("},");
    }*/

    @Transactional(readOnly = false)
    public void updateMenus(String[] ids, String pid) {
        for (int i = 0; i < ids.length; i++) {
            menuDao.updateParent(pid, i, ids[i]);
        }
    }
}
