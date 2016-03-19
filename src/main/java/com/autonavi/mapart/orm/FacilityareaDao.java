package com.autonavi.mapart.orm;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.autonavi.mapart.entity.Anf_hicaicaibian;
import com.autonavi.mapart.entity.CmsData;
import com.autonavi.mapart.entity.Facilityachieve;
import com.autonavi.mapart.entity.FapoiRelation;
import com.autonavi.mapart.entity.Qaresult;

/**
 * <p>
 * desc:设施区域处理接口
 * <p>
 * Copyright: Copyright(c)AutoNavi 2014
 * </p>
 * 
 * @author <a href="mailTo:i-caiqiang@autonavi.com">i-caiqiang</a>
 * @time 2014-4-24 10:33
 * 
 */
public interface FacilityareaDao {
	/**
	 * <p>
	 * desc: 关键字获取设施区域
	 * <p>
	 * @param fa
	 * @return Facilityachieve
	 * @throws DataAccessException
	 */
	Facilityachieve getFacilityarea(Facilityachieve fa) throws DataAccessException;

	Facilityachieve getFacility(int taskitemId) throws DataAccessException;
	/**
	 * <p>
	 * desc: 作业新增设施区域
	 * <p>
	 * 
	 * @param fa
	 * @return int
	 * @throws DataAccessException
	 */
	int insert(Facilityachieve fa) throws DataAccessException;

	/**
	 * <p>
	 * desc: 删除设施区域
	 * <p>
	 * 
	 * @param gids
	 * @throws DataAccessException
	 */
	void deleteFacilityarea(String gids) throws DataAccessException;
	/**
	 * 
	 * 根据gid删除记录
	 */
	public void deleteGid(int gid);
	
	/**
	 * <p>
	 * desc: 查询设施区域
	 * <p>
	 * 
	 * @return List<Facilityachieve>
	 * @throws DataAccessException
	 */
	List<Facilityachieve> getFacilityareaList(String id) throws DataAccessException;
	
	/**
	 * 查询facilityarea库中的面记录
	 * @param bounds
	 * @return 
	 */
	public List<Facilityachieve> getPolygonByBound(String bounds);

	void updateByTaskitemId(Facilityachieve fac);
	
	/**
	 * 创建临时表(导出shape)
	 * @param tmpTableName
	 * @param param
	 * @param type
	 */
	void creatTmpTable(String tmpTableName,String param, String type);

	/**
	 * 删除临时表(导出shape)
	 * @param tmpTableName 
	 */
	void deleteTmpTable(String tmpTableName);

	/**
	 * 获取临时表数据数目
	 * @param tmpTableName
	 * @return
	 */
	int getDataCount(String tmpTableName);

	void insertQaresult(Qaresult[] fa);
	

	List<Qaresult> getPolylineByBound(String bounds);

	List<Qaresult> getQaresult(int taskitemid);

	/**
	 * 根据polygon查询area_flag
	 * @param path
	 * @return
	 * @throws DataAccessException
	 */
	int getAreaFlag(String path) throws DataAccessException;

	/**
	 * 更新fa_type
	 * @param relation
	 * @throws DataAccessException
	 */
	void updateFatype(FapoiRelation relation) throws DataAccessException;
	/**
	 * 获取meshid 和 poi_id查询x_id
	 */
	List<Anf_hicaicaibian> getMeshIdAndPoiId();
	/**
	 * 
	 * 更新xid
	 */
	void updateXidByGid(Anf_hicaicaibian anf_hicaicaibian);
	/**
	 *入cms库
	 * @param gids
	 * @return
	 */
	List<CmsData> getCmsData(int[] gids);
	/**
	 * 更新facility根据gid
	 * @param oldFac
	 */
	public void updateFacilityByGid(Facilityachieve oldFac) ;
	
	/**
	 * 查询最后一条记录的id
	 * 
	 */
	int getLastRecordFa_id();
	
	
	/**
	 * 入cms库后改变release状态为1；
	 * 
	 */
	void updateReleaseByGid(List<CmsData> list);
	
	/**
	 * 
	 * 通过name查询本地设施区域
	 * @param string
	 * @return
	 */

	Facilityachieve getFacilityByName(String string);
}
