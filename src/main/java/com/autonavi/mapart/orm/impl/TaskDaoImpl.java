package com.autonavi.mapart.orm.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;

import com.autonavi.mapart.entity.DaoResult;
import com.autonavi.mapart.entity.Task;
import com.autonavi.mapart.orm.TaskDao;

public class TaskDaoImpl extends BasicMyBatisDao implements TaskDao {
	Logger log =  Logger.getLogger(getClass());
	@Override
	public Task getTaskById(int id) throws DataAccessException {
		return super.get("taskMapper.getTaskById", id);
	}

	@Override
	public List<Task> getTaskByIds(String ids) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Task> getTasksByProjectName(String projectname)
			throws DataAccessException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", projectname);
		return super.getList("taskMapper.getTasksByProjectName", map);
	}
	
	@Override
	public int insertTask(Task task) throws DataAccessException {
		super.save("taskMapper.insert", task);
		return task.getId();
	}
	@Override
	public int updateTask(Task task) throws DataAccessException {
		super.save("taskMapper.update", task);
		return task.getId();
	}

	@Override
	public void deleteTaskByIds(String ids) throws DataAccessException {
		String[] arrays = ids.split(",");
		int array[] = new int[arrays.length];
		for (int i = 0; i < arrays.length; i++) {
			array[i] = Integer.parseInt(arrays[i]);
		}
		super.delete("taskMapper.delete", array);

	}
	
	@Override
	public void deleteTaskByProjectname(String pros) throws DataAccessException {
		String[] arrays = pros.split(",");
		System.out.println("======================"+pros+"======="+arrays[0]+"====="+arrays.length);
		super.delete("taskMapper.deleteproject", arrays);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public DaoResult getTaskList(Map<String, Object> params) throws DataAccessException {
		if ( "selectProject".equals(params.get("selectType")) ) {
			super.get("taskMapper.selectProjectByPage", params);
		}else {
			super.get("taskMapper.selectByPage", params);
		}
		return new DaoResult((List<Object>) params.get("curResult"),
				(Integer) params.get("allRecordCount"));
	}
	@SuppressWarnings("unchecked")
	@Override
	public DaoResult getTaskListByStatus(Map<String, Object> map) {
		log.debug(map.toString() +"**********************************");
		super.get("taskMapper.selectTaskByPage", map);
		log.debug( map.get("allRecordCount")+"-----------------------"+map.get("curResult").toString());
		return new DaoResult((List<Object>) map.get("curResult"),
				(Integer) map.get("allRecordCount"));
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public DaoResult getTaskListByGet() throws DataAccessException {
		HashMap<String, Object> params = new HashMap<String, Object>();
		super.get("taskMapper.selectByGet", params);
		return new DaoResult((List<Object>) params.get("curResult"),
				(Integer) params.get("allRecordCount"));
	}

	@Override
	public Task getProjectByName(String projectname) throws DataAccessException {
		return super.get("taskMapper.getProjectByName", projectname);
	}
	@Override
	public List<Task> findCheckTasks(Integer usrId) {
		return super.getList("taskitemMapper.findCheckTasks", usrId);
	}

	@Override
	public int getUnsaveCount(String projectname) throws DataAccessException {
		return super.get("taskMapper.getUnsaveCount", projectname);
	}
	
	@Override
	public int getUnsaveCountByTaskid(int taskid) throws DataAccessException{
		return super.get("taskMapper.getUnsaveCountByTaskid", taskid);
	}
	
	@Override
	public void commit(Map<String, Object> map) throws DataAccessException {
		if ( map.get("tasktype").equals(0)) {
			//作业任务提交
			super.update("taskMapper.commit", map);
		} else if ( map.get("tasktype").equals(1) ) {
			//质检任务提交
			super.update("taskMapper.commitQaTask", map);
		}
		
	}
	@Override
	public void commitTaskByTaskid(Map<String, Object> map) throws DataAccessException{
		super.update("taskMapper.updateTaskByOpCommit", map);
	}

	@Override
	public void receiveTask(Map<String, Object> map) {
		super.update("taskMapper.receive", map);
	}

}
