package com.autonavi.mapart.orm;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.autonavi.mapart.entity.DaoResult;
import com.autonavi.mapart.entity.Task;

/**
 * <p>
 * desc:任务项处理接口
 * <p>
 * Copyright: Copyright(c)AutoNavi 2014
 * </p>
 * 
 * @author <a href="mailTo:i-caiqiang@autonavi.com">i-caiqiang</a>
 * @time 2014-4-24 10:33
 * 
 */
public interface TaskDao {
	
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
	 * @param ids
	 * @return
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
	 * <p>
	 * desc: 查询任务项
	 * <p>
	 * @param params
	 * @return
	 * @throws DataAccessException
	 */
	DaoResult getTaskList(Map<String, Object> params) throws DataAccessException;
	
	DaoResult getTaskListByStatus(Map<String, Object> map);
	
	/**
	 * <p>
	 * desc: 查询任务项
	 * <p>
	 * @param params
	 * @return
	 * @throws DataAccessException
	 */
	DaoResult getTaskListByGet() throws DataAccessException;
	
	
	/**
	 * <p>
	 * desc: 新增任务项
	 * <p>
	 * 
	 * @param task
	 * @return int
	 * @throws DataAccessException
	 */
	int insertTask(Task task) throws DataAccessException;
	
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
	 * desc: 获取项目下未保存的作业项数目
	 * <p>
	 * @param projectname
	 * @return
	 * @throws DataAccessException
	 */
	int getUnsaveCount(String projectname) throws DataAccessException;
	/***
	 * 通过taskid获取任务包未保存的任务项数目
	 */
	int getUnsaveCountByTaskid(int taskid) throws DataAccessException;
	/**
	 * <p>
	 * desc: 提交任务
	 * <p>
	 * @param map
	 * @throws DataAccessException
	 */
	void commit(Map<String, Object> map) throws DataAccessException;
	/**
	 * 
	 * 根据任务包id提交任务，及改变任务包的状态，如果作业提交，改变状态为1，如果质检提交，全部ok 状态为2,存在不ok的任务项状态为0；
	 * @param map
	 * @throws DataAccessException
	 */
	void commitTaskByTaskid(Map<String, Object> map) throws DataAccessException;

	void receiveTask(Map<String, Object> map);

	
}
