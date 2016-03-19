package com.autonavi.mapart.service;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.autonavi.mapart.entity.DaoResult;
import com.autonavi.mapart.entity.SetGetTaskListForm;
import com.autonavi.mapart.entity.Task;

/**
 * <p>
 * desc:任务项和作业项服务接口
 * <p>
 * Copyright: Copyright(c)AutoNavi 2014
 * </p>
 * 
 * @author <a href="mailTo:i-caiqiang@autonavi.com">i-caiqiang</a>
 * @time 2014-4-24 10:31
 * 
 */
public interface TaskService {
	
	/**
	 * <p>
	 * desc: 获取单任务项
	 * <p>
	 * @param task
	 * @return Task
	 * @throws DataAccessException
	 */
	Task getTaskById(int id) throws DataAccessException;
	
	/**
	 * <p>
	 * desc: 获取多任务项
	 * <p>
	 * @param task
	 * @return Task
	 * @throws DataAccessException
	 */
	List<Task> getTaskByIds(String ids) throws DataAccessException;
	
	/**
	 * <p>
	 * desc: 获取多任务项
	 * <p>
	 * @param task
	 * @return Task
	 * @throws DataAccessException
	 */
	List<Task> getTasksByProjectName(String Projectname) throws DataAccessException;
	

	/**
	 * 查询任务项 
	 * @param form： page 页码，recordCountPerPage 查询条数，
	 * tasktype 任务类别， status 任务状态，name 查询关键字，
	 * uid 用户ID， 查询内容   1- 查询项目  2-查询任务项
	 * @return
	 * @throws DataAccessException
	 * @return
	 * @throws DataAccessException
	 */
	DaoResult getTaskList(SetGetTaskListForm form) throws DataAccessException;
	/***
	 * 
	 * 查询任务根据状态、页码、数量
	 * @param map
	 * @return
	 */
	DaoResult getTaskListByStatus(Map<String, Object> map);
	
	/**
	 * <p>
	 * desc: 查询任务项
	 * <p>
	 * 
	 * @return Map<String, Object>
	 * @throws DataAccessException
	 */
	DaoResult getTaskListByGet() throws DataAccessException;
	
	
	/**
	 * 插入单任务项任务项
	 * @param task
	 * @return
	 * @throws DataAccessException
	 */
	int insertTask(Task task) throws DataAccessException;
	
	/**
	 * <p>
	 * desc: 导入任务清单时批量插入任务项
	 * <p>
	 * @param filepath
	 * @param remark
	 * @param uid
	 * @return DaoResult
	 */
	DaoResult insertTasks(String filepath, String remark, int uid);
	
	
	/**
	 * <p>
	 * desc: 更新任务项
	 * <p>
	 * 
	 * @param task
	 * @return int
	 * @throws DataAccessException
	 */
	int updateTask(Task task) throws DataAccessException;
	
	/**
	 * <p>
	 * desc: 删除任务项
	 * <p>
	 * 
	 * @param ids
	 * @throws DataAccessException
	 */
	void deleteTaskByIds(String ids) throws DataAccessException;
	
	/**
	 * <p>
	 * desc: 删除项目
	 * <p>
	 * 
	 * @param ids
	 * @throws DataAccessException
	 */
	void deleteTaskByProjectname(String pros) throws DataAccessException;

	/**
	 * <p>
	 * desc: 获取单个项目
	 * <p>
	 * @param filename
	 * @return
	 * @throws DataAccessException
	 */
	Task getProjectByName(String projectname) throws DataAccessException;
	
	List<Task> findCheckTasks(Integer usrId);

	/**
	 * <p>
	 * desc:获取项目下未保存的作业项数目
	 * <p>
	 * @param projectname
	 * @return
	 * @throws DataAccessException
	 */
	int getUnsaveCount(String projectname) throws DataAccessException;
	
	int getUnsaveCountByTaskid(int taskid) throws DataAccessException;
	/**
	 * <p>
	 * desc:项目提交-任务提交
	 * <p>
	 * @param projectname 项目名称
	 * @param status 任务状态
	 * @param tasktype 任务类型
	 * @param name 任务名称
	 * @throws DataAccessException
	 */
	void commit(String projectname, String name, int tasktype, int status) throws DataAccessException;
	
	/**
	 按任务包提交任务，即根据taskid
	 * @param task_status 
	 */
	void commitByTaskid(int taskid, int task_status) throws DataAccessException;
	/***
	 * 
	 * 按任务包领取任务通过任务id
	 * @param taskids
	 * @param usrid
	 */

	void receiveTaskByOp(String taskids, Integer usrid);

	void receiveTaskByQa(String taskids, Integer usrid);
	
	
	
	
}
