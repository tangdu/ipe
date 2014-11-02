package com.ipe.module.core.web.controller;

import static org.apache.shiro.SecurityUtils.getSubject;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.ipe.common.util.Anonymous;
import com.ipe.common.util.VerificationCodeUtil;
import com.ipe.module.bpm.service.ProcessTaskService;
import com.ipe.module.core.entity.Dict;
import com.ipe.module.core.entity.SysConfig;
import com.ipe.module.core.service.DictService;
import com.ipe.module.core.service.MenuService;
import com.ipe.module.core.service.MessageService;
import com.ipe.module.core.service.NoticeService;
import com.ipe.module.core.service.RoleService;
import com.ipe.module.core.service.SysConfigService;
import com.ipe.module.core.service.UserService;
import com.ipe.module.core.web.security.CaptchaException;
import com.ipe.module.core.web.security.CustUsernamePasswordToken;
import com.ipe.module.core.web.security.SystemRealm;
import com.ipe.module.core.web.util.RestRequest;
import com.ipe.module.core.web.util.WebUtil;

/**
 * Created with IntelliJ IDEA. User: tangdu Date: 13-9-7 Time: 下午10:21 To change
 * this template use File | Settings | File Templates.
 */
@Controller
public class IndexController extends AbstractController {

	@Autowired
	private UserService userService;
	@Autowired
	private MenuService menuService;
	@Autowired
	private RoleService roleService;

	@Anonymous
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(ModelMap data) {
		// 1_加载菜单
		SystemRealm.UserInfo userInfo = (SystemRealm.UserInfo) SecurityUtils
				.getSubject().getPrincipal();
		String menus = menuService.getUserMenu(userInfo.getUserId(),userInfo.getRoleId(),userInfo.getAdmin());
		data.put("menus", menus);
		// 2_字典表
		List<Dict> list = dictService.listAll();
		data.put("dicts", JSON.toJSONString(list));
		// 3_配置表
		List<SysConfig> config = sysConfigService.listAll();
        Map<String, Object> map = new HashMap<String, Object>();
        if (config != null) {
            for (SysConfig obj : config) {
            	map.put(obj.getKey(), obj.getValue());
            }
        }
        data.put("configs", JSON.toJSONString(map));
        // 4_分配权限
        List<String> authorits=roleService.getUserAuthorits(userInfo.getUserId());
        data.put("authorits", JSON.toJSONString(authorits));
        
		return "index";
	}

	@Anonymous
	@RequestMapping(value = "/signin", method = RequestMethod.GET)
	public String signin() {
		return "signin";
	}

	/**
	 * 根据url跳转到不同URL
	 *
	 * @param url
	 * @return
	 */
	@RequestMapping(value = "/reui/${url}", method = RequestMethod.GET)
	public String reUI(@PathVariable String url) {
		return url;
	}

	/**
	 * 用户登录
	 *
	 * @param useraccount
	 * @param password
	 * @param request
	 * @return
	 */
	@Anonymous
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@RequestParam String useraccount,
			@RequestParam String password, @RequestParam String captcha,HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		CustUsernamePasswordToken token = new CustUsernamePasswordToken(
				useraccount, password, request.getMethod(),
				captcha, request.getServletPath(),
				WebUtil.getIpAddr(request));
		try {
			getSubject().login(token);
			return "redirect:/";
		} catch (UnknownAccountException uae) {
			redirectAttributes.addAttribute("errorMsg", "用户名不存在系统！");
		} catch (IncorrectCredentialsException ice) {
			redirectAttributes.addAttribute("errorMsg", "密码错误！");
		} catch (LockedAccountException lae) {
			redirectAttributes.addAttribute("errorMsg", "用户已经被锁定不能登录，请与管理员联系！");
		} catch (ExcessiveAttemptsException eae) {
			redirectAttributes.addAttribute("errorMsg", "错误次数过多！");
		} catch (CaptchaException ae) {
			redirectAttributes.addAttribute("errorMsg", "验证码错误！");
		}catch (AuthenticationException ae) {
			redirectAttributes.addAttribute("errorMsg", "其他的登录错误！");
		}
		return "redirect:signin";
	}

	/**
	 * 注销
	 * 
	 * @param request
	 * @return
	 */
	@Anonymous
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request) {
		request.getSession().removeAttribute("user");
		request.getSession().removeAttribute("userPrvLoginTime");// TODO 上次登录时间
		try {
			userService.logout();
			getSubject().logout();
		} catch (Exception e) {
			LOGGER.error("Exception {}", e);
		}
		return "signin";
	}

	@Autowired
	private ProcessTaskService taskService;
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private MessageService messageService;
	@Autowired
	private DictService dictService;
	@Autowired
	private SysConfigService sysConfigService;

	private static final String PATH = "tools/";

	/**
	 * 首页展示
	 * 
	 * @param request
	 * @param data
	 * @return
	 */
	@Anonymous
	@RequestMapping(value = "/getIndexView")
	public String getIndexView(HttpServletRequest request, ModelMap data) {
		// step1:用户信息
		SystemRealm.UserInfo userInfo = ((SystemRealm.UserInfo) SecurityUtils
				.getSubject().getPrincipal());
		data.put("userInfo", userInfo);
		// step2:任务信息
		data.put("mtaskLists", taskService
				.userTaskList(null, new RestRequest()).getRows());
		// step3:最新公告
		data.put("noticeLists", noticeService.listAll());
		// step4:最新消息
		data.put("messageLists", messageService.listAll());
		// step5:内存信息
		MemoryMXBean my = ManagementFactory.getMemoryMXBean();
		data.put("initMemory", my.getHeapMemoryUsage().getInit() / 1000000
				+ " M");
		data.put("maxMemory", my.getHeapMemoryUsage().getMax() / 1000000 + " M");
		data.put("usedMemory", my.getHeapMemoryUsage().getUsed() / 1000000
				+ " M");
		OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
		data.put("osName", os.getName());
		RuntimeMXBean rt = ManagementFactory.getRuntimeMXBean();
		data.put("vmName", rt.getVmName());
		return PATH + "indexview_ftl";
	}

	/**
	 * 公共文件下载方法
	 *
	 * @param filePath
	 *            文件路径
	 * @param response
	 */
	@Anonymous
	@RequestMapping(value = { "/downFile" })
	public void downFile(@RequestParam String filePath,
			HttpServletResponse response) {
		try {
			filePath = URLDecoder.decode(filePath, "UTF-8");
			File file = new File(filePath);
			if (file.canRead() && file.exists()) {
				super.downFile(file, response);
			} else {
				super.downFileError(response);
			}
		} catch (Exception e) {
			LOGGER.error("downlogs error", e);
			super.downFileError(response);
		}
	}

	/**
	 * 生成验证码
	 * @param response
	 */
	@Anonymous
	@RequestMapping(value = { "/verificationCode" })
	public void verificationCode(HttpServletRequest request,HttpServletResponse response) {
		VerificationCodeUtil.createCode(request,response);
	}
}
