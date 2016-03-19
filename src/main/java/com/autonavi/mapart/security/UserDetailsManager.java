package com.autonavi.mapart.security;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.autonavi.mapart.entity.User;
import com.autonavi.mapart.orm.UserDao;

@Service("UserDetailService")
public class UserDetailsManager implements UserDetailsService {

	/**
	 * Logger for this class
	 */
	private Logger log = Logger.getLogger(UserDetailsManager.class);
	@Autowired
	private UserDao userDao;

	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {
		if (log.isDebugEnabled()) {
			log.debug("loadUserByUsername(String) - start"); //$NON-NLS-1$  
		}
		Collection<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
		String password = null;
		// 取得用户的密码
		User user = new User();
		user.setUsername(username);
		user = userDao.getUserByName(user);
		if (user == null) {
			String message = "用户" + username + "不存在";
			log.error(message);
			throw new UsernameNotFoundException(message);
		}
		password = user.getPassword();
		String uname = user.getUsername();
		String emile = user.getEmail();
		int role = user.getRole();
		int uid = user.getUid();
		String truename = user.getTruename();
		String str = uid + "," + uname + "," + truename + "," + role + ","
				+ emile;
		GrantedAuthorityImpl grantedAuthorityImpl1 = new GrantedAuthorityImpl(
				user.getAuthority().split(",")[0]);
		GrantedAuthorityImpl grantedAuthorityImpl2 = new GrantedAuthorityImpl(
				user.getAuthority().split(",")[1]);
		
		if (log.isDebugEnabled()) {
			log.debug("用户：[" + user.getUsername() + "]拥有角色：[" + user.getAuthority()
					+ "],即spring security中的access");
		}
		auths.add(grantedAuthorityImpl1);
		auths.add(grantedAuthorityImpl2);

		org.springframework.security.core.userdetails.User user1 = new org.springframework.security.core.userdetails.User(
				str, password, true, true, true, true, auths);

		if (log.isDebugEnabled()) {
			log.debug("loadUserByUsername(String) - end"); //$NON-NLS-1$  
		}
		return user1;
	}

}
