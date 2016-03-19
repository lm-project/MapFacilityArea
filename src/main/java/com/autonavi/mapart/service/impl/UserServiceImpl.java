package com.autonavi.mapart.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.autonavi.mapart.entity.DaoResult;
import com.autonavi.mapart.entity.User;
import com.autonavi.mapart.orm.UserDao;
import com.autonavi.mapart.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDao userDao;
	
	@Override
	public User getUserLogin(User user) throws DataAccessException {
		return userDao.getUserLogin(user);
	}

	@Override
	public User getUser(User user) throws DataAccessException {
		return userDao.getUser(user);
	}
	
	@Override
	public void insertOrupdateUser(User user, int type)
			throws DataAccessException {
			if (type == 0) {
				userDao.insertUser(user);
			}else {
				userDao.updateUser(user);
			}
	}

	@Override
	public void deleteUser(String uids) throws DataAccessException {
		
		
	}

	@Override
	public DaoResult getUserList(Map<String, Object> params)
			throws DataAccessException {
		return userDao.getUserList(params);
	}

}
