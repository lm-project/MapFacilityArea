package com.autonavi.mapart.orm;

import com.autonavi.mapart.entity.ResponseStatus;

/**
 * <p>
 * desc:地图参考ORM接口
 * <p>
 * Copyright: Copyright(c)AutoNavi 2014
 * </p>
 * 
 * @author <a href="mailTo:i-caiqiang@autonavi.com">i-caiqiang</a>
 * @time 2014-4-24 10:33
 * 
 */
public interface ArtDao {
	
	/***
	 * 查询Baidu设施区域
	 * @param key 查询关键字
	 * @return Status 返回值
	 */
	public ResponseStatus getBaiduMapresult(String key);
	
	/***
	 * 查询QQ设施区域
	 * @param key 查询关键字
	 * @return Status 返回值
	 */
	public ResponseStatus getGetQQMapresult(String key);
	
	/***
	 * 查询AmapPOI信息
	 * @param key 查询关键字
	 * @return Status 返回值
	 */
	public ResponseStatus getAmapresult(String key);
}
