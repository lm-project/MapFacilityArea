package com.autonavi.mapart.service;

import com.autonavi.mapart.entity.DaoResult;
import com.autonavi.mapart.entity.Facilityachieve;

public interface AnShpService {
	
	/**
	 * 导入shp文件
	 * @param filepath 文件路径
	 * @param dataVersion 数据版本
	 * @return
	 * @throws Exception
	 */
	DaoResult importShp(String filepath, String dataVersion) throws Exception;
	
	Facilityachieve getAnShapeByName(String name);

}
