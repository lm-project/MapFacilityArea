package com.autonavi.mapart.mapper.taskitem;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.autonavi.mapart.entity.DaoResult;
import com.autonavi.mapart.entity.Qaresult;
import com.autonavi.mapart.entity.ResponseStatus;
import com.autonavi.mapart.entity.SetGetTaskListForm;
import com.autonavi.mapart.entity.Task;
import com.autonavi.mapart.entity.Taskitem;
import com.autonavi.mapart.mapper.MapperCommonUtil;
import com.autonavi.mapart.orm.FacilityareaDao;
import com.autonavi.mapart.service.TaskService;
import com.autonavi.mapart.service.TaskitemService;
import com.autonavi.mapart.service.UserService;
import com.autonavi.mapart.util.DateFormat;

/**
 * <p>
 * desc:质检员操作作业项控制器
 * <p>
 * Copyright: Copyright(c)AutoNavi 2014
 * </p>
 * 
 * @author <a href="mailTo:i-caiqiang@autonavi.com">i-caiqiang</a>
 * 
 * @time 2014-4-28 10:58 edittime 2014-8-8 11:03
 */
@Controller
public class TaskitemQaMapper {

	private Logger log = Logger.getLogger(TaskitemQaMapper.class);

	@Autowired
	private UserService userService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private TaskitemService taskitemService;

	@Autowired
	private FacilityareaDao facilityareaDao;

	/**
	 * 获取质检员已领取作业项
	 * 
	 * @param taskids
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/qa/taskitems/received/{taskids}", method = RequestMethod.GET)
	public @ResponseBody
	List<Taskitem> getQaTaskitems(@PathVariable String taskids, HttpServletRequest request) {
		Integer usrid = MapperCommonUtil.getUserid(request);
		log.info("<—————————用户:" + MapperCommonUtil.getUserTruename(request)
				+ " 请求获取质检员已领取作业项—开始—————————>");
		if (usrid == -1) { // seesion中不存在用户
			log.info("<—————————IP:" + request.getLocalAddr()
					+ " 非法请求获取质检员已领取作业项!—————————>");
			return null;
		} else { // seesion中存在用户
			return taskitemService.getTaskitemsByQaTaskids(taskids,
					usrid.toString());
		}
	}
	
	/***
	 * 
	 * 查询任务包根据项目和status
	 * @param page
	 * @param projectname
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/qa/searchTask", method = RequestMethod.POST)
	public @ResponseBody ResponseStatus searchTask(Integer page, String projectname,
			int status, HttpServletRequest request) {
		log.debug("page :"+page+",projectname ："+projectname);
		Integer usrid = MapperCommonUtil.getUserid(request);
		if (usrid == -1) { // seesion中不存在用户
			log.info("<—————————IP:" + request.getLocalAddr()+ " 非法请求获取质检员未领取项目!—————————>");
			return new ResponseStatus(300, "error", "还未登陆");
		} 
		List<Taskitem> result = taskitemService.getTaskPackageList(page,projectname,status,usrid);
		log.debug("获取的数数量： "+result.size());
		log.debug("获取的List ： "+result.toString());
		JSONArray array = JSONArray.fromObject(result);
		log.debug("质检点击查询任务时返回给客户端的信息："+array.toString());
		return new ResponseStatus(100, array.toString(), String.valueOf(result.size()));
	}


	/**
	 * 分页查询任务
	 * 
	 * @param assid
	 * @param tname
	 * @param origin
	 * @param stime
	 * @param etime
	 * @param city
	 * @return
	 */
	@RequestMapping(value = "/qa/searchProject", method = RequestMethod.POST)
	public @ResponseBody ResponseStatus searchProjectByPage(@RequestParam("page") Integer page,
			@RequestParam("projectname") String projectname, HttpServletRequest request) {
		
		Integer usrid = MapperCommonUtil.getUserid(request);
		if (usrid == -1) { // seesion中不存在用户
			log.info("<—————————IP:" + request.getLocalAddr()+ " 非法请求获取质检员未领取项目!—————————>");
			return new ResponseStatus(300, "error", "还未登陆");
		} 
		SetGetTaskListForm form = new SetGetTaskListForm(page, 9,0, 1, projectname, -1, 1);
		DaoResult result = taskService.getTaskList(form);
//		DaoResult result = taskService.getTaskListByStatus(form);
		log.debug("获取的数数量： "+result.getTotalCount());
		log.debug("获取的List ： "+result.getList().toString());
		List<Object> list = result.getList();
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(new String[] { "committime", "truename" });
		JSONArray array = JSONArray.fromObject(list, jsonConfig);
		log.debug("质检点击查询任务时返回给客户端的信息："+array.toString());
		return new ResponseStatus(100, array.toString(),
				String.valueOf(result.getTotalCount()));
	}

