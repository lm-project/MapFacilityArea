package com.autonavi.mapart.orm.impl;

import com.autonavi.mapart.entity.ResponseStatus;
import com.autonavi.mapart.orm.ArtDao;
import com.autonavi.mapart.util.GetAmapresult;
import com.autonavi.mapart.util.GetBaiduMapresult;
import com.autonavi.mapart.util.GetQQMapresult;

public class ArtDaoImpl implements ArtDao{
	
	@Override
	public ResponseStatus getBaiduMapresult(String key){
		ResponseStatus status = new GetBaiduMapresult().select(key);
		return status;
	}
	
	@Override
	public ResponseStatus getGetQQMapresult(String key){
		ResponseStatus status = new GetQQMapresult().select(key);
		return status;
	}
	
	@Override
	public ResponseStatus getAmapresult(String key){
		ResponseStatus status = new GetAmapresult().select(key);
		return status;
	}

}
