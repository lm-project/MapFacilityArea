package com.autonavi.mapart.mapper;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.autonavi.mapart.entity.DaoResult;
import com.autonavi.mapart.entity.Facilityachieve;
import com.autonavi.mapart.entity.FapoiRelation;
import com.autonavi.mapart.entity.ResponseStatus;
import com.autonavi.mapart.entity.SetGetTaskListForm;
import com.autonavi.mapart.orm.FacilityareaDao;
import com.autonavi.mapart.service.AnShpService;
import com.autonavi.mapart.service.FacilityachieveService;
import com.autonavi.mapart.service.FapoiRelationService;
import com.autonavi.mapart.service.TaskService;
import com.autonavi.mapart.service.TaskitemService;
import com.autonavi.mapart.service.UserService;
import com.autonavi.mapart.util.ExecuteMultiPolygon;
import com.autonavi.mapart.util.UploadUtil;

/**
 * <p>
 * desc:公有权限请求操作控制器
 * <p>
 * Copyright: Copyright(c)AutoNavi 2014
 * </p>
 * 
 * @author <a href="mailTo:i-caiqiang@autonavi.com">i-caiqiang</a>
 * 
 * @time 2014-4-28 10:58 edittime 2014-8-8 11:03
 */
@Controller
public class CommonMapper {

	private Logger log = Logger.getLogger(CommonMapper.class);

	@Autowired
	private UserService userService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private TaskitemService taskitemService;

	@Autowired
	private FacilityareaDao facilityareaDao;

	@Autowired
	private FapoiRelationService fapoiRelationService;
	
	@Autowired
	private FacilityachieveService facilityService;
	
	@Autowired
	private AnShpService anShpService;

	/**
	 * 按身份查询项目列表->qa operatoe
	 * 
	 * @param taskitemid
	 *            作业项ID
	 * @return
	 */
	@RequestMapping(value = "common/getprojects", method = RequestMethod.GET)
	public @ResponseBody
	ResponseStatus getPorjects(HttpServletRequest request) {
		Integer usrid = MapperCommonUtil.getUserid(request);
		if (usrid == -1) { // seesion中不存在用户
			log.info("<—————————IP:" + request.getLocalAddr() + " —————————>");
			return new ResponseStatus(300, "非法请求查询项目列表，请先登录!");
		} else { // seesion中存在用户
			log.info("<—————————" + "用户:"
					+ MapperCommonUtil.getUserTruename(request)
					+ " 请求查询项目列表:开始—————————>");
			int role = MapperCommonUtil.getUserRole(request);
			int status;
			if (role == 2) {
				status = 0;
			} else {
				status = 1;
			}

			SetGetTaskListForm form = new SetGetTaskListForm(0, -1, 0,status,
					"", -1, 1);
			DaoResult result = taskService.getTaskList(form);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setExcludes(new String[] { "id", "name", "committime",
					"status", "userid", "callbacks", "createtime", "filename",
					"list", "remark", "tasktype" });
			JSONArray array = JSONArray
					.fromObject(result.getList(), jsonConfig);
			log.info("<—————————" + role + "用户:"
					+ MapperCommonUtil.getUserTruename(request)
					+ " 请求查询项目列表:结束———————————>");
			return new ResponseStatus(100, array.toString(), "查询成功！");
		}// end else
	}// end

