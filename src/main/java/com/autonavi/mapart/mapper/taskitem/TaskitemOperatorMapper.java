package com.autonavi.mapart.mapper.taskitem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.autonavi.mapart.entity.DaoResult;
import com.autonavi.mapart.entity.ResponseStatus;
import com.autonavi.mapart.entity.Taskitem;
import com.autonavi.mapart.mapper.MapperCommonUtil;
import com.autonavi.mapart.orm.FacilityareaDao;
import com.autonavi.mapart.service.TaskService;
import com.autonavi.mapart.service.TaskitemService;
import com.autonavi.mapart.service.UserService;

/**
 * <p>
 * desc:作业项操作控制器
 * <p>
 * Copyright: Copyright(c)AutoNavi 2014
 * </p>
 * 
 * @author <a href="mailTo:i-caiqiang@autonavi.com">i-caiqiang</a>
 * @time 2014-4-28 10:58
 * 
 */
@Controller
public class TaskitemOperatorMapper {

	@SuppressWarnings("unused")
	private Logger log = Logger.getLogger(TaskitemOperatorMapper.class);
	@Autowired
	private UserService userService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private TaskitemService taskitemService;
	@Autowired
	private FacilityareaDao facilityareaDao;
	
	/**
	 * 作业员领取作业项操作
	 * @param taskids
	 * @param taskitemids
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/operator/receive", method = RequestMethod.POST)
	public @ResponseBody ResponseStatus receive( HttpServletRequest request, String taskids) {
		log.debug("receive taskid ----:"+taskids);
		try {
			taskitemService.receive(taskids, getUsrId(request));
			return new ResponseStatus(100, "领取成功 !");
		} catch (Exception e) {
			e.printStackTrace();
			log.debug("receive task exception: "+e.getMessage());
			return new ResponseStatus(200, "领取失败 !");
		}
	}

	private Integer getUsrId(HttpServletRequest request) {
		return Integer.valueOf( String.valueOf(request.getSession().getAttribute("uid")));
	}
	
	/**
	 * 根据选择的任务id，获取任务详细信息
	 * 
	 * @param taids
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/operator/taskitems/received/{taskids}", method = RequestMethod.GET)
	public @ResponseBody List<Taskitem> getSelectedTasks(@PathVariable String taskids, HttpServletRequest request) {
		
		log.debug("____________"+taskids);
		Integer usrid = MapperCommonUtil.getUserid(request);
		return taskitemService.getTaskByTaskids(taskids, usrid.toString());
		
	}
	
	
	@RequestMapping(value = "/operator/getrecevie/", method = RequestMethod.POST)
	public @ResponseBody ResponseStatus getUnReceivedItems(int page, String projectname, 
			String taskname, String processtype, int status, HttpServletRequest request) {
		try {
			DaoResult rs = null;
			if (status == 0) {
				rs = taskitemService.getReceive(page, -1, getTaskname(projectname, taskname) ,
						processtype==null ?"" : processtype.trim(), 0, 0);
			} else {
				rs = taskitemService.getReceive(page, -1, "" , "" , 1,  MapperCommonUtil.getUserid(request));
			}
			if (rs.getTotalCount() == 0) {
				return new ResponseStatus(100, "true" , "未找到记录");
			}
			return new ResponseStatus(100, JSONArray.fromObject(rs.getList()).toString(), String.valueOf(rs.getTotalCount()));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseStatus(200, "获取任务项失败!");
		}
		
	}
	

	@RequestMapping(value = "/workrecord", method = RequestMethod.POST)
	public @ResponseBody List<Taskitem> workrecord() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("iscommit", 1);

		return taskitemService.getTaskitemListByQuqrey(map);
	}
	
	
	private static String getTaskname(String projectname, String taskname) {
		String task_name = "";
		if (projectname != null && !"".equals(projectname.trim())) {
			task_name = projectname.trim();
		}
		if (taskname != null && !"".equals(taskname.trim())) {
				task_name += "——" +taskname.trim();
		}
		return task_name;
	}
}
