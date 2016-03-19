package com.autonavi.mapart.mapper.task;


import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

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
public class TaskQaMapper {

	@SuppressWarnings("unused")
	private Logger log = Logger.getLogger(TaskQaMapper.class);

	@Autowired
	private TaskService taskService;

	@Autowired
	private TaskitemService taskitemService;
	
	/**
	 * 质检员领取作业项操作
	 * @param taskids
	 * @param taskitemids
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/qa/receiveTask", method = RequestMethod.POST)
	public @ResponseBody ResponseStatus receive( HttpServletRequest request, String taskids) {
		Integer usrid = MapperCommonUtil.getUserid(request);
		if (usrid == -1) { // seesion中不存在用户
			log.info("<—————————IP:" + request.getLocalAddr()+ " 非法请求获取质检员未领取项目!—————————>");
			return new ResponseStatus(300, "error", "还未登陆");
		} 
		log.debug("receive taskid ----:"+taskids);
		try {
			taskService.receiveTaskByQa(taskids, usrid);
			return new ResponseStatus(100, "领取成功 !");
		} catch (Exception e) {
			e.printStackTrace();
			log.debug("receive task exception: "+e.getMessage());
			return new ResponseStatus(200, "领取失败 !");
		}
	}
	
}
