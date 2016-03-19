package com.autonavi.mapart.service.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.autonavi.mapart.entity.DaoResult;
import com.autonavi.mapart.entity.User;
import com.autonavi.mapart.service.BaseTestCase;
import com.autonavi.mapart.service.UserService;

public class UserServiceImplTest extends BaseTestCase {

	@Autowired
	private UserService userService;

	@Test
	public void testGetUserLogin() {
		User user = new User();
		user.setUsername("cqlala1");
		user.setPassword("123456");
		User user1 = userService.getUserLogin(user);
		System.out.println(user1.getAuthority());
		Assert.assertEquals(7, user1.getUid());
	}

	@Test
	public void testInsertOrupdateUser() {
		try {
			User user = new User("cqlala", "666666", "蔡强",
					"qiang.cai@autonavi.com", 1);
			userService.insertOrupdateUser(user, 0);
		} catch (Exception ex) {
			Assert.assertTrue(ex.getMessage().contains("重复键违反唯一约束"));
		}
	}

	@Test
	@Transactional
	public void testDeleteUser() {
		userService.deleteUser("21,22");
		// fail("Not yet implemented");
	}

	@Test
	public void testGetUserList() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pageIndex", 1);
		map.put("recordCountPerPage", 9);
		map.put("role", 0);
		map.put("truename", "");
		DaoResult result = userService.getUserList(map);
		List<Object> list = result.getList();
		System.out.println("用户数量："+list.size());
		Assert.assertEquals(3, list.size());
	}

	@Test
	public void testGetUserByEmail() {
		User user = new User();
		user.setEmail("42432");
		user = userService.getUser(user);
		System.out.println(user.getUid());
		Assert.assertEquals("cqlala1", user.getUsername());
	}

}