	/**
	 * 查询已完成作业项的空间数据->qa operatoe
	 * 
	 * @param taskitemid
	 *            作业项ID
	 * @return
	 */
	@RequestMapping(value = "common/taskitem/{info}", method = RequestMethod.GET)
	public @ResponseBody ResponseStatus edit(@PathVariable String info, HttpServletRequest request) {
		Integer usrid = MapperCommonUtil.getUserid(request);
		if (usrid == -1) { // seesion中不存在用户
			log.info("<—————————IP:" + request.getLocalAddr() + " —————————>");
			return new ResponseStatus(300, "非法请求查询作业项!");
		}
		log.info("用户:" + MapperCommonUtil.getUserTruename(request) + " 请求查询作业项  —开始————>");
		log.info("请求参数信息："+info);
		JSONObject json = JSONObject.fromObject(info);
		Facilityachieve facility = facilityareaDao.getFacility(json.getInt("taskitemid"));
		log.debug("作页项存在吗："+facility);
		if (facility != null) {
			if(facility.getLng_lat().equals("")||facility.getLng_lat()==null){
				facility.setGeom(ExecuteMultiPolygon.getSecondGeometry(
						facility.getGeom(), false));
			}
		}else{
			facility = facilityareaDao.getFacilityByName(json.getString("searchKey"));
		}
		log.info("<—————————" + "用户:"+ MapperCommonUtil.getUserTruename(request)
														+ " 请求查询作业项—结束———————————>");
		JSONObject object = JSONObject.fromObject(facility);
		return new ResponseStatus(100, object.toString(), "查询成功！");
	}// end

	/**
	 * 根据polygon查询所属区域(大陆/港澳/共有),获取对应的area_flag
	 * 
	 * @param path
	 *            polygon顶点
	 * @return
	 */
	@RequestMapping(value = "common/area_flag", method = RequestMethod.GET)
	public @ResponseBody ResponseStatus getAreaFlag(@RequestParam("pointString") String pointString) {
		log.info("<—————————查询AREA_FLAG begin—————————>POLYGON Path:" + pointString);
		int area_flag = facilityareaDao.getAreaFlag(pointString);
		log.info("<—————————查询AREA_FLAG end—————————> Area_flag:" + area_flag);
		return new ResponseStatus(100, String.valueOf(area_flag), "查询成功！");
	}

	/**
	 * 查询设施区域类型与poi对应关系(FapoiRelation)
	 * 
	 * @param fa_category
	 * @param fa_type
	 * @param poi_type
	 * @return
	 */
	@RequestMapping(value = "common/fapoirelation", method = RequestMethod.POST)
	public @ResponseBody
	ResponseStatus getFapoiRelation(
			@RequestParam("fa_category") String fa_category,
			@RequestParam("fa_type") String fa_type,
			@RequestParam("poi_type") String poi_type) {
		log.info("<—————————查询Fa_type与Poi_type对应关系,查询参数：fa_category->" + fa_category + ", fa_type->"
				+ fa_type + ", poi_type->" + poi_type);
		List<FapoiRelation> list = fapoiRelationService.getFapoiRelationList(fa_category, fa_type, poi_type);
		JSONArray array = JSONArray.fromObject(list);
		return new ResponseStatus(100, array.toString(), String.valueOf(list.size()));
	}
	
	/**
	 * 更新fa_type对应关系
	 * @param fapoiRelation
	 * @return
	 */
	@RequestMapping(value = "common/updateFatype", method = RequestMethod.POST)
	public @ResponseBody
	ResponseStatus updateFatype(@RequestParam("fapoiRelation") String fapoiRelation){
		FapoiRelation relation = FapoiRelation.createFromJson(JSONObject.fromObject(fapoiRelation));
		log.info("<—————————更新fa_type,poi_type对应关系, poi_type:"+ relation.getPoi_type());
		fapoiRelationService.update(relation);
		
		/**
		 * update facility
		 */
		log.info("<—————————更新FacilityArea中fa_type字段");
		facilityareaDao.updateFatype(relation);
		return new ResponseStatus(100, "更新成功！");
		
	}
	
	/**
	 * 获取设施区域大类列表
	 * @return
	 */
	@RequestMapping(value = "common/getFacategory", method = RequestMethod.POST)
	public @ResponseBody
	ResponseStatus getFacategory(){
		List<String> list = fapoiRelationService.getFacategory();
		JSONArray array = JSONArray.fromObject(list);
		return new ResponseStatus(100, array.toString(), "查询成功！");
		
	}
	
