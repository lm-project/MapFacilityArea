package com.autonavi.mapart.orm;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.autonavi.mapart.entity.ReferenceGeom;
import com.autonavi.mapart.orm.impl.AnShapeDaoImpl;
import com.autonavi.mapart.util.GetGeometry;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:service-context-test.xml")

public class ReferenceGeomDaoTest {

	@Autowired
	ReferenceGeomDao dao;
	@Rollback(value=true)
	@Test
	public void testInsert() {
		ReferenceGeom geom = new ReferenceGeom();
		geom.setKey("3");
		geom.setType("百度");
		boolean isReverse = false;
		boolean isSecond = true;
		geom.setContext("");
		String pointPair = GetGeometry.getGeometry("12966047.73,4836066.49,12966191.56,4836260.86,12966450.69,4836049.83,12966584.7,4836207.886,12966659.24,4836170.612,12966806.87,4836058.3,12966683.28,4835835.149,12966520.45,4835894.493,12966401.59,4835984.214,12966298.18,4835856.55,12966048.09,4836065.79,12966047.73,4836066.49", isReverse,"POLYGON",isSecond);
		geom.setGeom(pointPair);
		dao.insert(geom );
	}
	
	@Test
	public void testFind() {
		Collection<String> list = dao.findCachedNames("1");
		assertEquals(1, list.size()+"******************");
	}

}
