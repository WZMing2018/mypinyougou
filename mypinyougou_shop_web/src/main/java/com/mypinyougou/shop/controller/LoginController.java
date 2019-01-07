package com.mypinyougou.shop.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/login")
public class LoginController {

	@RequestMapping("/loginInfo")
	public Map<String, String> loginInfo(HttpServletRequest request){

		HashMap<String, String> loginInfoMap = new HashMap<>();

		//获取SpringSecurity上下文中->认证信息->当前用户->登录名
		String loginName = SecurityContextHolder.getContext().getAuthentication().getName();
		loginInfoMap.put("loginName", loginName);
		//获取登录时间
		/*String loginTime = (String) request.getSession().getAttribute("loginTime");
		loginInfoMap.put("loginTime", loginTime);*/

		return loginInfoMap;
	}

}
