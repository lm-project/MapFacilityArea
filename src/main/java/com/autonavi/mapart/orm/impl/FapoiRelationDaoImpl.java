/**
 * 
 */
package com.autonavi.mapart.orm.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.autonavi.mapart.entity.FapoiRelation;
import com.autonavi.mapart.orm.FapoiRelationDao;

/**
 * @author huandi.yang
 *
 */
public class FapoiRelationDaoImpl extends BasicMyBatisDao implements FapoiRelationDao {

	@Override
	public void insertRelations(List<FapoiRelation> list) throws DataAccessException {
		super.save("fapoiRelationMapper.insert", list);
	}

	@Override
	public void deleteALl() throws DataAccessException {
		super.delete("fapoiRelationMapper.delete", null);

	}

	@Override
	public Map<String,String> getAllFatypes() {
		Map<String,String> m = new HashMap<String, String>();
		List<FapoiRelation> list = super.getList("fapoiRelationMapper.getAllFatypes");
		for(FapoiRelation f : list) {
			m.put(f.getPoi_typecode(), f.getFa_type().substring(0, 4)+"_"+f.getId());
		}
		return m;
	}

	@Override
	public List<FapoiRelation> getFapoiRelationList(Map<String, Object> params) {
		return super.getList("fapoiRelationMapper.queryRelationList", params);
	}

	@Override
	public void update(FapoiRelation relation) throws DataAccessException {
		super.update("fapoiRelationMapper.update", relation);
	}

	@Override
	public List<String> getFacategory() throws DataAccessException {
		return super.getList("fapoiRelationMapper.getFacategory");
	}

	@Override
	public List<String> getFatypeList(Map<String, Object> params)
			throws DataAccessException {
		return super.getList("fapoiRelationMapper.getFatypeList", params);
	}

	@Override
	public List<String> getPoitypeList(Map<String, Object> params)
			throws DataAccessException {
		return super.getList("fapoiRelationMapper.getPoitypeList", params);
	}

}
