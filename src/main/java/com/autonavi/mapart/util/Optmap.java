package com.autonavi.mapart.util;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.ptr.DoubleByReference;

public interface Optmap extends Library {

	Optmap INSTANCE = (Optmap) Native.loadLibrary("/lib64/liboptmap_x64.so", Optmap.class);

	void reversemap(double x, double y, DoubleByReference rx, DoubleByReference ry);
}
