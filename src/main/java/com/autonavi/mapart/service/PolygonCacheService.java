package com.autonavi.mapart.service;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.autonavi.mapart.entity.Anf_hicaicaibian;
import com.autonavi.mapart.entity.FapoiRelation;
import com.autonavi.mapart.entity.ReferenceGeom;
import com.autonavi.mapart.entity.ResponseStatus;
import com.autonavi.mapart.orm.Anf_hicaicaibianDao;
import com.autonavi.mapart.orm.FacilityareaDao;
import com.autonavi.mapart.orm.FapoiRelationDao;
import com.autonavi.mapart.orm.ReferenceGeomDao;
import com.autonavi.mapart.orm.impl.FapoiRelationDaoImpl;
import com.autonavi.mapart.util.ApiRequest;
import com.autonavi.mapart.util.GetAmapresult;
import com.autonavi.mapart.util.GetBaiduMapresult;
import com.autonavi.mapart.util.GetGeometry;
import com.autonavi.mapart.util.GetQQMapresult;
import com.autonavi.mapart.util.ImportFapoiRelation;
import com.autonavi.mapart.util.ReadExcel;
import com.autonavi.mapart.util.XidRequest;
public class PolygonCacheService {

	Log log = LogFactory.getLog(getClass());
	private final int SECOND = 1000;
	private final int TEN_MINS = 10 * 60;

	@Autowired
	private ArtService artService;
	@Autowired
	private TaskitemService taskitemService;
	@Autowired
	private ReferenceGeomDao referenceGeomDao;
	@Autowired
	private FapoiRelationDao fapoiRelationDao;
	@Autowired
	private FacilityareaDao facilityareaDao;
	@Autowired
	private Anf_hicaicaibianDao anf_hi;	
	@Autowired
	private FapoiRelationDaoImpl dao;
	public void init(){
		CmsRequest.startMsgServer();//开启web服务
		Calendar calendar = Calendar.getInstance();  
		calendar.set(Calendar.HOUR_OF_DAY, 13);  
		calendar.set(Calendar.MINUTE,0);  
		calendar.set(Calendar.SECOND, 0);
		log.debug("每天在  "+calendar.getTime()+"  启动抓取xid");		
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				updateXidOrPoi_id();	
			}
			
		},calendar.getTime());
		
	}
