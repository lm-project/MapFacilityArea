package com.autonavi.mapart.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autonavi.mapart.entity.DaoResult;
import com.autonavi.mapart.entity.FapoiRelation;
import com.autonavi.mapart.orm.FapoiRelationDao;
import com.autonavi.mapart.service.FapoiRelationService;
import com.autonavi.mapart.util.ImportFapoiRelation;

@Service
public class FapoiRelationServiceImpl implements FapoiRelationService {
	
	@Autowired
	private FapoiRelationDao fapoiRelationDao;

	@Override
	public DaoResult updateRelation(String filepath) {
		List<FapoiRelation> list;
		try {
			list = ImportFapoiRelation.readRelationExcel(filepath);
			//先清空表中数据
			fapoiRelationDao.deleteALl();
			//插入数据
			fapoiRelationDao.insertRelations(list);
		} catch (IOException e) {
			e.printStackTrace();
			return new DaoResult(200, "上传文件出错，请选择正确的上传文件!");
		}
		return new DaoResult(100, "上传文件成功!");
	}

	@Override
	public List<FapoiRelation> getFapoiRelationList(String fa_category, String fa_type,
			String poi_type) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("fa_category", fa_category);
		params.put("fa_type", fa_type);
		params.put("poi_type", poi_type);
		return fapoiRelationDao.getFapoiRelationList(params);
	}

	@Override
	public void update(FapoiRelation relation) {
		fapoiRelationDao.update(relation);
		
	}

	@Override
	public List<String> getFacategory() {
		return fapoiRelationDao.getFacategory();
	}

	@Override
	public List<String> getFatypeList(String fa_category) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("fa_category", fa_category);
		return fapoiRelationDao.getFatypeList(params);
	}

	@Override
	public List<String> getPoitypeList(String fa_type) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("fa_type", fa_type);
		return fapoiRelationDao.getPoitypeList(params);
	}

	@Override
	public void newRelation(FapoiRelation relation) {
		List<FapoiRelation> list = new ArrayList<FapoiRelation>();
		list.add(relation);
		fapoiRelationDao.insertRelations(list);
		
	}

}
