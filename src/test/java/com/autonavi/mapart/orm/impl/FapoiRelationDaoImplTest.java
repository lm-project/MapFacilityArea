package com.autonavi.mapart.orm.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.autonavi.mapart.entity.FapoiRelation;
import com.autonavi.mapart.service.BaseTestCase;
import com.autonavi.mapart.util.ImportFapoiRelation;

public class FapoiRelationDaoImplTest extends BaseTestCase {
	
	@Autowired
	private FapoiRelationDaoImpl dao;
	Logger log = Logger.getLogger(getClass());
	@Test
	public void testInsertRelations() {
		String path = "D:/设施区域类型编码.xlsx";
		File file = new File(path);
		List<FapoiRelation> list = null;
		try {
			if (file.exists()) {
				System.out.println(file.getAbsolutePath());
				list = ImportFapoiRelation.readRelationExcel(path);
				log.debug("------------------------------------");
				log.debug(list);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		dao.insertRelations(list);
	}

	@Test
	public void testDeleteALl() {
		dao.deleteALl();
	}

	@Test
	public void testGetAllFatypes() {
		Map<String, String> map = dao.getAllFatypes();
		assertEquals(map.size(), 97);
	}

}
