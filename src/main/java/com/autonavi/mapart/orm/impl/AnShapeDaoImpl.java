package com.autonavi.mapart.orm.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;

import com.autonavi.mapart.entity.Facilityachieve;
import com.autonavi.mapart.orm.AnShapeDao;


public class AnShapeDaoImpl extends BasicMyBatisDao implements AnShapeDao {
	Logger log = Logger.getLogger(getClass());
	@Override
	public Facilityachieve getShapeByName(String name_chn) {
		return super.get("anShapeMapper.getShapeByName", name_chn);
	}

	@Override
	public void importAnData(List<Facilityachieve> list) throws DataAccessException {
		super.save("facilityachieveMapper.importAnData", list);
		log.debug("将导入的数据插入本地数据库完——————————————————————————");
	}

	@Override
	public void updateDataByImport(Facilityachieve facilityachieve) throws DataAccessException {
		super.update("facilityachieveMapper.updateByImport", facilityachieve);
		log.debug("将导入的数据跟新本地数据库完——————————————————————————");
	}

	
	@Override
	public List<Facilityachieve> getDbData() throws DataAccessException {
		return super.getList("facilityachieveMapper.importSelect");
	}
}
