package com.autonavi.mapart.service.impl;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.autonavi.mapart.entity.DaoResult;
import com.autonavi.mapart.entity.FapoiRelation;
import com.autonavi.mapart.service.BaseTestCase;
import com.autonavi.mapart.service.FapoiRelationService;

public class FapoiRelationServiceImplTest extends BaseTestCase {
	
	@Autowired
	private FapoiRelationService service;

	@Test
	public void testUpdateRelation() {
		String filepath = "E:/test.xlsx";
		File file = new File(filepath);
		if (file.exists()) {
			System.out.println(file.getAbsolutePath());
		}
		try {
			DaoResult result = service.updateRelation(filepath);
			System.out.println(result.getRemark());
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	@Test
	public void testGetFapoiRelationList() {
		List<FapoiRelation> list = service.getFapoiRelationList("", "", "");
		assertEquals(0, list.size());
	}
	
	@Test
	public void testUpdate(){
		
	}
	
	@Test
	public void testGetFacategory(){
		List<String> list = service.getFacategory();
		assertEquals(0, list.size());
	}
	
	@Test
	public void getFatypeList(){
		List<String> list = service.getFatypeList("");
		assertEquals(0, list.size());
	}
	
	@Test
	public void getPoitypeList(){
		List<String> list = service.getPoitypeList("3320（公园）");
		assertEquals(0, list.size());
	}

}
