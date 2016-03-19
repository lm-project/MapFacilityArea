package com.autonavi.mapart.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.autonavi.mapart.entity.Facilityachieve;
import com.autonavi.mapart.entity.Qaresult;
import com.autonavi.mapart.entity.ResponseStatus;
import com.autonavi.mapart.orm.ArtDao;
import com.autonavi.mapart.orm.FacilityareaDao;
import com.autonavi.mapart.orm.TaskitemDao;
import com.autonavi.mapart.service.ArtService;
import com.autonavi.mapart.util.ExecuteMultiPolygon;


@Service
public class ArtServiceImpl implements ArtService{

	@Autowired
	private ArtDao artDao;
	
	@Autowired
	private FacilityareaDao facDao;
	
	@Autowired
	private TaskitemDao taskitemDao;
	
	
	private Logger  log = Logger.getLogger(getClass());
	@Override
	public ResponseStatus getBaiduMapresult(String key){
		return artDao.getBaiduMapresult(key);
	}
	
	@Override
	public ResponseStatus getGetQQMapresult(String key){
		return artDao.getGetQQMapresult(key);
	}
	
	@Override
	public ResponseStatus getAmapresult(String key){
		return artDao.getAmapresult(key);
	}
	
	@Override
	public List<Facilityachieve> getPolygonByBound(String bounds) {
		if (bounds.indexOf("&") > -1) {
			bounds.replace("&", ";");
		}
		return ExecuteMultiPolygon.execute( facDao.getPolygonByBound( bounds ) );
	}
	@Override
	public void insertFacilityarea(Facilityachieve fac) throws DataAccessException {
		Facilityachieve oldFac = facDao.getFacility(fac.getTaskitem_id());
		if(oldFac==null){
			fac.setCondition(0);	
			fac.setX_id(0);
			fac.setFa_id(1);
			facDao.insert(fac);
		}else{
			int x_id = 0,fa_id=1;
			int release = oldFac.getRelease();
			int condition = oldFac.getCondition();
			if(fac.getPoi_id()==oldFac.getPoi_id()&&fac.getMesh().equals(oldFac.getMesh())){
				x_id = oldFac.getX_id();	
				log.debug("xid_________________________"+x_id);
			}
			if(x_id != 0){
				fa_id = facDao.getLastRecordFa_id();
			}
			if(release==0){
				log.debug("更新此任务项");
				fac.setX_id(x_id);
				if(condition==2||condition==1){
					condition=1;
				}
				fac.setCondition(condition);
				fac.setGid(oldFac.getGid());
				fac.setFa_id(fa_id);
				log.debug("最后的数据为： condition:"+fac.getCondition()+",x_id："+fac.getX_id()+",fa_id:"+fa_id);
				facDao.updateFacilityByGid(fac);		
			}else{
				log.debug("插入一条新的数据，condition状态为修改1");
				fa_id = fa_id==1 ? fa_id:fa_id+1;
				fac.setFa_id(fa_id);
				if(condition==2){
					fac.setCondition(0);	
				}else{
					fac.setCondition(1);
				}
				fac.setX_id(x_id);
				facDao.insert(fac);
			}
		}
	}

	
	
	
	@Override
	public void insertQaresult(Qaresult[] fa) {
		facDao.insertQaresult( fa );
		
	}
	
	@Override
	public List<Qaresult> getPolylineByBound(String bounds) {
		return ExecuteMultiPolygon.tranformQa( facDao.getPolylineByBound(bounds));
	}

	@Override
	public List<Qaresult> getQaresult(int taskitemid) {
		return ExecuteMultiPolygon.tranformQa(facDao.getQaresult(taskitemid));
	}

	@Override
	public void deleteTaskItem(int taskItemId) {
		Facilityachieve oldFac = facDao.getFacility(taskItemId);
		if(oldFac==null){
			return;
		}else{
			if(oldFac.getRelease()==1){
				log.debug("已经入cms库，新增一条记录condition 状态为删除：2,release状态为：0");
				oldFac.setCondition(2);
				oldFac.setGeom("MULTIPOLYGON(((0 0,0 0,0 0,0 0)))");
				oldFac.setFa_id(facDao.getLastRecordFa_id()+1);
				facDao.insert(oldFac);
			}else{
				log.debug("未入cms库，删掉此条记录");
				
				if(oldFac.getCondition()==0){
					facDao.deleteGid(oldFac.getGid());
				}else{
					oldFac.setCondition(2);
					oldFac.setGeom("MULTIPOLYGON(((0 0,0 0,0 0,0 0)))");
					facDao.updateFacilityByGid(oldFac);
				}
			}
		}
	}
}
