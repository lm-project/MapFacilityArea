package com.autonavi.mapart.entity;

import java.io.Serializable;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * desc: 返回URL请求处理状态实体类
 * <p>
 * Copyright: Copyright(c)AutoNavi 2014
 * </p>
 * 
 * @author <a href="mailTo:i-caiqiang@autonavi.com">i-caiqiang</a>
 * @time 2014-4-25 9:06
 * 
 */
public class ResponseStatus implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4538483660305918915L;
	private int code;			//请求URL状态码
	private String restring;	//请求URL返回值
	private String description;	//请求URL状态描述
	
	public ResponseStatus(){
		
	}
	
	public ResponseStatus(int code, String description){
		this.code = code;
		this.description = description;
	}
	
	/**
	 * 
	 * @param code 返回状态码 100-正确 200-异常
	 * @param restring 返回参数
	 * @param description	返回状态描述
	 */
	public ResponseStatus(int code, String restring, String description){
		this.code = code;
		this.restring = restring;
		this.description = description;
	}
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getRestring() {
		return restring;
	}

	public void setRestring(String restring) {
		this.restring = restring;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
	@Override
	public String toString() {
		return getCode()+","+getDescription()+","+getRestring();
	}
}
