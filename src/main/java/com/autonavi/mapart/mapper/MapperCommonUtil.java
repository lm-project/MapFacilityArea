package com.autonavi.mapart.mapper;

import javax.servlet.http.HttpServletRequest;

public class MapperCommonUtil {

	/**
	 * 获取session中用户id
	 * 
	 * @param request
	 * @return
	 */
	public static int getUserid(HttpServletRequest request) {
		Object object = request.getSession().getAttribute("uid");
		if (object == null) {
			return -1;
		} else {
			return Integer.valueOf(String.valueOf(object));
		}
	}
	

	/**
	 * 获取session中用户真实姓名
	 * 
	 * @param request
	 * @return
	 */
	public static String getUserTruename(HttpServletRequest request) {
		Object object = request.getSession().getAttribute("uid");
		if (object == null) {
			return null;
		} else {
			return String.valueOf(object);
		}
	}
	
	/**
	 * 获取session中用户权限  1-admin 2-operator 3-qa
	 * 
	 * @param request
	 * @return
	 */
	public static int getUserRole(HttpServletRequest request) {
		Object object = request.getSession().getAttribute("role");
		if (object == null) {
			return -1;
		} else {
			return Integer.valueOf(String.valueOf(object));
		}
	}
	
}
