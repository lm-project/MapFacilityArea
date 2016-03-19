package com.autonavi.mapart.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.sun.jna.ptr.DoubleByReference;

public class OptmapTest {

	@Test
	public void testReversemap() {
		DoubleByReference rx = new DoubleByReference();
		DoubleByReference ry = new DoubleByReference();
		Optmap.INSTANCE.reversemap(61.0, 136.2, rx, ry);
		assertEquals("", rx.getValue());
	}

}
