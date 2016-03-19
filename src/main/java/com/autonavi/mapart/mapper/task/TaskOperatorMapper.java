package com.autonavi.mapart.mapper.task;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.autonavi.mapart.entity.DaoResult;
import com.autonavi.mapart.entity.ResponseStatus;
import com.autonavi.mapart.mapper.MapperCommonUtil;
import com.autonavi.mapart.service.TaskService;
import com.autonavi.mapart.service.TaskitemService;

/**
 * <p>
 * desc:任务项操作控制器
 * <p>
 * Copyright: Copyright(c)AutoNavi 2014
 * </p>
 * 
 * @author <a href="mailTo:qiang.cai@autonavi.com">i-caiqiang</a>
 * @time 2014-4-28 10:58
 * 
 */
@Controller
@SessionAttributes("user")
public class TaskOperatorMapper {

	@SuppressWarnings("unused")
	private Logger log = Logger.getLogger(TaskOperatorMapper.class);

	@Autowired
	private TaskService taskService;

	@Autowired
	private TaskitemService taskitemService;
	
	/**
	 * 提交作业任务包 
	 * @param projectname
	 * @return
	 */
	@RequestMapping(value = "/op/task/commit", method = RequestMethod.POST)
		public @ResponseBody ResponseStatus commitTaskByTaskid(HttpServletRequest request, int taskid) {
		Integer role = MapperCommonUtil.getUserRole(request);
		if (role == -1||role==0||role==3) { // seesion中不存在用户
			log.info("<—————————IP:" + request.getLocalAddr() + " —————————>");
			return new ResponseStatus(300, "非法请求查询项目列表，请先登录!","");
		} 
		// 获取未保存的作业项数量
		int count = taskitemService.getUnsaveCountByTaskid(taskid);
		if (count != 0) {
			return new ResponseStatus(200, "项目中还有"+count+"项未完成的任务项,", "请作业完毕后提交！");
		}
		taskService.commitByTaskid(taskid,2);	//任务包提交
		return new ResponseStatus(100, "", "提交成功！");
	}
	
	
	/**
	 * 作业员领取作业项操作
	 * @param taskids
	 * @param taskitemids
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/operator/receiveTask", method = RequestMethod.POST)
	public @ResponseBody ResponseStatus receive( HttpServletRequest request, String taskids) {
		Integer usrid = MapperCommonUtil.getUserid(request);
		if (usrid == -1) { // seesion中不存在用户
			log.info("<—————————IP:" + request.getLocalAddr()+ " 非法请求获取质检员未领取项目!—————————>");
			return new ResponseStatus(300, "error", "还未登陆");
		} 
		log.debug("receive taskid ----:"+taskids);
		try {
			taskService.receiveTaskByOp(taskids, usrid);
			return new ResponseStatus(100, "领取成功 !");
		} catch (Exception e) {
			e.printStackTrace();
			log.debug("receive task exception: "+e.getMessage());
			return new ResponseStatus(200, "领取失败 !");
		}
	}
	@RequestMapping(value = "/getTask/", method = RequestMethod.POST)
	public @ResponseBody ResponseStatus getTask(int page, String projectname,
			int status, String s_time, String e_time, HttpServletRequest request) {
		log.debug("get task params info ----->status:"+status+",page:"+page+
				",projectname:"+projectname+",s_time:"+s_time+",e_time:"+e_time);
		try {
			Integer usrid = MapperCommonUtil.getUserid(request);
			if (usrid == -1) { // seesion中不存在用户
				log.info("<—————————IP:" + request.getLocalAddr()+ " 非法请求获取质检员未领取项目!—————————>");
				return new ResponseStatus(300, "error", "还未登陆");
			} 
			Map<String,Object> map = getParemMap(page,projectname,status,s_time,e_time,usrid);
			DaoResult rs = taskService.getTaskListByStatus(map);
			log.debug("^^^^^^^"+JSONArray.fromObject(rs.getList()).toString());
			if (rs.getTotalCount() == 0) {
				return new ResponseStatus(100, "true" , "未找到记录");
			}
			return new ResponseStatus(100, JSONArray.fromObject(rs.getList()).toString(), String.valueOf(rs.getTotalCount()));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseStatus(200, "获取任务项失败!");
		}
		
	}

	private Map<String ,Object> getParemMap(int page, String projectname, int status, String s_time, String e_time, Integer usrid){
		Map <String,Object > map = new HashMap<String,Object>();
		map.put("pageIndex", page);
		map.put("recordCountPerPage", 2);
		map.put("status", status);
		map.put("projectname", StringUtils.trim(projectname));
		map.put("uid", usrid);
		map.put("s_time", StringUtils.trim(s_time));
		map.put("e_time", StringUtils.trim(e_time));
		return map;
	}
	public static void main(String[] args) {
		System.out.println(StringUtil.isBlank(" "));
		System.out.println(StringUtils.isEmpty(""));
		System.out.println(" ".isEmpty());
	}
	
}
