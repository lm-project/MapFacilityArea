package com.autonavi.mapart.entity;

public class QACityNumber {
	private String taskname;  	// 项目名称
	private String city;		// 涉及城市
	private int total;			// 质检作业项总数量
	private int received;		// 已领取质检作业项数量
	
	
	public String getTaskname() {
		return taskname;
	}
	public void setTaskname(String taskname) {
		this.taskname = taskname;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getReceived() {
		return received;
	}
	public void setReceived(int received) {
		this.received = received;
	}
	
}
