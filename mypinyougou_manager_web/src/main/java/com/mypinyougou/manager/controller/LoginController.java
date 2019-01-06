package com.mypinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mypinyougou.entity.PageResult;
import com.mypinyougou.entity.Result;
import com.mypinyougou.pojo.TbTypeTemplate;
import com.mypinyougou.sellergoods.service.TypeTemplateService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/login")
public class LoginController {

	@RequestMapping("/name")
	public Map<String, String> loginName(){
		//获取SpringSecurity上下文中->认证信息->当前用户->登录名
		String loginName = SecurityContextHolder.getContext().getAuthentication().getName();

		HashMap<String, String> loginNameMap = new HashMap<>();
		loginNameMap.put("loginName", loginName);

		return loginNameMap;
	}
	

}
