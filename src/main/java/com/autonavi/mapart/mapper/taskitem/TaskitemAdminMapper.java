package com.autonavi.mapart.mapper.taskitem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.autonavi.mapart.entity.ResponseStatus;
import com.autonavi.mapart.entity.Taskitem;
import com.autonavi.mapart.orm.FacilityareaDao;
import com.autonavi.mapart.service.TaskService;
import com.autonavi.mapart.service.TaskitemService;
import com.autonavi.mapart.service.UserService;

/**
 * <p>
 * desc:管理员操作作业项控制器
 * <p>
 * Copyright: Copyright(c)AutoNavi 2014
 * </p>
 * 
 * @author <a href="mailTo:i-caiqiang@autonavi.com">i-caiqiang</a>
 * @time 2014-4-28 10:58
 * 
 */
@Controller
public class TaskitemAdminMapper {

	@SuppressWarnings("unused")
	private Logger log = Logger.getLogger(TaskitemAdminMapper.class);
	@Autowired
	private UserService userService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private TaskitemService taskitemService;
	@Autowired
	private FacilityareaDao facilityareaDao;

	@RequestMapping(value = "admin/taskitem/{id}", method = RequestMethod.GET)
	public @ResponseBody
	ResponseStatus detialTask(@RequestParam("page") Integer page, @PathVariable Integer id) {
		int pageIndex = 1;
		if ("".equals(id)) {
			return new ResponseStatus(200, "0", "用户参数输入错误！");
		} else {
			if ("".equals(page)) {
				pageIndex = 1;
			} else {
				try {
					pageIndex = Integer.valueOf(page);
				} catch (Exception e) {
					return new ResponseStatus(200, "0", "用户参数输入错误！");
				}
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("pageIndex", pageIndex);
			map.put("recordCountPerPage", 9);
			map.put("taskid", id);
			map.put("task_name", "");
			map = taskitemService.getTaskitemListByPage(map);
			@SuppressWarnings("unchecked")
			List<Taskitem> list = (List<Taskitem>) map.get("taskitems");
			JsonConfig jsonConfig = new JsonConfig();  
			jsonConfig.setExcludes(new String[] { "committime", "truename", "fileId"});
			JSONArray array = JSONArray.fromObject(list,jsonConfig);
			return new ResponseStatus(100, array.toString(), map.get("allRecordCount").toString());
		}
	}

	@RequestMapping(value = "admin/taskitem/{ids}", method = RequestMethod.DELETE)
	public @ResponseBody
	ResponseStatus delete(@PathVariable String ids) {
		Assert.notNull(ids);
		taskitemService.deleteTaskitemByIds(ids, 1);
		return new ResponseStatus(100, "0", "删除成功！");
	}
	
	@RequestMapping(value = "admin/taskitem/recycle/{taskId}", method = RequestMethod.PUT)
	public @ResponseBody ResponseStatus recover(@PathVariable String taskId) {
		Assert.notNull(taskId);
		taskitemService.recover(taskId,"");
		return new ResponseStatus(100, "0", "回收成功！");
	}
	
	@RequestMapping(value = "admin/taskitem/recycle/{taskId}/{ids}", method = RequestMethod.PUT)
	public @ResponseBody
	ResponseStatus recover(@PathVariable String taskId, @PathVariable String ids) {
		Assert.notNull(ids);
		taskitemService.recover(taskId,ids);
		return new ResponseStatus(100, "0", "回收成功！");
	}

}
