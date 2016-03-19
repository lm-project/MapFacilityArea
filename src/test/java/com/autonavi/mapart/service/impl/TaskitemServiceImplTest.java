package com.autonavi.mapart.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.TreeBag;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.autonavi.mapart.entity.DaoResult;
import com.autonavi.mapart.entity.QACityNumber;
import com.autonavi.mapart.entity.Taskitem;
import com.autonavi.mapart.service.BaseTestCase;
import com.autonavi.mapart.service.TaskitemService;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:service-context-test.xml")
public class TaskitemServiceImplTest  {

	@Autowired
	private TaskitemService taskitemService;

	/**
	 * 新增作业项测试
	 */
	@Test
	public void testInsertTaskitem() {

		Taskitem taskitem = new Taskitem(576, "FA3110000xxx", "", "J50F001020",
				"风景线", "116.3", "39.2", "北京", "3310", "新增", 1);
		List<Taskitem> list = new ArrayList<Taskitem>();
		list.add(taskitem);
		taskitemService.insertTaskitem(list);
	}

	/**
	 * 更新作业项测试
	 */
	@Test
	public void testUpdateTaskitem() {

		Taskitem t = new Taskitem();
		t.setStatus(1);

		t.setId(740);
		taskitemService.updateTaskitem(t);
	}

	/**
	 * 批量删除作业项测试
	 */
	@Test
	public void testDeleteTaskitemByIds() {
		int idType = 1; // 参数ids类型：0-所属任务id，1-作业项id
		String ids = "15177";
		taskitemService.deleteTaskitemByIds(ids, idType);
	}

	/**
	 * 分页查询作业项测试
	 */
	@Test
	public void testGetTaskitemListByPage() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pageIndex", 1);
		map.put("recordCountPerPage", 9);
		map.put("taskid", 576);
		map.put("task_name", "");

		HashMap<String, Object> paras = (HashMap<String, Object>) taskitemService
				.getTaskitemListByPage(map);
		@SuppressWarnings("unchecked")
		List<Taskitem> list = (List<Taskitem>) paras.get("taskitems");
		System.out.println(list.get(0).getName());
		Assert.assertEquals(9, list.size());
	}

	/**
	 * 根据选择的任务id，查询相应作业项列表(作业员“开始作业”时加载)
	 */
	@Test
	public void testGetTaskByTaskids() {
		String ids = "563"; // 任务id
		String uid = "2";
		List<Taskitem> items = taskitemService.getTaskByTaskids(ids, uid);
		for (Taskitem i : items) {
			System.out.println(i);
		}
	}

	/**
	 * 作业员领取任务测试
	 */
	@Test
	public void testReceive() {
		String taskids = "586"; // 任务id
		String taskitemids = ""; // 作业项id
		int userId = 2; // 用户id
		taskitemService.receive(taskids, userId);
	}

	/**
	 * 获取已领取的作业项 测试
	 */
	@Test
	public void testGetReceive() {

		int status = 1; // 状态为已领取
		int uid = 2; // 领取的用户id
		DaoResult rs = taskitemService.getReceive(1, 1, "小区设施区域编辑——许洪波", "",
				status, uid);
		Assert.assertEquals(rs.getList().size(), 0);
	}

	/**
	 * 获取未领取的作业项 测试
	 */
	@Test
	public void getUnReceived() {
		int userId = 0;
		int status = userId;
		DaoResult rs = taskitemService
				.getReceive(0, -1, "", "", status, userId);
		System.out.println(((Taskitem) rs.getList().get(0)).getTaskname() + ","
				+ rs.getTotalCount());
		Assert.assertEquals(rs.getList().size(), 5);
	}

	/**
	 * 回收作业项测试
	 */
	@Test
	public void testRecover() {

		// 按任务回收
		// String taskIds = "586";
		// String ids = "";

		// 单个作业项回收
		String taskIds = "585";
		String ids = "15176";

		taskitemService.recover(taskIds, ids);
	}

	/**
	 * 作业项提交测试
	 */
	@Test
	public void testCommit() {
		String projectname = "小区设施区域";
		int tasktype = 0; // 任务类型：作业
		int taskitemStatus = 3; // 作业项状态：提交(3)
		taskitemService.commit(projectname, "", tasktype, taskitemStatus);
	}

	/**
	 * 获取质检员已领取作业项
	 */
	@Test
	public void testGetTaskitemsByQaTaskids() {
		String taskids = "691"; // 质检任务id
		String userid = "34"; // 质检员id
		List<Taskitem> items = taskitemService.getTaskitemsByQaTaskids(taskids,
				userid);
		Assert.assertFalse(items.isEmpty());
	}

	/**
	 * 质检项领取测试
	 */
	@Test
	public void testQAreceive() {
		// Task task = new Task("小区类型设施区域互查——北京市——500" , "", "", 1, 1);
		// int taskid = taskService.insertTask(task);
		int taskid = 232; // 质检任务id
		String qataskname = "小区类型设施区域互查——北京市——500"; // 质检任务名称
		int userId = 1; // 质检员id
		String projectname = "小区类型设施区域互查";
		String city = "北京市"; // 城市
		int number = 500; // 领取数量
		taskitemService.QAreceive(taskid, qataskname, userId, projectname,
				city, number);
//		TaskitemServiceImplTest test1 = new TaskitemServiceImplTest();
//		TaskitemServiceImplTest.Test1 test12 = test1.new Test1(taskid,
//				qataskname, userId, projectname, city, number);
//		for (int i = 0; i < 1; i++) {
//			new Thread(test12).run();
//		}
	}

	/*
	 * @Test public void testGetUnQAReceived() { fail("Not yet implemented"); }
	 * 
	 * @Test public void testGetQAReceived() { fail("Not yet implemented"); }
	 */

	/**
	 * 查询项目中作业项涉及城市
	 */
	@Test
	public void testFindCitysByProjectname() {
		String projectname = "小区类型设施区域互查";
		DaoResult result = taskitemService.findCitysByProjectname(projectname);
		System.out.println((((QACityNumber) result.getList().get(0))
				.getReceived()));
	}

	/**
	 * 查询所有作业项
	 */
	@Test
	public void testGetAll() {
		Collection<Taskitem> all = taskitemService.getAll();
		Assert.assertEquals(151, all.size());
	}

	public class Test1 implements Runnable {

		int taskid;
		String qataskname;
		int userId;
		String projectname;
		String city;
		int number;

		public Test1() {
		}

		public Test1(int taskid, String qataskname, int userId,
				String projectname, String city, int number) {
			this.taskid = taskid;
			this.qataskname = qataskname;
			this.userId = userId;
			this.projectname = projectname;
			this.city = city;
			this.number = number;
		}

		public void run() {
			taskitemService.QAreceive(taskid, qataskname, userId, projectname,
					city, number);
		}
	}
}