	/**
	 * 获取设施区域中类列表
	 * @return
	 */
	@RequestMapping(value = "common/getFatypeList", method = RequestMethod.POST)
	public @ResponseBody
	ResponseStatus getFatypeList(@RequestParam("fa_category") String fa_category){
		log.info("<—————————查询设施区域中类,fa_category:"+fa_category);
		List<String> list = fapoiRelationService.getFatypeList(fa_category);
		JSONArray array = JSONArray.fromObject(list);
		return new ResponseStatus(100, array.toString(), "查询成功！");
	}
	
	
	/**
	 * 获取poi类型列表
	 * @return
	 */
	@RequestMapping(value = "common/getPoitypeList", method = RequestMethod.POST)
	public @ResponseBody
	ResponseStatus getPoitypeList(@RequestParam("fa_type") String fa_type){
		log.info("<—————————查询poi类型,fa_type:"+fa_type);
		List<String> list = fapoiRelationService.getPoitypeList(fa_type);
		JSONArray array = JSONArray.fromObject(list);
		return new ResponseStatus(100, array.toString(), "查询成功！");
	}
	
	
	/**
	 * 新增Fa_type与poi类型对应关系
	 * @return
	 */
	@RequestMapping(value = "common/newFapoiRelation", method = RequestMethod.POST)
	public @ResponseBody
	ResponseStatus newFapoiRelation(@RequestParam("fapoiRelation") String fapoiRelation){
		FapoiRelation relation = FapoiRelation.createFromJson(JSONObject.fromObject(fapoiRelation));
		log.info("<—————————新增fa_type与poi_type对应关系—————————>");
		fapoiRelationService.newRelation(relation);
		return new ResponseStatus(100, "查询成功！");
	}
	/**
	 * 
	 * 导入shp文件到到数据库
	 * @param request
	 * @param shpfiles 文件路径
	 * @return
	 */
	@RequestMapping(value = "admin/importShp", method = RequestMethod.POST)
	@Transactional
	public @ResponseBody ResponseStatus importShp(HttpServletRequest request, 
					@RequestParam MultipartFile[] shpfiles) {
		int role = MapperCommonUtil.getUserRole(request);
		log.info("<------用户："+ MapperCommonUtil.getUserTruename(request) + "上传Shape文件----->");
		if(role != 0){
			return new ResponseStatus(200, "", "权限不够，请让管理员导入!");
		}
		String inputfile = "uplaodfile";
		final String dir = "/home/MapFacilityArea/importFile/"; // 文件存放目录
		String filepath = null;
		log.info("<-------------------开始处理上传shape文件------------------->");
		log.info("<-------------------处理文件数:" + shpfiles.length + "------------------->");
		try {
			for (MultipartFile shpfile : shpfiles) {
				log.info("<------开始处理文件:"+ shpfile.getOriginalFilename()+ "------->");
				if (!shpfile.isEmpty()) {
					filepath = UploadUtil.fileUpload(shpfile, dir, inputfile);
					log.info("<-------------------上传文件至服务器存储目录:" + filepath +"------------------->");
					log.info("<--------数据版本:" + request.getParameter("dataVersion") +"-------->");
					DaoResult result = anShpService.importShp(filepath,
							String.valueOf(request.getParameter("dataVersion")));
					if (result.getStatusCode() == 100) {
						return new ResponseStatus(100, result.getTotalCount()+"", "上传成功!");
					}else {
						return new ResponseStatus(200, result.getRemark());
					}
					
				} else{
					return new ResponseStatus(200, "无上传文件！");
				}// end if
			}//end 处理上传文件
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			UploadUtil.delFile(filepath);
			log.info("<-------------------上传文件处理失败，服务器自动删除:" + filepath +"文件--------------------------->");
			return new ResponseStatus(200, "", "上传文件内容不符合!");
		}
		return null;
	}

}
