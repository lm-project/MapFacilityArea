package com.autonavi.mapart.orm.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.autonavi.mapart.entity.DaoResult;
import com.autonavi.mapart.entity.User;
import com.autonavi.mapart.orm.UserDao;

/**
 * <p>
 * desc:用户处理实现类
 * <p>
 * Copyright: Copyright(c)AutoNavi 2014
 * </p>
 * 
 * @author <a href="mailTo:i-caiqiang@autonavi.com">i-caiqiang</a>
 * @time 2014-4-24 10:33
 * 
 */
public class UserDaoImpl extends BasicMyBatisDao implements UserDao {

	@Override
	public User getUserByName(User user) throws DataAccessException {
		return super.get("userMapper.getUserByName", user);
	}

	@Override
	public User getUserLogin(User user) throws DataAccessException {
		return super.get("userMapper.loginByQuery", user);
	}

	@Override
	public User getUser(User user) throws DataAccessException {
		return super.get("userMapper.selectById", user);
	}

	@Override
	public User getUserByEmail(String email) throws DataAccessException {
		return null;
	}

	@Override
	public void insertUser(User user) throws DataAccessException {
		super.save("userMapper.insert", user);

	}

	@Override
	public void updateUser(User user) throws DataAccessException {
		super.save("userMapper.update", user);
	}

	@Override
	public DaoResult getUserList(Map<String, Object> params)
			throws DataAccessException {
		@SuppressWarnings("unused")
		HashMap<String, Object> paras = new HashMap<String, Object>();
		super.get("userMapper.selectByPage", params);
		@SuppressWarnings("unchecked")
		List<Object> users = (List<Object>) params.get("curResult");
		int allRecordCount = (Integer) params.get("allRecordCount");
		return new DaoResult(users, allRecordCount);
	}

	@Override
	public void deleteUser(String uids) throws DataAccessException {
		String[] arrays = uids.split(",");
		int array[] = new int[arrays.length];
		for (int i = 0; i < arrays.length; i++) {
			array[i] = Integer.parseInt(arrays[i]);
		}
		super.delete("userMapper.delete", array);

	}

}
