package com.autonavi.mapart.mapper;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.autonavi.mapart.entity.Facilityachieve;
import com.autonavi.mapart.entity.ResponseStatus;
import com.autonavi.mapart.service.AnShpService;
import com.autonavi.mapart.service.ArtService;
import com.autonavi.mapart.service.FacilityachieveService;
import com.autonavi.mapart.service.PolygonCacheService;
import com.autonavi.mapart.service.TaskService;
import com.autonavi.mapart.service.TaskitemService;
import com.autonavi.mapart.util.Utils;

/**
 * <p>
 * desc:地图参考控制器 ,poi,polygon等api的查询
 * <p>
 * Copyright: Copyright(c)AutoNavi 2014
 * </p>
 * 
 * @author <a href="mailTo:i-caiqiang@autonavi.com">i-caiqiang</a>
 * @time 2014-3-28 13:33
 * 
 */

@Controller
public class ArtMapper {

	@Autowired
	private ArtService artService;
	
	@Autowired
	private PolygonCacheService polygonCacheService;
	
	@Autowired
	private TaskService taskService;

	@Autowired
	private TaskitemService taskitemService;
	
	@Autowired
	private FacilityachieveService facilityachieveService;

	@Autowired
	private AnShpService anShpService;
	
	Logger log = Logger.getLogger(getClass());
	
	@RequestMapping(value = "/polygon/search/autonavi", method = RequestMethod.POST)
	public @ResponseBody
	ResponseStatus getAnShp(HttpServletRequest request,@RequestParam("searchKey") String key) {
		Facilityachieve anShape = anShpService.getAnShapeByName(key);
		return new ResponseStatus(100, anShape == null ? "" : anShape.getGeom() , "success");
	}

	@RequestMapping(value = "/polygon/searcher", method = RequestMethod.POST)
	public @ResponseBody
	ResponseStatus search1(@RequestParam("searchKey") String key) {
		ResponseStatus s1 = polygonCacheService.readBd(key);
		ResponseStatus s2 = polygonCacheService.readQQ(key);
		ResponseStatus resultStatus = new ResponseStatus();
		
		if (s1.getCode() == 100 || s2.getCode() == 100) {
			resultStatus.setCode(100);
			if (s1.getCode() == 100 && s2.getCode() == 100) {
				resultStatus.setRestring(s1.getRestring() + ";"
						+ s2.getRestring());
				resultStatus.setDescription(s1.getDescription());
			} else if (s1.getCode() == 100) {
				resultStatus.setRestring(s1.getRestring() + ";");
				resultStatus.setDescription(s1.getDescription());
			} else {
				resultStatus.setRestring(";" + s2.getRestring());
				resultStatus.setDescription(s2.getDescription());
			}
		} else if ((s1.getCode() == 200 && s2.getCode() == 200)
				|| (s1.getCode() == 300 && s2.getCode() == 300)) {
			return s1;
		}
		return resultStatus;
	}

	@RequestMapping(value = "/poi/searcher", method = RequestMethod.POST)
	public @ResponseBody ResponseStatus search2(@RequestParam("searchKey") String key) {
		return polygonCacheService.readAutonavi(key);
	}
	
	@RequestMapping(value = "/checkPolygon", method = RequestMethod.POST)
	public @ResponseBody ResponseStatus checkPolygon(@RequestParam("polygon") String polygon) {
		log.debug("检查的polygon字符串：  "+polygon);
		String retCode = Utils.checkPolygon(polygon);
		log.debug("check info : "+retCode);
		return new ResponseStatus(100,retCode);
	}
}

