package com.autonavi.mapart.service;

import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.autonavi.mapart.entity.ResponseStatus;
import com.autonavi.mapart.orm.FapoiRelationDao;

public class PolygonCacheServiceTest extends BaseTestCase {

	@Autowired
	PolygonCacheService service;
	
	@Autowired
	private FapoiRelationDao fapoiRelationDao;
	
	@Test
	public void test() throws InterruptedException {
//		service.readQQ("安慧东里");
//		service.readBd("安慧东里");
		ResponseStatus rs = service.readAutonavi("北京大学");
		assertEquals("", rs.getRestring());
	}
	
	
	@Test
	public void testGetFatypeByPoitype(){
		String poiString = "J50F001020_231767_120302_39.990785_116.422584,J50F001020_231768_120302_39.989708_116.421719";
		Map<String,String> allFatypes = fapoiRelationDao.getAllFatypes();
		String poi = service.getFatypeByPoitype(poiString, allFatypes);
		System.out.println("_____"+poi);
		assertEquals("", poi);
	}

}
