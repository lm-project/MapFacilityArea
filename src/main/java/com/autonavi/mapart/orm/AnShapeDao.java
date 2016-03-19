package com.autonavi.mapart.orm;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.autonavi.mapart.entity.Facilityachieve;

public interface AnShapeDao {

	Facilityachieve getShapeByName(String name);

	/**
	 * 批量导入AN数据
	 * @param list
	 * @throws DataAccessException
	 */
	void importAnData(List<Facilityachieve> list) throws DataAccessException;
	
	void updateDataByImport(Facilityachieve facilityachieve) throws DataAccessException;
	/**
	 * 
	 * 查询所有本地数据库的poi_id,mesh,geom
	 * 
	 */
	List<Facilityachieve> getDbData()throws DataAccessException;
	
	
}
