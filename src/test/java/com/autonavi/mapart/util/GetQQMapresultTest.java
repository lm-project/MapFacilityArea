package com.autonavi.mapart.util;

import junit.framework.Assert;

import org.junit.Test;

import com.autonavi.mapart.entity.ResponseStatus;

public class GetQQMapresultTest {

	@Test
	public void testSelect() {
		ResponseStatus result = new GetQQMapresult().select("故宫博物院");
		System.out.println(result.toString());
		
		Assert.assertEquals(100, result.getCode());
	}

}
