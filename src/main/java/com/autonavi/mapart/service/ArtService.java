package com.autonavi.mapart.service;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.autonavi.mapart.entity.Facilityachieve;
import com.autonavi.mapart.entity.Qaresult;
import com.autonavi.mapart.entity.ResponseStatus;

/**
 * <p>
 * desc:地图参考服务接口
 * <p>
 * Copyright: Copyright(c)AutoNavi 2014
 * </p>
 * 
 * @author <a href="mailTo:i-caiqiang@autonavi.com">i-caiqiang</a>
 * @time 2014-4-24 10:31
 * 
 */
public interface ArtService {
	
	/**
	 * 查询百度关键字所在设施区域
	 * @param key
	 * @return geo
	 */
	public ResponseStatus getBaiduMapresult(String key);
	
	/**
	 * 查询QQ关键字所在设施区域
	 * @param key
	 * @return geo
	 */
	public ResponseStatus getGetQQMapresult(String key);
	
	
	/**
	 * 查询高德关键字POI信息
	 * @param key
	 * @return 
	 */
	public ResponseStatus getAmapresult(String key);
		
	/**
	 * 查询facilityarea库中的面记录
	 * @param bounds
	 * @return 
	 */
	public List<Facilityachieve> getPolygonByBound(String bounds);
	
	/**
	 * <p>
	 * desc: 作业新增实现
	 * <p>
	 * @param type 
	 * 
	 * @param userName
	 * @param password
	 * @return User
	 * @throws DataAccessException
	 */
	public void insertFacilityarea(Facilityachieve gig) throws DataAccessException;

	public void insertQaresult(Qaresult[] fa);
	
	public List<Qaresult> getQaresult(int taskitemid);
	public List<Qaresult> getPolylineByBound(String bounds);
	
	/**
	 * 删除作业项
	 */
	public void deleteTaskItem(int taskItemId);

}
