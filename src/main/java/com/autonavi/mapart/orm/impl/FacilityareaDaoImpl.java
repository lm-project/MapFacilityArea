package com.autonavi.mapart.orm.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.autonavi.mapart.entity.Anf_hicaicaibian;
import com.autonavi.mapart.entity.CmsData;
import com.autonavi.mapart.entity.Facilityachieve;
import com.autonavi.mapart.entity.FapoiRelation;
import com.autonavi.mapart.entity.Qaresult;
import com.autonavi.mapart.orm.FacilityareaDao;
import com.autonavi.mapart.util.ExecuteMultiPolygon;
import com.autonavi.mapart.util.GetGeometry;

public class FacilityareaDaoImpl extends BasicMyBatisDao implements FacilityareaDao {

	@Override
	public Facilityachieve getFacilityarea(Facilityachieve fa)
			throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int insert(Facilityachieve geom) throws DataAccessException {
		return super.save("facilityachieveMapper.insert", geom);
	}

	
	@Override
	public void deleteFacilityarea(String gids) throws DataAccessException {
		int[] array = convertString2ArrayInt(gids, "&");
		super.delete("facilityachieveMapper.delete", array);
	}
	
	@Override
	public void deleteGid(int gid) {
		super.delete("facilityachieveMapper.deleteGid",gid);
		// TODO Auto-generated method stub
		
	}
	@Override
	public List<Facilityachieve> getFacilityareaList(String task_item_id)
			throws DataAccessException {
		int[] array = convertString2ArrayInt(task_item_id, ",");
		return super.getList("facilityachieveMapper.getFacilityList", array);
	}

	@Override
	public List<Facilityachieve> getPolygonByBound(String bounds) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("fa_type", "");
		map.put("meshs", "");
		map.put("bounds", ExecuteMultiPolygon.execute(bounds));
		return super.getList("facilityachieveMapper.selectPolygonByBounds", map);
	}

	@Override
	public Facilityachieve getFacility(int taskitemId) throws DataAccessException {
		System.out.println("________________________________");
		return super.get("facilityachieveMapper.getFacilityByTaskitem", taskitemId);
	}
	
	@Override
	public Facilityachieve getFacilityByName(String name) {
		return super.get("facilityachieveMapper.getFacilityByName", name);
	}

	@Override
	public void updateByTaskitemId(Facilityachieve fac) {
		super.save("facilityachieveMapper.updateByTaskitemId", fac);
	}

	@Override
	public void creatTmpTable(String tmpTableName,String param, String type) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tableName", tmpTableName);
		
		//按任务导出
		if ("task".equals(type)){
			int[] array = convertString2ArrayInt(param, "&");
			params.put("taskids", array);
			super.update("facilityachieveMapper.createTmpTableNameById",params);
		}
		//按项目导出
		else if("project".equals(type)){
			params.put("projectname", param);
			super.update("facilityachieveMapper.createTmpTableNameByName",params);
		}
		//导出质检数据
		else if("qaresult".equals(type)){
			params.put("projectname", param);
			super.update("qaresultMapper.createTmpTableNameByName",params);
			try{
				cleanQaDulpData(tmpTableName);
			}catch(Exception e) {
				e.printStackTrace();
				logger.error("Clean qa dulp data fail:" + e.getMessage());
			}
		}
	}
	
	private void cleanQaDulpData(String qaTname) throws SQLException {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("qa_result",qaTname);
		
		super.delete("qaresultMapper.deleteWrongQa", map);

		Map<String, String> plygon2id = new HashMap<String, String>();
		Collection<Integer> ids = new ArrayList<Integer>();
		List<Qaresult> rs2 = super.getList("qaresultMapper.queryDulpQa", map);
		for(Qaresult s: rs2){
			String id = String.valueOf(s.getId());
			String ply = s.getReverse_qamark();
			if (plygon2id.containsKey(ply)) {
				continue;
			}
			plygon2id.put(ply, id);
			ids.add(s.getId());
		}
		
		if(ids.isEmpty()) {
			return;
		}
		map.put("items",ids);
		super.delete("qaresultMapper.deleteDulpQa", map);
	}
	private int[] convertString2ArrayInt(String ids, String split) {
		String[] arrays = ids.split(split);
		int array[] = new int[arrays.length];  
		for(int i=0;i<arrays.length;i++){  
//			System.out.println(arrays[i]);
		    array[i]=Integer.parseInt(arrays[i]);   
		}
		return array;
	}

	@Override
	public void deleteTmpTable(String tmpTableName) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tableName", tmpTableName);
		super.update("facilityachieveMapper.deleteTmpTable", params);
	}
	
	@Override
	public int getDataCount(String tmpTableName) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tableName", tmpTableName);
		return (Integer)super.get("facilityachieveMapper.getDataCount", params);
	}

	@Override
	public void insertQaresult(Qaresult[] fa) {
		super.save("qaresultMapper.insert", fa);
	}

	@Override
	public List<Qaresult> getPolylineByBound(String bounds) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("bounds", ExecuteMultiPolygon.execute(bounds));
		return super.getList("qaresultMapper.getPolylineByBound", map);
	}

	@Override
	public List<Qaresult> getQaresult(int taskitemid) {
		return super.getList("qaresultMapper.getQaresult", taskitemid);
	}

	@Override
	public int getAreaFlag(String path) throws DataAccessException {
		final boolean isSecond = false;
		final boolean isReverse = false;
		String polygon = GetGeometry.getGeometry(path, isReverse, GetGeometry.POLYGON, isSecond);
		try {
			return super.get("facilityachieveMapper.getAreaFlag", polygon);
		} catch (Exception e){
			logger.error("获取AreaFlag的异常："+e.getMessage());
			return 0;
		}
	}

	@Override
	public void updateFatype(FapoiRelation relation) throws DataAccessException {
		String fa_type = relation.getFa_type().substring(0, 4);
		relation.setFa_type(fa_type);
		super.update("facilityachieveMapper.updateFatype", relation);
		
	}

	@Override
	public List<Anf_hicaicaibian> getMeshIdAndPoiId() {
		return super.getList("facilityachieveMapper.getMesh_poiId");
	}

	@Override
	public void updateXidByGid(Anf_hicaicaibian anf) {
		super.update("facilityachieveMapper.updateXidByGid", anf);
		
	}

	@Override
	public List<CmsData> getCmsData(int[] fa_ids) {
		return super.getList("facilityachieveMapper.selectCMS",fa_ids);
	}

	@Override
	public void updateReleaseByGid(List<CmsData> list) {
		super.update("facilityachieveMapper.updateReleaseByFa_id", list);
		
	}
	@Override
	public void updateFacilityByGid(Facilityachieve oldFac) {
		super.update("facilityachieveMapper.updateFacilityByGid", oldFac);
		
	}
	@Override
	public int getLastRecordFa_id() {
		return super.get("facilityachieveMapper.selectLastFa_id","");
	}


}
