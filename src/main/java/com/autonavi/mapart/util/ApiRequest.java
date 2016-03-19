package com.autonavi.mapart.util;

import com.autonavi.mapart.entity.ResponseStatus;

public interface ApiRequest {
	ResponseStatus select(String key,String... types);
}
