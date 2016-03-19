package com.autonavi.mapart.orm.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;

import com.autonavi.mapart.entity.DaoResult;
import com.autonavi.mapart.entity.Task;
import com.autonavi.mapart.entity.Taskitem;
import com.autonavi.mapart.orm.TaskitemDao;

public class TaskitemDaoImpl extends BasicMyBatisDao implements TaskitemDao {

	@Override
	public List<Taskitem> getTaskitemByIds(String ids) throws DataAccessException {
		int[] arrays = convertString2ArrayInt(ids);
		return super.getList("taskitemMapper.selectByIds", arrays);
	}

	@Override
	public void insertTaskitem(List<Taskitem> list) throws DataAccessException {
		super.save("taskitemMapper.insert", list);

	}

	@Override
	public List<Taskitem> getTaskitemListByQuqrey(Map<String, Object> params) throws DataAccessException {
		return super.getList("taskitemMapper.getByQuery", params);
	}

	@Override
	public void deleteTaskitemByIds(String ids, int idType) {
		int[] array = convertString2ArrayInt(ids);
		
		if(idType == 0){
			super.delete("taskitemMapper.deleteByTaskIds", array);
		} else {
			super.delete("taskitemMapper.deleteByIds", array);
		}
	}

	private int[] convertString2ArrayInt(String ids) {
		String[] arrays = ids.split(",");
		int array[] = new int[arrays.length];  
		for(int i=0;i<arrays.length;i++){  
		    array[i]=Integer.parseInt(arrays[i]);   
		}
		return array;
	}

	@Override
	public Map<String, Object> getTaskitemListByPage(Map<String, Object> map) {
		HashMap<String, Object> paras = new HashMap<String, Object>();
		super.get("taskitemMapper.getTaskitemsByPage", map);
		@SuppressWarnings("unchecked")
		List<Taskitem> Taskitem = (List<Taskitem>) map.get("curResult");
		int allRecordCount = (Integer) map.get("allRecordCount");
		paras.put("taskitems", Taskitem);
		paras.put("allRecordCount", allRecordCount);
		return paras;
	}

	@Override
	public List<Taskitem> getTaskByTaskids(String taskIds,String userId) {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("taskIds", convertString2ArrayInt(taskIds));
		m.put("userId", Integer.parseInt(userId));
		return super.getList("taskitemMapper.selectByTaids", m);
	}


	@Override
	public void updateUserid(String taskIds, String userId) {
		Map<String, String> m = new HashMap<String, String>();
		m.put("taskIds", taskIds);
		m.put("userId", userId);
		super.update("taskitemMapper.updateUserid", m);
	}

	@Override
	public void receive(Map<String, Object> maps) throws DataAccessException {
		super.update("taskitemMapper.receive", maps);
	}

	@Override
	public void QAreceive(Map<String, Object> params)
			throws DataAccessException {
//		super.update("taskitemMapper.lock",params);
		super.update("taskitemMapper.qareceive", params);
//		super.update("taskitemMapper.unlock",params);
	}
	
	@Override
	public int getQaUnreceiveUnmber(String pro, String city, String userid)
			throws DataAccessException {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("projectname", pro);
		m.put("city", city);
		m.put("userid", StringUtils.isNotBlank(userid)?Integer.parseInt(userid):null);
		return (int) super.get("taskitemMapper.getQaUnreceiveUnmber", m);
		
	}
	

	@Override
	public void recover(Map<String, Object> params) throws DataAccessException {
		super.update("taskitemMapper.recover", params);
		
	}

	@Override
	public void update(Taskitem taskitem) throws DataAccessException {
		super.update("taskitemMapper.update", taskitem);
		
	}
	@Override
	public void updateDelete(Taskitem taskitem) throws DataAccessException {
		super.update("taskitemMapper.updateDelete", taskitem);
		
	}
	@SuppressWarnings("unchecked")
	@Override
	public DaoResult getUnReceived(Map<String, Object> params) throws DataAccessException {
		super.get("taskitemMapper.getTaskListByUseridAndStatus", params);
		if( params.get("allRecordCount") == null) {
			return new DaoResult();
		} else {
			String allRecordCount = String.valueOf( params.get("allRecordCount") );
			return new DaoResult((List<Object>) params.get("curResult"),
					allRecordCount=="" ? 0 : Integer.valueOf( allRecordCount ) );
		}
		
	}


	@Override
	public void commit(Map<String, Object> map) throws DataAccessException {
		if ( map.get("tasktype").equals(0)) {
			//作业任务提交
			super.update("taskitemMapper.commit", map);
		} else if ( map.get("tasktype").equals(1) ) {
			//质检任务提交
			super.update("taskitemMapper.commitQaItem", map);
		}
		
	}
	@Override
	public void updateOpTaskitemStatus(Map<String, Object> map) throws DataAccessException{
		super.update("taskitemMapper.commitOpItem", map);
	}
	@Override
	public List<Taskitem> getTaskitemByQaTaskIds(String taskids, String userId) {
		Map<String,Object> m = new HashMap<String,Object>();
		String[] arrays = taskids.split(",");
		int array[] = new int[arrays.length];
		for (int i = 0; i < arrays.length; i++) {
			array[i] = Integer.parseInt(arrays[i]);
		}
		m.put("taskIds", array);
		m.put("userId", Integer.valueOf(userId));
		return super.getList("taskitemMapper.getTaskitemByQaTaskIds", m);
	}

	@Override
	public DaoResult findCitysByProjectname(String projectname)
			throws DataAccessException {
		List<Object> list = super.getList("taskitemMapper.findCitysByProjectname", projectname);
		return new DaoResult( list, list.size());
	}

	@Override
	public Collection<Taskitem> getAll() {
		return super.getList("taskitemMapper.findAllItems");
	}
	
	@Override
	public List<Taskitem> getTaskPackageByPro(Map<String, Object> map) {
		return super.getList("taskitemMapper.getTaskitemsByQuery",map);
	}
	public static void main(String[] args) {
		System.out.println(" ".equals(" "));
		System.out.println(org.apache.commons.lang3.StringUtils.trim("　3310（风景名胜）"));
	}
	@Override
	public int getUnsaveCountByTaskid(int taskid) throws DataAccessException{
		return super.get("taskitemMapper.getUnsaveCountByTaskid", taskid);
	}
	
}
