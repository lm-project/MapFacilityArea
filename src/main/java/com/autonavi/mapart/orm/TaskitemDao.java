package com.autonavi.mapart.orm;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.autonavi.mapart.entity.DaoResult;
import com.autonavi.mapart.entity.Taskitem;

/**
 * <p>
 * desc:作业项处理接口
 * <p>
 * Copyright: Copyright(c)AutoNavi 2014
 * </p>
 * 
 * @author <a href="mailTo:i-caiqiang@autonavi.com">i-caiqiang</a>
 * @time 2014-4-24 10:33
 * 
 */
public interface TaskitemDao {
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
	 * 领取作业项
	 * @param task
	 * @return
	 * @throws DataAccessException
	 */
	void receive(Map<String, Object> maps) throws DataAccessException;
	
	/**
	 * 领取质检项
	 * @param params
	 * @throws DataAccessException
	 */
	void QAreceive(Map<String, Object> params) throws DataAccessException;
	
	
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
	DaoResult getUnReceived(Map<String, Object> params) throws DataAccessException;
	
	
	/**
	 * 回收作业项
	 * @param task
	 * @return
	 * @throws DataAccessException
	 */
	void recover(Map<String, Object> params) throws DataAccessException;

	/**
	 * <p>
	 * desc: 更新作业项实现
	 * <p>
	 * @param taskitem
	 * @throws DataAccessException
	 */
	void update(Taskitem taskitem) throws DataAccessException;
	
	/**
	 * 更新删除的任务项
	 * updateDelete
	 */
	void updateDelete(Taskitem taskitem);
	
	/**
	 *<p>
	 * desc: 分页按条件查询未领取任务(作业员)
	 * <p> 
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
	List<Taskitem> getTaskitemListByQuqrey(Map<String, Object> params) throws DataAccessException;

    /**
	 * 根据任务项id查询相应作业项列表
	 * @param map
	 * @return
	 */
	List<Taskitem> getTaskByTaskids(String taskIds,String userId);

	/**
	 *<p>
	 * desc: 批量删除作业项
	 * <p> 
	 * @param idType id类型：0-任务id，1-作业项id
	 * @param map
	 * @return
	 */
	void deleteTaskitemByIds(String ids, int idType);
	

	void updateUserid(String ids, String userId);

	/**
	 * <p>
	 * desc: 作业项提交
	 * <p>
	 * @param map
	 * @throws DataAccessException
	 */
	void commit(Map<String, Object> map) throws DataAccessException;
	
	void updateOpTaskitemStatus(Map<String, Object> map) throws DataAccessException;
	
	List<Taskitem> getTaskitemByQaTaskIds(String taskids, String userId);
	
	DaoResult findCitysByProjectname(String projectname) throws DataAccessException;

	Collection<Taskitem> getAll();

	List<Taskitem> getTaskPackageByPro(Map<String, Object> map);

	int getUnsaveCountByTaskid(int taskid);
}
