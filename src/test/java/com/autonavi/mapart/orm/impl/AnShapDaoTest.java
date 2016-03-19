package com.autonavi.mapart.orm.impl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.autonavi.mapart.entity.Facilityachieve;
import com.autonavi.mapart.orm.AnShapeDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:service-context-test.xml")

public class AnShapDaoTest {
	@Autowired
	AnShapeDao dao;
	@Rollback(value=true)
	@Test
	public void testInsert() {
		ArrayList<Facilityachieve> list = new ArrayList<Facilityachieve>();
		Facilityachieve fac = new Facilityachieve("托尔斯泰","K50F048019",300291,3310,0,0,0,
				"MULTIPOLYGON(((419385.2472 141139.0944,419425.1064 141138.2592,419398.3764 141120.6588,419385.2472 141139.0944)))",
				"a",0,"","");
		list.add(fac);
		assertEquals(1,"====================================="+dao);
		dao.importAnData(list);
		assertEquals(0, "插入完成-------------------------");
	}
	

}
