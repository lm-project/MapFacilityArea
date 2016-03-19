package com.autonavi.mapart.entity;

import java.util.List;

public class DaoResult {
	private List<Object> list;
	private int totalCount;
	private int statusCode;
	private String remark;

	public DaoResult() {

	}
	

	public DaoResult(List<Object> list, int totalCount) {
		super();
		this.list = list;
		this.totalCount = totalCount;
	}
	
	public DaoResult(List<Object> list, int totalCount, int statusCode, String remark) {
		super();
		this.list = list;
		this.totalCount = totalCount;
		this.statusCode = statusCode;
		this.remark = remark;
	}
	
	public DaoResult(int statusCode, String remark) {
		super();
		this.statusCode = statusCode;
		this.remark = remark;
	}

	public List<Object> getList() {
		return list;
	}

	public void setList(List<Object> list) {
		this.list = list;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

}