//	private void updateXidOrPoi_id(){
//		dao.insertRelations(ReadExcel.readXlsx("F:\\设施区域类型编码.xlsx"));
//	}
	
	
	private void updateXidOrPoi_id(){
		int fa_id = facilityareaDao.getLastRecordFa_id();
		List<Anf_hicaicaibian> list = facilityareaDao.getMeshIdAndPoiId();
		log.debug("----需要获取xid 的数量 -----"+list.size()+"   ,最大流水号是："+fa_id);
		String queryStr = "queryStr="+JSONArray.fromObject(list).toString().
				replaceAll("\"callbacks\":\\[\\{\\}\\],", "").replaceAll("mesh", "meshid");
		log.debug(queryStr);
		String result = XidRequest.sendPost(queryStr);
		log.debug("-----------------------------------------------------");
		log.debug(result);
		JSONArray jsonArray = JSONArray.fromObject(result);  
		for(int i = 0;i<jsonArray.size();i++){
			JSONObject json = JSONObject.fromObject(jsonArray.get(i));
			int x_id = json.getInt("x_id");
			int poi_id = json.getInt("poi_id");
			log.debug("x_id =============="+x_id+"   poi_id==========="+poi_id);
			if(x_id==0||poi_id==0){
				continue;
			}
			log.debug("************************************************");
			facilityareaDao.updateXidByGid(
					new Anf_hicaicaibian(x_id,json.getString("meshid"),poi_id,json.getInt("gid"),++fa_id));
		}
	}
	public ResponseStatus  getALLReferenceGeom() {
		Collection<ReferenceGeom>  collection = referenceGeomDao.findCachedGeoms("baidu");
		System.out.println("collenction:"+collection.size());
		return new ResponseStatus(100, JSONArray.fromObject(collection).toString(), "");
	}
	
	public ResponseStatus readBd(String key) {
		return read(key, Type.baidu,new GetBaiduMapresult());
	}

	public ResponseStatus readQQ(String key) {
		return read(key, Type.qq,new GetQQMapresult());
	}

	public ResponseStatus readAutonavi(String key) {
		try {
			ResponseStatus rs = read(key, Type.autonavi, new GetAmapresult());
			if(rs.getCode() == 100){
				log.info("<----------key:"+ key +",poi info:"+rs.getRestring()+"---------->");
				log.debug("获取fa_poi_relation表中的所有信息");
				Map<String,String> allFatypes = fapoiRelationDao.getAllFatypes();
				rs.setRestring(getFatypeByPoitype(rs.getRestring(),allFatypes));
			}
			return rs;
		} catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	private ReferenceGeom readCache(String key1, Type type) {
		ReferenceGeom geom = new ReferenceGeom();
		geom.setKey(key1);
		geom.setType(type.toString());
		return referenceGeomDao.select(geom);
	}
	
	private ResponseStatus read(String key, Type type,ApiRequest api) {
		log.debug("已经缓存过了吗？ =========="+key+"    "+type);
		ReferenceGeom cachedGeom = readCache(key,type);
		if (cachedGeom == null||cachedGeom.getContext().equals("")) {
			log.debug("NO cached and cache key .... " + key + ",type:" + type);
			ResponseStatus rs = api.select(key);
			cacheData(type, key, rs);
			return rs;
		} else {
			log.debug(type+":have cache -----:"+cachedGeom.getContext());
			return new ResponseStatus(100,cachedGeom.getContext(),""); 
		}
	}

	/**
	 * 根据poi信息查询对应的Fa_type
	 * 
	 * @param string
	 *            请求到的poi信息
	 */
	protected String getFatypeByPoitype(String poiString,Map<String,String> allFatypes) {
		log.debug("-------------------------------根据poi信息查询对应的Fa_type息----------------------------------");
		log.debug("抓取的poi信息："+poiString);
		log.debug("fapoiRelation表中的信息："+allFatypes.toString());
		String[] pois = poiString.split(","); // 查询到的poi
		String poi_typecode = "";
		String[] poiInfo = new String[5];
		// 逐一处理每个poi
		log.debug("高德抓取的 poi信息长度："+pois.length);
		for (int i = 0; i < pois.length; i++) {
			poiInfo = pois[i].split("_");
			if( poiInfo.length > 2 ){
				poi_typecode = poiInfo[2]; // 读取poi typecode    
				try{
					poiInfo[2] = allFatypes.get(poi_typecode)==null ? "":allFatypes.get(poi_typecode);
					log.debug("根据poi_typecode："+poi_typecode+"查询fa_poi_relation中的fa_type：  "+poiInfo[2]+"此poi的信息为："+allFatypes.get(poi_typecode));
				} catch(Exception e){
					log.debug("计算fa_type出错："+e.getMessage());
					e.printStackTrace();
				}
				pois[i] = StringUtils.join(poiInfo, "_");
			}
		}
		return StringUtils.join(pois, ",");
		
	}
	
	
	private void cacheData(Type type, String name, ResponseStatus rs) {
		log.error("类型:"+type + "  名称:"+name+"  Restring:"+rs.getRestring());
		try {
			if (StringUtils.isNotBlank(rs.getRestring())&&rs.getCode() == 100&&rs!=null) {
				ReferenceGeom geom = new ReferenceGeom();
				geom.setKey(name);
				geom.setType(type.toString());
				geom.setContext(rs.getRestring());
				if( ! type.equals(Type.autonavi)) {
					final boolean isSecond = false;
					final boolean isReverse = false;
//					final String strGeo = type.equals(Type.baidu)? 
//							GetGeometry.getBaiduLngLat(rs.getRestring()):rs.getRestring();
					geom.setGeom(GetGeometry.getGeometry(rs.getRestring(), isReverse, GetGeometry.POLYGON, isSecond));
				}
				referenceGeomDao.insert( geom );
			}
		} catch( Exception e) {
			e.printStackTrace();
			log.error("插入reference_geom 时的异常信息："+type + ":"+rs.getRestring());
			return;
		}
	}

	
	
	public void update(final ApiRequest api, final Type type,Collection<String> names) {
		Random r = new Random();
		int nameNum = 0;
		final int sleepIntervel = 5;
		final int batchSize = 100;
		for (final String name : names) {
			int sleepTime = r.nextInt( sleepIntervel );
			sleepTime = sleepTime == 0 ?  sleepIntervel :  sleepTime;
			if( (++nameNum) % batchSize == 0){
				sleepTime = TEN_MINS;
			}
			log.debug("sleep time :" + sleepTime);
			try {
				cacheData(type, name, api.select(name));
				Thread.sleep(sleepTime * SECOND);
			} catch (InterruptedException e) {
				e.printStackTrace();
				
			}
		}
	}
	public void updateAll(final Collection<String> all) {
		Calendar calendar = Calendar.getInstance();  
		calendar.set(Calendar.HOUR_OF_DAY, 23);  
		calendar.set(Calendar.MINUTE, 38);  
		calendar.set(Calendar.SECOND, 0);
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				//updateAutonavi(all);
				updateBdAll(all);
				//updateTxAll(all);
			}
//		},calendar.getTime());
		},5 * 1000);
	}

	public void updateBdAll(final Collection<String> all) {
		new Thread(new Runnable() {
			public void run() {
				update(new GetBaiduMapresult(), Type.baidu,all);
			}
		}).start();
	}
	
	public void updateAutonavi(final Collection<String> all) {
	
		new Thread(new Runnable() {
			public void run() {
				update(new GetAmapresult(), Type.autonavi,all);
			}
		}).start();
	}

	public void updateTxAll(final Collection<String> all) {
		new Thread(new Runnable() {
			public void run() {
				update(new GetQQMapresult(), Type.qq,all);
			}
		}).start();
	}
	
	class PolygonCache implements Serializable {
		private static final long serialVersionUID = 1L;
		ResponseStatus status;
	}

	static enum Type {
		baidu, qq, autonavi
	}
}