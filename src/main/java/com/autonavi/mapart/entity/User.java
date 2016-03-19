package com.autonavi.mapart.entity;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * desc: 用户实体类
 * <p>
 * Copyright: Copyright(c)AutoNavi 2014
 * </p>
 * 
 * @author <a href="mailTo:i-caiqiang@autonavi.com">i-caiqiang</a>
 * @time 2014-4-25 10:25
 * 
 */
public class User {
	private int    uid;
	private String username;
	private String password;
	private String truename;
	private String email;
	private int    role;
	private String authority;
	public User(){
		
	}
	
	
	public User(int uid) {
		super();
		this.uid = uid;
	}


	public User(String truename){
		this.truename = truename;
	}
	
	public User(String username, String password){
		this.username = username;
		this.password = password;
	}

	public User(String username, String password, String truename, String email, int role){
		this.username = username;
		this.password = password;
		this.truename = truename;
		this.email = email;
		this.role = role;
	}
	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public String getTruename() {
		return truename;
	}

	public void setTruename(String truename) {
		this.truename = truename;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	
}
