package com.autonavi.mapart.entity;
/**
 * <p>
 * Title:
 * </p>
 * <p>
 * desc: 返回任务和作业项状态实体类
 * <p>
 * Copyright: Copyright(c)AutoNavi 2014
 * </p>
 * 
 * @author <a href="mailTo:qiang.cai@autonavi.com">caiqiang</a>
 * @time 2014-7-30 13:25
 * 
 */
public class TTStatus {
	// TT ---  task and taskitem
	
	//作业员 ——》 taskitem
	public static final int UNRECEIVE = 0; 	// 未领取
    public static final int RECEIVE = 1;	// 已领取
    public static final int SAVE = 2;		// 已保存
    
    //管理员  ——》  task and taskitem
    public static final int COMMIT = 3;		// 已提交
    
    
    public static final int QA_RECEIVED = 4;			// 质检已领取
    
    public static final int QA_PASSED = 5; //质检通过
    public static final int QA_FAIL_PASS = 6; //质检未通过
    
}
