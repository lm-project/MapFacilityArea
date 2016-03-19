package com.autonavi.mapart.entity;

import java.util.List;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * desc: 任务实体类
 * <p>
 * Copyright: Copyright(c)AutoNavi 2014
 * </p>
 * 
 * @author <a href="mailTo:i-caiqiang@autonavi.com">i-caiqiang</a>
 * @time 2014-4-25 9:06
 * 
 */
public class Task {
	private int    id; 				//	表id
	private String name; 			//	任务名称
	private String remark; 			// 	任务描述
	private String createtime; 		// 	创建时间
	private String filename; 		//	任务清单文件名
	private int    userid; 			// 	创建者用户ID
	private int    status; 			// 	任务状态, 0:未提交, 1:已提交
	private String committime; 		// 	提交时间
	private String projectname;     //  项目名称
	private int    tasktype;        //  任务类型，0:作业任务，1:质检任务
	
	private List<Taskitem> list; 	// 	任务下子项
	
	
	private String userinformation; 	//  相关用户信息
	
    static final int UNCOMMIT = 0;	// 	未提交
    static final int COMMIT = 1;	// 	已提交
	
	public Task() {
		
	}
	
	/**
	 * 新增任务项
	 * @param name 任务名称
	 * @param remark 任务描述
	 * @param filename 任务清单文件名
	 * @param userid 创建者用户ID
	 * @param projectname 所属项目名称
	 * @param tasktype 项目类型 0-作业 1-质检
	 */
	public Task(String name, String remark, String filename, int userid, String projectname, int tasktype) {
		super();
		this.name = name;
		this.remark = remark;
		this.filename = filename;
		this.userid = userid;
		this.projectname = projectname;
		this.tasktype = tasktype;
	}
	
	/**
	 * 更新任务项
	 * @param projectname 项目名称
	 */
	public Task( int status, String projectname) {
		super();
		this.status = status;
		this.projectname = projectname;
	}
	
	/**
	 * 更新任务项
	 * @param name 任务名称
	 * @param remark 任务描述
	 * @param filename 任务清单文件名
	 * @param userid 创建者用户ID
	 */
	public Task(int id, String name, String remark) {
		super();
		this.id = id;
		this.name = name;
		this.remark = remark;
	}
	
	/**
	 * 上传返回任务项
	 * @param name 任务名称
	 * @param remark 任务描述
	 * @param filename 任务清单文件名
	 * @param userid 创建者用户ID
	 */
	public Task(String name, String projectname, int id) {
		super();
		this.id = id;
		this.name = name;
		this.projectname = projectname;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCommittime() {
		return committime;
	}

	public void setCommittime(String committime) {
		this.committime = committime;
	}

	public List<Taskitem> getList() {
		return list;
	}

	public void setList(List<Taskitem> list) {
		this.list = list;
	}


	public String getProjectname() {
		return projectname;
	}


	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}


	public int getTasktype() {
		return tasktype;
	}


	public void setTasktype(int tasktype) {
		this.tasktype = tasktype;
	}

	public String getUserinformation() {
		return userinformation;
	}

	public void setUserinformation(String userinformation) {
		this.userinformation = userinformation;
	}

	public Task(int id, String name, String remark, String createtime,
			String filename, int userid, int status, String committime,
			String projectname, int tasktype, List<Taskitem> list,
			String userinformation) {
		super();
		this.id = id;
		this.name = name;
		this.remark = remark;
		this.createtime = createtime;
		this.filename = filename;
		this.userid = userid;
		this.status = status;
		this.committime = committime;
		this.projectname = projectname;
		this.tasktype = tasktype;
		this.list = list;
		this.userinformation = userinformation;
	}

	
}
