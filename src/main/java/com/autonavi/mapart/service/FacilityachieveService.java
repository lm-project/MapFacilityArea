package com.autonavi.mapart.service;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.autonavi.mapart.entity.DaoResult;
import com.autonavi.mapart.entity.Facilityachieve;

public interface FacilityachieveService {

	/**
	 * 导出shape数据
	 * @param param 条件参数(项目名称or任务id)
	 * @param type 参数类型('project':按项目导出,param值为项目名称; 'task':按任务导出,param值为任务id; 'qaresult':导出质检数据,param值为项目名称)
	 * @return
	 * @throws Exception
	 */
	String pgsql2shp(String param, String type) throws Exception;
	
	
	boolean shp2pgsql(String dir) throws Exception;


	DaoResult importShp(String filepath, String dataVersion) throws Exception;
	
	/**
	 * <p>
	 * desc: 查询设施区域
	 * <p>
	 * 
	 * @return List<Facilityachieve>
	 * @throws DataAccessException
	 */
	List<Facilityachieve> getFacilityareaList(String id) throws DataAccessException;

	/**
	 * 
	 */
	void updateFacilityachieve(Facilityachieve fa);

}
