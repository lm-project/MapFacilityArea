package com.autonavi.mapart.service;

import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.autonavi.mapart.entity.DaoResult;
import com.autonavi.mapart.entity.User;

public interface UserService {
	
	/**
	 * <p>
	 * desc: 用户登录实现
	 * <p>
	 * 
	 * @param userName
	 * @param password
	 * @return User
	 * @throws DataAccessException
	 */
	User getUserLogin(User user) throws DataAccessException;

	/**
	 * <p>
	 * desc: 关键字获取用户
	 * <p>
	 * 
	 * @return User
	 * @throws DataAccessException
	 */
	User getUser(User user) throws DataAccessException;
	
	
	/**
	 * <p>
	 * desc: 新增或修改用户实现
	 * <p>
	 * 
	 * @param user
	 * @param type 0-insert 1-update
	 * @throws DataAccessException
	 */
	void insertOrupdateUser(User user, int type) throws DataAccessException;
	
	/**
	 * <p>
	 * desc: 删除用户实现
	 * <p>
	 * 
	 * @return List
	 * @throws DataAccessException
	 */
	void deleteUser(String uids) throws DataAccessException;
	
	/**
	 * <p>
	 * desc: 查询用户实现
	 * <p>
	 * @param params
	 * @return
	 * @throws DataAccessException
	 */
	public DaoResult getUserList(Map<String, Object> params) throws DataAccessException;
		
}