	/**
	 * 质检员请求获取已提交项目下 按城市作业项数量
	 * 
	 * @param projectname 项目名称
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/qa/getcity", method = RequestMethod.POST)
	public @ResponseBody
	ResponseStatus findCitysByProjectname(
			@RequestParam("projectname") String projectname,
			HttpServletRequest request) {

		try {
			DaoResult rs = taskitemService.findCitysByProjectname(projectname);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setExcludes(new String[] { "committime", "fadeback",
					"id", "mesh", "name", "number", "pcsid", "processtype",
					"qataskid", "qataskname", "qauserid", "receivetime",
					"status", "task", "taskid", "taskname", "userid",
					"typecode", "x_coord", "y_coord" });
			return new ResponseStatus(100, JSONArray.fromObject(rs.getList(),
					jsonConfig).toString(), String.valueOf(rs.getTotalCount()));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseStatus(200, "获取未领取任务项失败!");
		}

	}

	/**
	 * 
	 * @param projects
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/qa/recevie", method = RequestMethod.POST)
	public @ResponseBody
	ResponseStatus qarecevie(@RequestParam("projectname") String projectname,
			@RequestParam("city") String city,
			@RequestParam("number") int number, HttpServletRequest request) {
		int uid = 0;
		if (request.getSession().getAttribute("uid") == null) {
			return new ResponseStatus(300, "error", "还未登陆!");
		} else if ("".equals(city)) {
			return new ResponseStatus(200, "error", "参数为填充！");
		}
		uid = Integer.valueOf(String.valueOf(request.getSession().getAttribute(
				"uid")));
		String truename = String.valueOf(request.getSession().getAttribute(
				"truename"));
		String taskname = projectname + "—" + city + "—" + number + "—" + truename + DateFormat.get13Now();
		String remark = "";
		String filename = "";
		int tasktype = 1;
		
		int totle = taskitemService.getQaUnreceiveUnmber(projectname, city, null);
		if (number > totle) {
			return new ResponseStatus(200, "false", totle+"");
		}
		Task task = new Task(taskname, remark, filename, uid, projectname,
				tasktype);
		// 创建质检任务
		int taskid = taskService.insertTask(task);
		// 更新作业项的被质检领取后的记录
		taskitemService.QAreceive(taskid, taskname, uid, projectname, city,
				number);

		return new ResponseStatus(100, "领取成功！");
	}

	/**
	 * 
	 * @param page
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/qa/getreceived", method = RequestMethod.GET)
	public @ResponseBody ResponseStatus queryReceive(HttpServletRequest request,
			@RequestParam("page") Integer page ) {
		int uid = MapperCommonUtil.getUserid(request);
		if (uid == -1) {
			return new ResponseStatus(300, "error", "还未登陆");
		}
		log.debug("sffs&&&&&&&&&&&&&&&&&&      "+page);
		SetGetTaskListForm form = new SetGetTaskListForm(page, 9,1,1, "", uid,  2);
		DaoResult result = taskService.getTaskList(form);
		List<Object> list = result.getList();
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(new String[] { "committime", "truename" });
		JSONArray array = JSONArray.fromObject(list, jsonConfig);
		log.debug("sffs&&&&&&&&&&&&&&&&&&      "+array.toString());
		return new ResponseStatus(100, array.toString(), String.valueOf(result
				.getTotalCount()));
	}
	
	
	
	/**
	 * 
	 * @param page
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/qa_result/getQaInfo/{taskitemid}", method = RequestMethod.GET)
	public @ResponseBody List<Qaresult> getQaInfo(HttpServletRequest request, @PathVariable int taskitemid ) {
		log.debug("taskitemid is : "+taskitemid);
		int uid = MapperCommonUtil.getUserid(request);
		if (uid == -1) {
			return null;
		}
		List<Qaresult>  list = taskitemService.getQaInfoByTaskitemId(taskitemid);
		for(int i = 0;i<list.size();i++){
			Qaresult qa = list.get(i);
			log.debug("----------"+qa.toString());
		}
		log.debug("query qaresult info size : "+list.size());
		return list;
	}
}
