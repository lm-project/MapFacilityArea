/**
 * 
 */
package com.autonavi.mapart.service;

import java.util.List;

import com.autonavi.mapart.entity.DaoResult;
import com.autonavi.mapart.entity.FapoiRelation;

/**
 * 设施区域与POI类型对应关系 service层借口
 * 
 * @author huandi.yang
 * 
 */
public interface FapoiRelationService {

	/**
	 * 更新对应关系
	 * 
	 * @param filepath
	 * @return
	 */
	DaoResult updateRelation(String filepath);

	/**
	 * 查询对应关系列表
	 * @param fa_category
	 * @param fa_type
	 * @param poi_type
	 * @return
	 */
	List<FapoiRelation> getFapoiRelationList(String fa_category, String fa_type,
			String poi_type);

	/**
	 * 更新对应关系
	 * @param relation
	 */
	void update(FapoiRelation relation);

	/**
	 * 查询设施区域大类
	 * @return
	 */
	List<String> getFacategory();

	/**
	 * 根据设施区域大类查询对应中类(Fa_type)列表
	 * @param fa_category
	 * @return
	 */
	List<String> getFatypeList(String fa_category);

	/**
	 * 根据设施区域中类(Fa_type)查询对应POI类型列表
	 * @param fa_category
	 * @return
	 */
	List<String> getPoitypeList(String fa_type);

	/**
	 * 新增对应关系
	 * @param relation
	 */
	void newRelation(FapoiRelation relation);

}
