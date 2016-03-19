package com.autonavi.mapart.mapper;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.autonavi.mapart.entity.Facilityachieve;
import com.autonavi.mapart.entity.Qaresult;
import com.autonavi.mapart.entity.ResponseStatus;
import com.autonavi.mapart.entity.TTStatus;
import com.autonavi.mapart.entity.Taskitem;
import com.autonavi.mapart.service.ArtService;
import com.autonavi.mapart.service.FacilityachieveService;
import com.autonavi.mapart.service.TaskService;
import com.autonavi.mapart.service.TaskitemService;

/**
 * 
 * 设施区域成果，保存，查询，导出
 */
@Controller
public class FacilityMapper {
	
	@SuppressWarnings("unused")
	private Logger log = Logger.getLogger(FacilityMapper.class);

	@Autowired
	private ArtService artService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private TaskitemService taskitemService;

	@Autowired
	private FacilityachieveService facilityachieveService;
	
	@RequestMapping(value = "/facility/save", method = RequestMethod.POST)
	public @ResponseBody ResponseStatus save(HttpServletRequest request,
										@RequestParam("facility") String facility) {
		log.debug("=======================================================");
		log.debug(facility);
		int uid = MapperCommonUtil.getUserid(request);
		if (uid == -1) {
			ResponseStatus status = new ResponseStatus(300, "你还未登陆,访问被拒绝!", "index");
			return status;
		}

		Facilityachieve fa = Facilityachieve.createFromJson(JSONObject.fromObject(facility));
		log.debug(fa.toString());
		artService.insertFacilityarea(fa);
		Taskitem taskitem = new Taskitem();
		taskitem.setStatus(TTStatus.SAVE);
		taskitem.setId(fa.getTaskitem_id());
		taskitem.setDel_status(0);
		taskitem.setOpuserid(uid);
		taskitemService.updateDelete(taskitem);
		return new ResponseStatus(100,"设施区域保存成功");
	}
	
	@RequestMapping(value = "/facility/delete", method = RequestMethod.POST)
	public @ResponseBody ResponseStatus delete(HttpServletRequest request,
									@RequestParam("taskitemid") int taskitemid) {
		log.debug("-------------------删除任务项-----------"+taskitemid);
		String uid = request.getSession().getAttribute("uid").toString();
		if (uid == null) {
			ResponseStatus status = new ResponseStatus(300, "你还未登陆,访问被拒绝!", "index");
			return status;
		}
		artService.deleteTaskItem(taskitemid);
		Taskitem taskitem = new Taskitem();
		taskitem.setId(taskitemid);
		taskitem.setStatus(TTStatus.SAVE);
		taskitem.setDel_status(1);//表示已删除
		taskitemService.updateDelete(taskitem);
		return new ResponseStatus(100, "任务项删除成功！");
	}
	
	@RequestMapping(value = "/facility/check/{taskitemid}", method = RequestMethod.GET)
	public @ResponseBody
	List<Qaresult> editCheck(HttpServletRequest request,@PathVariable("taskitemid") int taskitemid) {
		return artService.getQaresult(taskitemid);
	}
	
	@RequestMapping(value = "/facility/exportShape", method = RequestMethod.POST)
	public @ResponseBody
	ResponseStatus pgsql2shp(@RequestParam("param") String param, @RequestParam("type") String type) {
//		int taskid = Integer.parseInt(taskids);
		try {
			String exportUrl = facilityachieveService.pgsql2shp(param, type);
			log.debug("导出路径的URL：    "+exportUrl);
			return new ResponseStatus(100, exportUrl + ".tar", "导出成功！");
		} catch (Exception e) {
			return new ResponseStatus(200, e.getMessage(), "导出失败！");
		}
	}

	@RequestMapping(value = "/getPolygons/{bounds}", method = RequestMethod.GET)
	public @ResponseBody
	ResponseStatus search(@PathVariable("bounds") String bounds, HttpServletRequest request) {
		if (request.getSession().getAttribute("uid") == null) {
			return  new ResponseStatus(300, "index", "您还未登陆，请先登录！");
		}
		List<Facilityachieve> geoms = artService.getPolygonByBound(bounds);
		JSONArray array = JSONArray.fromObject(geoms);
		return new ResponseStatus(100, array.toString(), "查询成功！");
	}
	
	/**
	 * bug
	 * @param bounds
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getPolylines/{bounds}", method = RequestMethod.GET)
	public @ResponseBody
	ResponseStatus searchPolylines(@PathVariable("bounds") String bounds, HttpServletRequest request) {
		if (request.getSession().getAttribute("uid") == null) {
			return  new ResponseStatus(300, "index", "您还未登陆，请先登录！");
		}
		List<Qaresult> geoms = artService.getPolylineByBound(bounds);
		JSONArray array = JSONArray.fromObject(geoms);
		return new ResponseStatus(100, array.toString(), "查询成功！");
	}
	
}