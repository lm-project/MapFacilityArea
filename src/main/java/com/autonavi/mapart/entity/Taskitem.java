package com.autonavi.mapart.entity;


/**
 * <p>
 * Title:
 * </p>
 * <p>
 * desc: 作业项实体类
 * <p>
 * Copyright: Copyright(c)AutoNavi 2014
 * </p>
 * 
 * @author <a href="mailTo:i-caiqiang@autonavi.com">i-caiqiang</a>
 * @time 2014-4-25 9:24
 * 
 */
public class Taskitem {

	private int id; 				// 任务项id
	private int taskid; 			// 所属任务id，关联task表id
	private String taskname; 		// 所属任务项名称
	private String name; 			// 任务项名称
	private String x_coord; 		// 经度
	private String y_coord; 		// 维度
	private String city; 			// 城市
	private String processtype ;  	// 处理类型
	private int    status ; 		// 任务项状态,0:作业未领取,1:作业已领取,2:作业已完成,3:作业已提交,4:质检已领取，5：已质检已完成，6：质检已提交，7：质检未通过，8：质检已通过（可以发布了）任务项状态, 0:作业项未领取, 1:作业项已领取, 2:作业项已保存, 3:作业项已提交, 4:质检项未领取, 5:质检项已领取  5:质检项已质检
	private String receivetime; 	// 领取任务项时间
	private int    userid; 			// 作业员id
	private String fadeback ; 		// 质检反馈
    private String committime;		// 提交时间
    private int    qataskid;        // 质检任务id，关联task表id
    private int    qauserid;        // 质检员用户id，关联user表id
    private String qataskname;      // 质检任务名称
	private int del_status;         // 任务项删除状态，0未删除，1已删除
    private Task task;				//所属任务项
    
    private int opuserid;				//所属任务项
    private String optime;				//所属任务项
    
    public int getOpuserid() {
		return opuserid;
	}

	public void setOpuserid(int opuserid) {
		this.opuserid = opuserid;
	}

	public String getOptime() {
		return optime;
	}

	public void setOptime(String optime) {
		this.optime = optime;
	}

	private int total;				//任务项数量
    
    public Taskitem() {
    	
    }
    
    /**
     * 
     * @param taskid 所属任务id，关联task表id
     * @param pcsid	运营id
     * @param mesh 图幅号
     * @param name 任务项名称
     * @param x_coord 经度
     * @param y_coord 维度
     * @param city 城市
     * @param typecode 类型编码
     * @param processtype 处理类型
     * @param userid 作业员id
     */
	public Taskitem(int taskid, String number, String pcsid, String mesh, String name,
			String x_coord, String y_coord, String city, String typecode,
			String processtype, int userid) {
		super();
		this.taskid = taskid;
		this.name = name;
		this.x_coord = x_coord;
		this.y_coord = y_coord;
		this.city = city;
		this.processtype = processtype;
		this.userid = userid;
	}


	public int getDel_status() {
		return del_status;
	}

	public void setDel_status(int del_status) {
		this.del_status = del_status;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTaskid() {
		return taskid;
	}
	public void setTaskid(int taskid) {
		this.taskid = taskid;
	}
	public String getTaskname() {
		return taskname;
	}
	public void setTaskname(String taskname) {
		this.taskname = taskname;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getX_coord() {
		return x_coord;
	}
	public void setX_coord(String x_coord) {
		this.x_coord = x_coord;
	}
	public String getY_coord() {
		return y_coord;
	}
	public void setY_coord(String y_coord) {
		this.y_coord = y_coord;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getProcesstype() {
		return processtype;
	}
	public void setProcesstype(String processtype) {
		this.processtype = processtype;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getReceivetime() {
		return receivetime;
	}
	public void setReceivetime(String receivetime) {
		this.receivetime = receivetime;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getFadeback() {
		return fadeback;
	}
	public void setFadeback(String fadeback) {
		this.fadeback = fadeback;
	}
	public String getCommittime() {
		return committime;
	}
	public void setCommittime(String committime) {
		this.committime = committime;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}
	
	public int getQataskid() {
		return qataskid;
	}

	public void setQataskid(int qataskid) {
		this.qataskid = qataskid;
	}

	public int getQauserid() {
		return qauserid;
	}

	public void setQauserid(int qauserid) {
		this.qauserid = qauserid;
	}

	public String getQataskname() {
		return qataskname;
	}

	public void setQataskname(String qataskname) {
		this.qataskname = qataskname;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "Taskitem [id=" + id + ", taskid=" + taskid + ", taskname=" + taskname + ", "
				+ " name=" + name + ", x_coord=" + x_coord + ", y_coord="
				+ y_coord + ", city=" + city + ", , processtype=" + processtype + ", status="
				+ status + ", receivetime=" + receivetime + ", userid=" + userid + ", fadeback=" + fadeback
				+ ", committime=" + committime + ", task=" + task + "]";
	}
	
//	@Override
//	public String toString() {
//		return "Taskitem [id=" + id + ", taskid=" + taskid + ", taskname=" + taskname + ", number=" + number
//				+ ", pcsid=" + pcsid + ", mesh=" + mesh + ", name=" + name + ", x_coord=" + x_coord + ", y_coord="
//				+ y_coord + ", city=" + city + ", typecode=" + typecode + ", processtype=" + processtype + ", status="
//				+ status + ", receivetime=" + receivetime + ", userid=" + userid + ", fadeback=" + fadeback
//				+ ", committime=" + committime + ", task=" + task + "]";
//	}
	
	
}
