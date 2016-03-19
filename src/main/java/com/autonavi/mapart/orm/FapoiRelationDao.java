/**
 * 
 */
package com.autonavi.mapart.orm;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.autonavi.mapart.entity.FapoiRelation;

/**
 * 设施区域与POI对应关系 处理借口
 * @author huandi.yang
 *
 */
public interface FapoiRelationDao {
	
	/**
	 * 插入对应关系数据
	 * @param list
	 */
	void insertRelations(List<FapoiRelation> list) throws DataAccessException;
	
	/**
	 * 删除所有数据
	 */
	void deleteALl() throws DataAccessException;
	
	/**
	 * 根据poi类型编码查询对应fa_type
	 * @param poi_typecode
	 * @return
	 */
	Map<String,String> getAllFatypes();

	/**
	 * 查询对应关系
	 * @param params
	 * @return
	 */
	List<FapoiRelation> getFapoiRelationList(Map<String, Object> params);

	/**
	 * 更新对应关系
	 * @param relation
	 */
	void update(FapoiRelation relation) throws DataAccessException;

	/**
	 * 查询设施区域大类
	 * @return
	 * @throws DataAccessException
	 */
	List<String> getFacategory() throws DataAccessException;

	/**
	 * 根据设施区域大类查询对应中类列表
	 * @param fa_category
	 * @return
	 * @throws DataAccessException
	 */
	List<String> getFatypeList(Map<String, Object> params) throws DataAccessException;
	
	/**
	 * 根据设施区域中类(Fa_type)查询对应POI类型列表
	 * @param fa_category
	 * @return
	 * @throws DataAccessException
	 */
	List<String> getPoitypeList(Map<String, Object> params) throws DataAccessException;

}
