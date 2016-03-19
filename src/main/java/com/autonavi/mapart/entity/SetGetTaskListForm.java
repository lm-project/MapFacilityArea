package com.autonavi.mapart.entity;

import java.util.HashMap;
import java.util.Map;


public class SetGetTaskListForm {
	
	private int page; 				// 页数
	private int recordCountPerPage;	// 页查询条数
	private int tasktype;			// 任务类别 0 --普通任务   1--质检任务
	private int status ;			// 任务状态
	private String name;			// 查询关键字
	private int uid;				// 用户ID
	private int type;				// 查询内容   1- 查询项目  2-查询任务项
	
	public SetGetTaskListForm(){
		
	}
	
	public Map<String, Object> getMap(int pag, int recordCountPerPag, int statu, String projectname) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pageIndex", pag);
		map.put("recordCountPerPage", recordCountPerPag);
		map.put("status", statu);
		map.put("projectname", projectname);
		return map;
	}
	
	/**
	 * 
	 * @param page 页数
	 * @param recordCountPerPage 页查询条数
	 * @param status 任务状态0 --普通任务   1--质检任务
	 * @param name 查询关键字
	 * @param uid 用户ID
	 * @param type 查询内容   1- 查询项目  2-查询任务项
	 * page, 9, 0, 1, projectname, -1, 1
	 */
	
	public SetGetTaskListForm(int page, int recordCountPerPage, int tasktype,
			int status, String name, int uid, int type) {
		super();
		this.page = page;
		this.recordCountPerPage = recordCountPerPage;
		this.tasktype = tasktype;
		this.status = status;
		this.name = name;
		this.uid = uid;
		this.type = type;
	}
//	public SetGetTaskListForm(int page, int recordCountPerPage,
//			int status, String name, int uid, int type) {
//		super();
//		this.page = page;
//		this.recordCountPerPage = recordCountPerPage;
//		this.status = status;
//		this.name = name;
//		this.uid = uid;
//		this.type = type;
//	}
	
	public Map<String, Object> getForm() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pageIndex", page);
		map.put("recordCountPerPage", recordCountPerPage);
		map.put("tasktype", tasktype);
		map.put("status", status);
		map.put("projectname", name);
		map.put("uid", uid);
		
		
		if (type == 1) { // 查询任务所属项目
			map.put("selectType", "selectProject");
		} else if (type == 2) { // 查询任务
			map.put("selectType", "selectTask");
		}
		
		
		return map;
		
	}
	
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRecordCountPerPage() {
		return recordCountPerPage;
	}
	public void setRecordCountPerPage(int recordCountPerPage) {
		this.recordCountPerPage = recordCountPerPage;
	}
	public int getTasktype() {
		return tasktype;
	}
	public void setTasktype(int tasktype) {
		this.tasktype = tasktype;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	
}
