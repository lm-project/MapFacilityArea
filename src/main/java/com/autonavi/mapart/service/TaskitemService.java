package com.autonavi.mapart.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.autonavi.mapart.entity.DaoResult;
import com.autonavi.mapart.entity.Qaresult;
import com.autonavi.mapart.entity.Taskitem;

/**
 * <p>
 * desc:作业项服务接口
 * <p>
 * Copyright: Copyright(c)AutoNavi 2014
 * </p>
 * 
 * @author <a href="mailTo:i-caiqiang@autonavi.com">i-caiqiang</a>
 * @time 2014-4-24 10:31
 * 
 */
public interface TaskitemService {

	/**
	 * <p>
	 * desc: 获取任作业项
	 * <p>
	 * 
	 * @param taskitem
	 * @return List<Taskitem>
	 * @throws DataAccessException
	 */
	List<Taskitem> getTaskitemByIds(String ids) throws DataAccessException;

	/**
	 * <p>
	 * desc: 新增作业项实现
	 * <p>
	 * 
	 * @param list
	 * @throws DataAccessException
	 */
	void insertTaskitem(List<Taskitem> list) throws DataAccessException;

	/**
	 * <p>
	 * desc: 更新作业项实现
	 * <p>
	 * 
	 * @param userName
	 * @param password
	 * @return User
	 * @throws DataAccessException
	 */
	void updateTaskitem(Taskitem taskitem)
			throws DataAccessException;
	/**
	 * 更新删除的任务项
	 * updateDelete
	 */
	void updateDelete(Taskitem taskitem);
	/**
	 * <p>
	 * desc: 分页按条件查询未领取任务(作业员)
	 * <p>
	 * 
	 * @param map
	 * @return
	 */
	Map<String, Object> getTaskitemListByPage(Map<String, Object> map);

	/**
	 * <p>
	 * desc: 查询作业项实现
	 * <p>
	 * 
	 * @return Map<String, Object>
	 * @throws DataAccessException
	 */
	List<Taskitem> getTaskitemListByQuqrey(Map<String, Object> params)
			throws DataAccessException;

	/**
	 * 根据任务项id查询相应作业项列表
	 * @param userId TODO
	 * @param map
	 * 
	 * @return
	 */
	List<Taskitem> getTaskByTaskids(String ids, String userId);

	/**
	 * <p>
	 * desc: 批量删除作业项
	 * <p>
	 * @param idType id类型：0-任务id，1-作业项id
	 * 
	 * @param map
	 * @return
	 */
	void deleteTaskitemByIds(String ids, int idType);


	/**
	 * 作业项领取
	 * @param taskids
	 * @param taskitemids
	 * @param userId
	 */
	void receive(String taskids, int userId);
	
	/**
	 * 质检项领取
	 * @param taskids
	 * @param qataskname
	 * @param userId
	 * @param projectname
	 * @param city
	 * @param number
	 */
	void QAreceive(int taskid, String qataskname, int userId, String projectname, String city, int number);
	
	
	/**
	 * 获取领取质检项数量
	 * @param params
	 * @throws DataAccessException
	 */
	int getQaUnreceiveUnmber(String pro, String city, String userid) throws DataAccessException;
	
	
	
	/**
	 * 获取未领取已选择作业项
	 * @param params
	 * @throws DataAccessException
	 */
	DaoResult getReceive(int pageNum, int pageSize, String taskname, String processtype, int status, int uid) throws DataAccessException;
	
	/**
	 * 获取未领取质检项
	 * @param params
	 * @throws DataAccessException
	 */
	DaoResult getUnQAReceived(int pageNum, int pageSize) throws DataAccessException;
	
	/**
	 * 获取领取已选择质检项
	 * @param params
	 * @throws DataAccessException
	 */
	DaoResult getQAReceived(int userId, int status, int pageNum, int pageSize) throws DataAccessException;
	
	
	/**
	 * 作业项回收
	 * @param taskids
	 * @param taskitemids
	 */
	void recover(String taskids, String taskitemids);

	
	/**
	 * 作业项提交
	 * @param projectname
	 * @param taskname
	 * @param tasktype
	 * @param taskitemStatus
	 * @throws DataAccessException
	 */
	void commit(String projectname, String taskname, int tasktype, int taskitemStatus) throws DataAccessException;
	
	
	void commitOpTaskitem(int taskid, int taskitme_status)throws DataAccessException;
	
	List<Taskitem> getTaskitemsByQaTaskids(String taskids, String string);

	DaoResult findCitysByProjectname(String projectname) throws DataAccessException;
	
	
	Collection<Taskitem> getAll();

	List<Taskitem> getTaskPackageList(Integer page, String projectname, int status,
			Integer usrid);

	int getUnsaveCountByTaskid(int taskid);

	List<Qaresult> getQaInfoByTaskitemId(Integer taskitemid);
	
	
}
