package com.autonavi.mapart.util;

import java.io.File;

import org.junit.Test;

public class TarUtilTest {

	@Test
	public void test() {
		TarUtil.pack(new File[] { new File(
				"/Users/cbin/toys/findfun/MapFacilityArea/src/test/java/com/autonavi/mapart/util/TarUtilTest.java") },
				new File("a.tar"));
	}
}
