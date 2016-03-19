package com.autonavi.mapart.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.autonavi.mapart.entity.DaoResult;
import com.autonavi.mapart.entity.Qaresult;
import com.autonavi.mapart.entity.Task;
import com.autonavi.mapart.entity.Taskitem;
import com.autonavi.mapart.orm.QaResultDao;
import com.autonavi.mapart.orm.TaskitemDao;
import com.autonavi.mapart.service.TaskitemService;

@Service
public class TaskitemServiceImpl implements TaskitemService {
	Logger log =  Logger.getLogger(getClass());
	@Autowired
	private TaskitemDao taskitemDao;
	@Autowired
	private QaResultDao qaResultDao;
	@Override
	public void insertTaskitem(List<Taskitem> list) throws DataAccessException {
		taskitemDao.insertTaskitem(list);
	}

	@Override
	public void updateTaskitem(Taskitem taskitem)
			throws DataAccessException {
		taskitemDao.update(taskitem);
	}
	
	@Override
	public void updateDelete(Taskitem taskitem) throws DataAccessException {
		taskitemDao.updateDelete(taskitem);
		
	}
	
	@Override
	public List<Taskitem> getTaskitemListByQuqrey(Map<String, Object> params)
			throws DataAccessException {
		return taskitemDao.getTaskitemListByQuqrey(params);
	}

	@Override
	public void deleteTaskitemByIds(String ids, int idType) {
		taskitemDao.deleteTaskitemByIds(ids, idType);
	}

	@Override
	public Map<String, Object> getTaskitemListByPage(Map<String, Object> map) {
		return taskitemDao.getTaskitemListByPage(map);
	}

	@Override
	public List<Taskitem> getTaskByTaskids(String ids, String userId) {
		return taskitemDao.getTaskByTaskids(ids,userId);
	}


	@Override
	public void receive(String taskids, int userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		String[] arrays = taskids.split(",");
		int array[] = new int[arrays.length];
		for (int i = 0; i < arrays.length; i++) {
			array[i] = Integer.parseInt(arrays[i]);
		}
		map.put("taskids", array);
		map.put("userid", userId);
		taskitemDao.receive(map);
	}

	@Override
	public DaoResult getReceive(int pageNum, int pageSize, String taskname, String processtype, 
			int status, int uid) throws DataAccessException {
		Map<String,Object> m = new HashMap<String,Object>();
		m.put("pageIndex", pageNum);
		m.put("recordCountPerPage", pageSize);
		m.put("status", status);
		m.put("userid", uid);
		m.put("taskname", taskname);
		m.put("processtype", processtype);
		return taskitemDao.getUnReceived(m);
	}
	
	@Override
	public void recover(String taskids, String taskitemids) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(taskids == null || taskids == ""){
			map.put("taskids", taskids);
		} else {
			String[] arrays = taskids.split(",");
			int array[] = new int[arrays.length];
			for (int i = 0; i < arrays.length; i++) {
				array[i] = Integer.parseInt(arrays[i]);
			}
			map.put("taskids", array);
		}
		if(taskitemids == null || taskitemids ==""){
			map.put("taskitemids", taskitemids);
		} else {
			String[] arrays = taskitemids.split(",");
			int array[] = new int[arrays.length];
			for (int i = 0; i < arrays.length; i++) {
				array[i] = Integer.parseInt(arrays[i]);
			}
			map.put("taskitemids", array);
		}
		taskitemDao.recover(map);
	}

	@Override
	public void commit(String projectname, String taskname, int tasktype,
			int taskitemStatus) throws DataAccessException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tasktype", tasktype);
		map.put("status", taskitemStatus);
		if (tasktype == 0) {
			//作业任务项提交
			map.put("projectname", projectname);
		} else if (tasktype == 1) {
			//质检任务项提交
			map.put("name", taskname);
		}
		taskitemDao.commit(map);
		
	}

	@Override
	public void commitOpTaskitem(int taskid, int taskitme_status)throws DataAccessException{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("taskid", taskid);
		map.put("status", taskitme_status);
		taskitemDao.updateOpTaskitemStatus(map);
	}

	@Override
	public List<Taskitem> getTaskitemsByQaTaskids(String taskids, String userId) {
		return taskitemDao.getTaskitemByQaTaskIds(taskids, userId);
	}

	@Override
	public synchronized void QAreceive(int taskid, String qataskname, int userId, String projectname, String city, int number) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("taskid", taskid);
		map.put("qataskname", qataskname);
		map.put("userid", userId);
		map.put("projectname", projectname);
		map.put("city", city);
		map.put("number", number);
		
		taskitemDao.QAreceive(map);
	}

	@Override
	public int getQaUnreceiveUnmber(String pro, String city, String userid)
			throws DataAccessException {
		
		return taskitemDao.getQaUnreceiveUnmber(pro, city, userid);
	}
	
	@Override
	public DaoResult getUnQAReceived(int pageNum, int pageSize)
			throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DaoResult getQAReceived(int userId, int status, int pageNum,
			int pageSize) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DaoResult findCitysByProjectname(String projectname)
			throws DataAccessException {
		return taskitemDao.findCitysByProjectname(projectname);
	}

	@Override
	public Collection<Taskitem> getAll() {
		return  taskitemDao.getAll();
	}

	@Override
	public List<Taskitem> getTaskitemByIds(String ids)
			throws DataAccessException {
		
		return  taskitemDao.getTaskitemByIds(ids);
	}

	@Override
	public List<Taskitem> getTaskPackageList(Integer page, String projectname,
			int status, Integer usrid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", page);
		map.put("status", status);
		map.put("projectname", projectname);
		map.put("qauerid", usrid);
		return taskitemDao.getTaskPackageByPro(map);
	}
	
	
	@Override
	public int getUnsaveCountByTaskid(int taskid) throws DataAccessException {
		return taskitemDao.getUnsaveCountByTaskid(taskid);
	}

	@Override
	public List<Qaresult> getQaInfoByTaskitemId(Integer taskitemid) {
		return qaResultDao.getQaResultListByTaskitemid(taskitemid);
	}
}
