package com.autonavi.mapart.orm.impl;

import org.springframework.dao.DataAccessException;

import com.autonavi.mapart.entity.Anf_hicaicaibian;
import com.autonavi.mapart.orm.Anf_hicaicaibianDao;

public class Anf_hicaicaibianDaoImpl extends BasicMyBatisDao implements Anf_hicaicaibianDao {

	@Override
	public String getXidByMeshIdAndPoiId(Anf_hicaicaibian anf) throws DataAccessException {
		return super.get("Anf_hicaicaibianMapper.getXidByMESHID_POIID", anf);
	}
}
//timestamp without time zone