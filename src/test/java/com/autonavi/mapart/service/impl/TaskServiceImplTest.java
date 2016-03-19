package com.autonavi.mapart.service.impl;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import junit.framework.Assert;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.autonavi.mapart.entity.DaoResult;
import com.autonavi.mapart.entity.SetGetTaskListForm;
import com.autonavi.mapart.entity.Task;
import com.autonavi.mapart.service.BaseTestCase;
import com.autonavi.mapart.service.TaskService;
import com.autonavi.mapart.service.TaskitemService;
import com.autonavi.mapart.util.DateFormat;

public class TaskServiceImplTest extends BaseTestCase {

	@Autowired
	private TaskService taskService;

	@Autowired
	private TaskitemService taskitemService;

	@Test
	public void testGetTaskById() {
		// Columns: id, name, remark, createtime, file_name, user_id, status, commit_time>
		Task task = taskService.getTaskById(555);
		Assert.assertEquals(555, task.getId());
		// Row: 555, 小区类型设施区域互查——王香香, null, 2014-08-12 14:21:52, d:/cq/fileio/upload/20140812142151_任务分配清单.xlsx, 1, 0, null>
	}

	/*@Test
	public void testGetTaskByIds() {
		fail("Not yet implemented");
	}*/

	@Test
	public void testInsertTask() {
		Task task = new Task("测试——yyy", "", "d:/cq/fileio/upload/20140814142451_taskdemo.xlsx", 1, "测试", 0);
		taskService.insertTask(task);
	}

	@Test
	public void testInsertQaTask() {
		int uid = 45;
		String truename = "yyy";
		String projectname = "小区类型设施区域互查";
		String city = "北京";
		int number = 50;
		String taskname = projectname + "—" + city + "—" + number + "—"
				+ truename;
		String remark = "";
		String filename = "";
		int tasktype = 1;
		Task task = new Task(taskname, remark, filename, uid, projectname,
				tasktype);
		// 创建质检任务
		int taskid = taskService.insertTask(task);
		// 更新作业项的被质检领取后的记录
		taskitemService.QAreceive(taskid, taskname, uid, projectname, city,
				number);
	}

	@Test
	public void testInsertTasks() {
		String path = "src/test/resources/data/taskdemo.xlsx";
		File file = new File(path);
		if (file.exists()) {
			System.out.println(file.getAbsolutePath());
		}
		try {
			DaoResult result = taskService.insertTasks(path, "", 1);
			System.out.println(result.getRemark());
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

	@Test
	public void testUpdateTask() {
		// Task task = new Task(11, "test1更新", "更新测试");
		Task task = new Task(1, "测试");
		taskService.updateTask(task);
	}

	@Test
	public void testDeleteTaskByIds() {
		taskService.deleteTaskByIds("581");
	}
	
	
	@Test
	public void testDeleteTaskByProjectname() {
		String  pros = "小区类型设施区域互查";
		taskService.deleteTaskByProjectname(pros);
	}
	

	@Test
	public void testGetTaskListForTaskList() {
		SetGetTaskListForm form = new SetGetTaskListForm(1, 23,0, -1,
				"", -1, 2);
		DaoResult result = taskService.getTaskList(form);
		List<Object> list = (List<Object>) result.getList();
		System.out.println(list.get(0).toString());
		assertEquals(8, list.size());
	}

	@Test
	public void testGetTaskListForProjectList() {
//		SetGetTaskListForm form = new SetGetTaskListForm(1, 23, 0, -1,
//				"", -1, 1);
		SetGetTaskListForm form = new SetGetTaskListForm(1, 23,0, 1,
				"", -1, 1);
		DaoResult result = taskService.getTaskList(form);
		List<Object> list = (List<Object>) result.getList();
		assertEquals(3, list.size());
	}
	@Test
	public void testGetTaskListByGet() {
		DaoResult result = taskService.getTaskListByGet();
		List<Object> list = (List<Object>) result.getList();
		System.out.println(list.get(0).toString());
		assertEquals(6, list.size());
	}

	@Test
	public void testGetProjectByName() {
		String projectname = "小区设施区域编辑";
		Task task = taskService.getProjectByName(projectname);
		String filePath = task.getFilename();
		if (filePath.indexOf("/") > -1 && (filePath.indexOf("_") > -1)) {
			String file = filePath.substring( filePath.lastIndexOf("/")+1, filePath.length() );
			String time = file.split("_")[0];
			String filename = file.split("_")[1];
			task.setCreatetime( DateFormat.StringToDate(time) );
			task.setFilename(filename);
		}
		JsonConfig jsonConfig = new JsonConfig();  
		jsonConfig.setExcludes(new String[] { "id", "list", "name", "tasktype"});
		JSONObject object = JSONObject.fromObject(task, jsonConfig);
		System.out.println(object.toString());
		Assert.assertEquals(projectname, task.getProjectname());
	}

	@Test
	public void testFindCheckTasks() {
		List<Task> tasks = taskService.findCheckTasks(12);
		assertEquals(0, tasks.size());
		assertEquals("", JSONArray.fromObject(tasks).toString());
	}

	@Test
	public void testGetUnsaveCount() {
		String projectname = "小区设施区域编辑";
		int count = taskService.getUnsaveCount(projectname);
		Assert.assertEquals(50, count);
	}

	@Test
	public void testCommit() {
		String projectname = "小区设施区域";
		int tasktype = 0; // 任务类型：作业
		int status = 1; // 任务状态：提交(1)
		taskService.commit(projectname, "", tasktype, status);
	}

}
