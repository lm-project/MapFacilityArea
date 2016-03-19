package com.autonavi.mapart.orm;

import org.springframework.dao.DataAccessException;

import com.autonavi.mapart.entity.Anf_hicaicaibian;
public interface Anf_hicaicaibianDao {
	String getXidByMeshIdAndPoiId(Anf_hicaicaibian anf) throws DataAccessException;
}
