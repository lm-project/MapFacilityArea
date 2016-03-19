package com.autonavi.mapart.orm;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import com.autonavi.mapart.entity.ReferenceGeom;
import com.autonavi.mapart.orm.impl.BasicMyBatisDao;

public class ReferenceGeomDao extends BasicMyBatisDao {
	
	Logger log = Logger.getLogger(getClass());
	public void insert(ReferenceGeom geom) {
		log.debug("<------------------------------------>");
		log.debug(geom.getKey()+"       "+geom.getType());
		log.debug("geom:"+geom.getGeom());
		List<ReferenceGeom> dbs = super.getList("ReferenceGeom.getByTypeKey", geom);
		log.debug("----------------------dbs   "+dbs.size());
		if (dbs.isEmpty()) {
			super.save("ReferenceGeom.insert", geom);
		} else {
			for(ReferenceGeom db : dbs) {
				db.setGeom( geom.getGeom() );
				super.update("ReferenceGeom.update", geom);
			}
		}
	}

	public ReferenceGeom select(ReferenceGeom geom) {
		return super.get("ReferenceGeom.getByTypeKey", geom);
	}
							  
	public Collection<String> findCachedNames(String type) {
		return getList("ReferenceGeom.findCachedNames", type);
	}
	
	public Collection<ReferenceGeom> findCachedGeoms(String type) {
		return getList("ReferenceGeom.findCachedGeoms", type);
	}
	
}
