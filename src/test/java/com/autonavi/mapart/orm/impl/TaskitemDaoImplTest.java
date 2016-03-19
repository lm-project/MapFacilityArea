package com.autonavi.mapart.orm.impl;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.autonavi.mapart.orm.TaskitemDao;
import com.autonavi.mapart.service.BaseTestCase;

public class TaskitemDaoImplTest extends BaseTestCase {

	@Autowired
	private TaskitemDao dao;
	@Test
	public void testGetUnReceived() {
		int qaUnreceiveUnmber = dao.getQaUnreceiveUnmber("台州市","台州市",null);
		assertEquals(1, qaUnreceiveUnmber);
	}

}
