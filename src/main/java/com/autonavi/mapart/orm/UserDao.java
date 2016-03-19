package com.autonavi.mapart.orm;

import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.autonavi.mapart.entity.DaoResult;
import com.autonavi.mapart.entity.User;

public interface UserDao {

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
	User getUserByName(User user) throws DataAccessException;

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
	 * desc: 判断用户邮箱是否存在实现
	 * <p>
	 * 
	 * @param email
	 * @return User
	 * @throws DataAccessException
	 */
	User getUserByEmail(String email) throws DataAccessException;

	/**
	 * <p>
	 * desc: 新增用户
	 * <p>
	 * 
	 * @param user
	 * @throws DataAccessException
	 */
	void insertUser(User user) throws DataAccessException;
	
	/**
	 * <p>
	 * desc: 修改用户
	 * <p>
	 * 
	 * @param user
	 * @throws DataAccessException
	 */
	void updateUser(User user) throws DataAccessException;
	
	
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
	 * 
	 * @return Map<String, Object>
	 * @throws DataAccessException
	 */
	
	/**
	 * <p>
	 * desc: 查询用户实现
	 * <p>
	 * @param params
	 * @return
	 * @throws DataAccessException
	 */
	DaoResult getUserList(Map<String, Object> params) throws DataAccessException;
}
