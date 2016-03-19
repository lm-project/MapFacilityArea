package com.autonavi.mapart.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.autonavi.mapart.entity.DaoResult;
import com.autonavi.mapart.entity.SetGetTaskListForm;
import com.autonavi.mapart.entity.Task;
import com.autonavi.mapart.entity.Taskitem;
import com.autonavi.mapart.orm.TaskDao;
import com.autonavi.mapart.orm.TaskitemDao;
import com.autonavi.mapart.service.TaskService;
import com.autonavi.mapart.util.ImportExcel;

@Service
public class TaskServiceImpl implements TaskService {
	private Logger log = Logger.getLogger(getClass());
	@Autowired
	private TaskDao taskDao;

	@Autowired
	private TaskitemDao taskitemDao;

	@Override
	public Task getTaskById(int id) throws DataAccessException {
		return taskDao.getTaskById(id);
	}

	@Override
	public List<Task> getTaskByIds(String ids) throws DataAccessException {
		return taskDao.getTaskByIds(ids);
	}

	@Override
	public List<Task> getTasksByProjectName(String Projectname)
			throws DataAccessException {
		return taskDao.getTasksByProjectName(Projectname);
	}
	
	@Override
	public Task getProjectByName(String projectname) throws DataAccessException {
		return taskDao.getProjectByName(projectname);
	}
	
	@Override
	public int insertTask(Task task) throws DataAccessException {
		return taskDao.insertTask(task);
	}
	/**
	 * 
	 */
	@Override
	@Transactional
	public DaoResult insertTasks(String filepath, String remark, int uid) {
		Task task = null;
		List<Task> tasklist;
		List<Object> returntasklists = new ArrayList<Object>();
		try {
			tasklist = ImportExcel.readTaskExcel(filepath);
			log.debug("upload taskPackage length :"+tasklist.size());
			for (int i = 0; i < tasklist.size(); i++) {
				task = tasklist.get(i);
				task.setRemark(remark);
				task.setUserid(uid);
				task.setFilename(filepath);
				int taskid = taskDao.insertTask(task);
				List<Taskitem> taskitemlist = tasklist.get(i).getList();
				log.debug(taskid+","+task.getName()+" have taskItem length is  :"+taskitemlist.size());
				for (Taskitem taskitem : taskitemlist) {
					taskitem.setTaskid(taskid);
				}
				taskitemDao.insertTaskitem(taskitemlist);
				returntasklists.add(new Task(task.getId(), task.getName()));
			}// end for
		} catch (Exception e) {
			e.printStackTrace();
			return new DaoResult(200, "上传文件出错，请选择正确的上传文件!");
		}
		return new DaoResult(returntasklists, returntasklists.size(), 100, "上传文件成功!");
	}

	@Override
	public int updateTask(Task task) throws DataAccessException {
		return taskDao.updateTask(task);
	}

	@Override
	public void deleteTaskByIds(String ids) throws DataAccessException {
		taskDao.deleteTaskByIds(ids);
	}

	@Override
	public void deleteTaskByProjectname(String pros) throws DataAccessException {
		taskDao.deleteTaskByProjectname(pros);
	}
	
	@Override
	public DaoResult getTaskList(SetGetTaskListForm form) throws DataAccessException {
		return taskDao.getTaskList( form.getForm() );
	}
	
	@Override
	public DaoResult getTaskListByStatus(Map<String, Object> map) {
		return taskDao.getTaskListByStatus(map);
	}
	
	@Override
	public DaoResult getTaskListByGet() throws DataAccessException {
		return taskDao.getTaskListByGet();
	}

	public List<Task> findCheckTasks(Integer usrId) {
		return taskDao.findCheckTasks(usrId);
	}

	@Override
	public int getUnsaveCount(String projectname) throws DataAccessException {
		return taskDao.getUnsaveCount(projectname);
	}
	
	@Override
	public int getUnsaveCountByTaskid(int taskid) throws DataAccessException {
		return taskDao.getUnsaveCountByTaskid(taskid);
	}
	@Override
	public void commit(String projectname, String name, int tasktype, int status)
			throws DataAccessException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tasktype", tasktype);
		map.put("status", status);
		if (tasktype == 0) {
			// 作业任务提交
			map.put("projectname", projectname);
		} else if (tasktype == 1) {
			// 质检任务提交
			map.put("name", name);
		}
		taskDao.commit(map);
	}
	
	@Override
	public void commitByTaskid(int taskid,int status) throws DataAccessException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("taskid", taskid);
		map.put("status", status);
		taskDao.commitTaskByTaskid(map);
	}
/**
 * 
 * 作业领取任务
 */
	@Override
	public void receiveTaskByOp(String taskids, Integer usrid) {
		final String op_userid = "op_userid";
		final String op_receive_time = "op_receive_time";
		final int status = 1;
		taskDao.receiveTask(setReceiveParams(taskids,usrid,op_userid,op_receive_time,status));
	}

/***
 * 质检领取任务
 */
	@Override
	public void receiveTaskByQa(String taskids, Integer usrid) {
		final String qa_userid = "qa_userid";
		final String qa_receive_time = "qa_receive_time";
		final int status = 3;
		taskDao.receiveTask(setReceiveParams(taskids,usrid,qa_userid,qa_receive_time,status));
	}
	private Map<String, Object> setReceiveParams(String taskids, Integer usrid, 
			String user, String receive_time, int status){
		Map<String, Object> map = new HashMap<String, Object>();
		String[] arrays = taskids.split(",");
		int array[] = new int[arrays.length];
		for (int i = 0; i < arrays.length; i++) {
			array[i] = Integer.parseInt(arrays[i]);
		}
		map.put("taskids", array);
		map.put(user, usrid);
		map.put("status",status);
		map.put(receive_time, "date");
		log.debug("update task paramts   :"+map);
		return map;
	}
}
