package com.autonavi.mapart.mapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.autonavi.mapart.entity.DaoResult;
import com.autonavi.mapart.entity.ResponseStatus;
import com.autonavi.mapart.entity.User;
import com.autonavi.mapart.service.UserService;

/**
 * <p>
 * desc:涉及用户操作控制器
 * <p>
 * Copyright: Copyright(c)AutoNavi 2014
 * </p>
 * 
 * @author <a href="mailTo:i-caiqiang@autonavi.com">i-caiqiang</a>
 * @time 2014-4-25 15:32
 * 
 */
@Controller
@SessionAttributes("user")
public class UserMapper {

	@Autowired
	private UserService userService;
	private Logger log = Logger.getLogger(getClass());

	// ***用户登录操作*****

	@RequestMapping("/")
	public String index1() {
		return "login";
	}

	@RequestMapping("/index")
	public String index2() {
		return "login";
	}

	@RequestMapping("/logout")
	public String logout() {
		return "index";
	}
	
	@RequestMapping("/test")
	public String test() {
		
		return "testSession";
	}
	
	@RequestMapping("/404")
	public String error() {
		return "404";
	}

	@RequestMapping(value = "/login")
	public String dispatch(ModelMap model, HttpServletRequest request) {
		
		UserDetails userDetails = (UserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		if (userDetails == null) {
			return "login";
		}
		String[] str = userDetails.getUsername().split(",");
		request.getSession().setAttribute("uid", str[0]);
		request.getSession().setAttribute("truename", str[2]);
		request.getSession().setAttribute("role", str[3]);
		Set<String> roles = AuthorityUtils
				.authorityListToSet(SecurityContextHolder.getContext()
						.getAuthentication().getAuthorities());
		
		if (roles.contains("ROLE_ADMIN")) {
			return "redirect:admin";
		} else if (roles.contains("ROLE_QA")) {
			return "redirect:qa";
		} else if(roles.contains("ROLE_USER")) {
			return "redirect:operator";
		}else if(roles.contains("ROLE_CTRL")) {
			return "redirect:ctrl";
		}else {
			return "index";
		}
	}
	
	
	// ********** admin 关于用户操作begin ************
	@RequestMapping("/admin")
	public String admin(HttpServletRequest request) {
		log.debug("------------------------------------------------/admin");
		if (request.getSession().getAttribute("uid") == null) {
			return "login";
		}
		return "admin";
	}

	@RequestMapping("admin/personal")
	public String getPersonal(HttpServletRequest request) {
		log.debug("------------------------------------------------admin/personal");
		if (request.getSession().getAttribute("uid") == null) {
			return "login";
		}
		return "admin_personal";
	}

	@RequestMapping(value = "admin/user", method = RequestMethod.POST)
	public @ResponseBody
		ResponseStatus getUser(@RequestParam("page") Integer page,
			@RequestParam("truename") String truename,
			@RequestParam("role") Integer role, HttpServletRequest request) {
		log.debug("------------------------------------------------admin/user");
		int uid = MapperCommonUtil.getUserid(request);
		if (uid==-1) {
			return new ResponseStatus(200, "error", "用户登录已超时，请重新登录！");
		} else {
			try {
				int pageIndex = 1;
				try {
					pageIndex = Integer.valueOf(page);
				} catch (Exception e) {
				}
				int r = role;
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("pageIndex", pageIndex);
				map.put("recordCountPerPage", 9);
				map.put("role", r);
				map.put("truename", truename);
				DaoResult result = userService.getUserList(map);
				JSONArray array = JSONArray.fromObject(result.getList());
				return new ResponseStatus(100, array.toString(),
						String.valueOf(result.getTotalCount()));
			} catch (Exception e) {
				return new ResponseStatus(200, "0", "用户参数输入错误！");
			}
		}
	}

	@RequestMapping(value = "admin/newu", method = RequestMethod.POST)
	public @ResponseBody
		ResponseStatus newUser(@RequestParam("username") String username,
			@RequestParam("password") String password,
			@RequestParam("truename") String truename,
			@RequestParam("email") String email, @RequestParam("role") int role) {
		log.debug("------------------------------------------------admin/newu");
		if ("".equals(username) || "".equals(password) || "".equals(truename)
				|| "".equals(email) || "".equals(role)) {
			return new ResponseStatus(200, "0", "用户参数输入错误！");
		} else if (password.length() < 6 || !((1 == role) || 2 == role)) {
			return new ResponseStatus(200, "0", "用户参数输入错误！");
		} else {
			User user = new User(username, password, truename, email,
					Integer.valueOf(role));
			userService.insertOrupdateUser(user, 0);
			return new ResponseStatus(100, "0", "用户名或密码错误！");
		}
	}

	@RequestMapping(value = "admin/editu/{uid}", method = RequestMethod.GET)
	public @ResponseBody
	ResponseStatus editUser(@PathVariable Integer uid) {
		User user = new User();
		user.setUid(uid);
		user = userService.getUser(user);
		// model.addAttribute("usere", user);
		JSONArray json = JSONArray.fromObject(user);
		return new ResponseStatus(100, json.toString(), "");
	}

	@RequestMapping(value = "admin/editu", method = RequestMethod.POST)
	public @ResponseBody
	ResponseStatus editUser(@RequestParam("uid") Integer uid,
			@RequestParam("password") String password,
			@RequestParam("truename") String truename,
			@RequestParam("email") String email,
			@RequestParam("role") Integer role) {
		if ("".equals(uid)) {
			return new ResponseStatus(200, "0", "用户参数输入错误！(uid)");
		} else if ((!"".equals(password) && (password.length() < 6))
				|| !(1 == role || 2 == role)) {
			return new ResponseStatus(200, "0", "用户参数输入错误！(pwd	)");
		} else {
			User user = new User("", password, truename, email, role);
			user.setUid(uid);
			userService.insertOrupdateUser(user, 1);
			return new ResponseStatus(100, "0", "修改成功！");
		}
	}

	@RequestMapping(value = "admin/delu", method = RequestMethod.POST)
	public @ResponseBody
	ResponseStatus delUser(@RequestParam("uids") String uids) {
		if ("".equals(uids)) {
			return new ResponseStatus(200, "0", "用户参数输入错误！");
		} else {
			userService.deleteUser(uids);
			return new ResponseStatus(100, "0", "删除成功！");
		}
	}

	// ********** admin 关于用户操作end ************

	// ********** operator 关于用户操作degin ************
	@RequestMapping("/operator")
	public String index(Model model, HttpServletRequest request) {
		Object uidobj = request.getSession().getAttribute("uid");
		if (uidobj == null) {
			return "redirect:index";
		}
		return "operator";
	}

	@RequestMapping("/qa/art")
	public String qaArt(HttpServletRequest request) {
		return goArt(request);
	}
	
	@RequestMapping("/operator/art")
	public String art(HttpServletRequest request) {
		return goArt(request);
	}

	private String goArt(HttpServletRequest request) {
		Object uidobj = request.getSession().getAttribute("uid");
		if (uidobj == null) {
			return "redirect:index";
		}
		
		UserDetails userDetails = (UserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		if (userDetails == null) {
			return "login";
		}
		String[] str = userDetails.getUsername().split(",");
		request.getSession().setAttribute("uid", str[0]);
		request.getSession().setAttribute("truename", str[2]);
		return "artindex";
	}

	@RequestMapping(value = "/operator/expand_iframe")
	public String expand() {
		return "main-panel-expandmap";
	}

	// ********** operator 关于用户操作end ************
	
	
	/********** qa 操作begin ************/
	@RequestMapping("/qa")
	public String QA(HttpServletRequest request) {
		if (request.getSession().getAttribute("uid") == null) {
			return "login";
		}
		return "qa";
	}
	
	@RequestMapping(value = "/qa/expand_iframe")
	public String qaexpand() {
		return "main-panel-expandmap";
	}
	/********** qa 操作end ************/
	
	
	/********** ctrl 操作begin ************/
	@RequestMapping("/ctrl")
	public String ctrl(HttpServletRequest request) {
		if (request.getSession().getAttribute("uid") == null) {
			return "login";
		}
		return "ctrl";
	}
	/********** ctrl 操作end ************/
}
