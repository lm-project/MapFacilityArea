package com.autonavi.mapart.mapper.task;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.autonavi.mapart.entity.DaoResult;
import com.autonavi.mapart.entity.Facilityachieve;
import com.autonavi.mapart.entity.ResponseStatus;
import com.autonavi.mapart.entity.SetGetTaskListForm;
import com.autonavi.mapart.entity.Task;
import com.autonavi.mapart.entity.Taskitem;
import com.autonavi.mapart.entity.User;
import com.autonavi.mapart.mapper.MapperCommonUtil;
import com.autonavi.mapart.service.FacilityachieveService;
import com.autonavi.mapart.service.TaskService;
import com.autonavi.mapart.service.TaskitemService;
import com.autonavi.mapart.service.UserService;
import com.autonavi.mapart.util.DateFormat;
import com.autonavi.mapart.util.GetGeometry;
import com.autonavi.mapart.util.UploadUtil;

/**
 * <p>
 * desc:管理员操作任务项+项目控制器
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
public class TaskAdminMapper {

	private Logger log = Logger.getLogger(TaskAdminMapper.class);

	@Autowired
	private UserService userService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private TaskitemService taskitemService;

	@Autowired
	private FacilityachieveService facilityService;
	
	/**
	 * 获取所有项目(excel)
	 * 
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "admin/project/", method = RequestMethod.GET)
	public @ResponseBody ResponseStatus getProject(@RequestParam Integer page) {
		if ("".equals(page)) {
			page = 1;
		}
		SetGetTaskListForm form = new SetGetTaskListForm(page, 9, 0,-1, "",
				-1, 1);
		DaoResult result = taskService.getTaskList(form);
		List<Object> list = result.getList();
		List<Task> list2 = new ArrayList<Task>();
		Task task = null;
		for (Object object : list) {
			task = (Task) object;
			list2.add(excuteTask(task));
		}
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(new String[] { "callbacks", "committime",
				"list", "userid" });
		JSONArray array = JSONArray.fromObject(list2, jsonConfig);
		return new ResponseStatus(100, array.toString(), String.valueOf(result
				.getTotalCount()));
	}

	/**
	 * 删除选中项目
	 * 
	 * @param projects
	 * @return
	 */
	@RequestMapping(value = "admin/delproject/", method = RequestMethod.POST)
	public @ResponseBody ResponseStatus delProject(@RequestParam String projects) {
		taskService.deleteTaskByProjectname(projects);
		return new ResponseStatus(100, "删除成功！");
	}

	/**
	 * 获取项目信息
	 * 
	 * @param model
	 * @param projectname
	 *            项目名称
	 * @return
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value = "admin/project/edit", method = RequestMethod.POST)
	public @ResponseBody ResponseStatus editProject(Model model,
			@RequestParam("projectname") String projectname) {
		Assert.notNull(projectname);
		model.addAttribute("project", taskService.getProjectByName(projectname));
		model.addAttribute("page", 1);
		Task task = taskService.getProjectByName(projectname);
		log.debug("task================"+task);
		User user = new User();
		user.setUid(task.getUserid());
		user = userService.getUser(user);
		task.setUserinformation(user.getTruename() + "<" + user.getEmail()
				+ ">");
		if (task == null) {
			return new ResponseStatus(200, "eoor", "找不到该项目!");
		} else {
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setExcludes(new String[] { "id", "list", "name",
					"tasktype" });
			JSONObject object = JSONObject.fromObject(excuteTask(task),
					jsonConfig);
			return new ResponseStatus(100, object.toString(), "");
		}
	}

	private Task excuteTask(Task task) {
		String filePath = task.getFilename();
		if (filePath.indexOf("/") > -1 && (filePath.indexOf("_") > -1)) {
			String file = filePath.substring(filePath.lastIndexOf("/") + 1,
					filePath.length());
			System.out.println(file);
			String time = file.split("_")[0];
			String filename = file.split("_")[1];
			task.setCreatetime(DateFormat.StringToDate(time));
			task.setFilename(filename);
		}
		return task;
	}

	/**
	 * 项目提交-任务提交
	 * 
	 * @param projectname
	 * @return
	 */
	@RequestMapping(value = "admin/project/commit", method = RequestMethod.POST)
	public @ResponseBody ResponseStatus commit(
			@RequestParam("projectname") String projectname) {
		// 获取未保存的作业项数量
		int count = taskService.getUnsaveCount(projectname);
		if (count != 0) {
			return new ResponseStatus(200, "项目中有未完成的作业项！", "提交失败！");
		}
		// 任务提交
		int tasktype = 0; // 任务类型：作业
		int status = 1; // 任务状态：提交(1)
		taskService.commit(projectname, "", tasktype, status);
		// 作业项提交
		int taskitemStatus = 3; // 作业项状态：提交(3)
		taskitemService.commit(projectname, "", tasktype, taskitemStatus);
		
		// 提交成功时对facility_achieve 进行一次反变形
		cover(projectname);
		return new ResponseStatus(100, "", "提交成功！");
	}

	private void cover(String projectname) {
		List<Task> task_list = taskService.getTasksByProjectName(projectname);
		StringBuffer task_ids_String = new StringBuffer("");
		
		for (int i = 0; i < task_list.size(); i++) {
			task_ids_String.append(task_list.get(i).getId()+",");
		}
		
		String task_ids = task_ids_String.substring(0, task_ids_String.length()-1); // "714,715,716,717,718,719,720,721,722,723";
		List<Taskitem> task_item_list = taskitemService.getTaskitemByIds(task_ids);
		StringBuffer task_item_ids = new StringBuffer("");
		
		for (int i = 0; i < task_item_list.size(); i++) {
			task_item_ids.append(task_item_list.get(i).getId()+",");
		}
		
		String ss = task_item_ids.substring(0, task_item_ids.length()-1);
		List<Facilityachieve> list = facilityService.getFacilityareaList(ss);
		
		for (int i = 0; i < list.size(); i++) {
			Facilityachieve fa = list.get(i);
			fa.setReverse_geom(GetGeometry.getGeometry(fa.getGeom(), true, "POLYGON", true));
			facilityService.updateFacilityachieve(fa);
		}
		
	}

	@RequestMapping(value = "admin/alltask", method = RequestMethod.POST)
	public @ResponseBody ResponseStatus getAssignment(
			@RequestParam("page") Integer page,
			@RequestParam("projectname") String projectname) {
		if ("".equals(page)) {
			page = 1;
		}

		SetGetTaskListForm form = new SetGetTaskListForm(page, 9,0, -1,
				projectname, -1, 2);
		DaoResult result = taskService.getTaskList(form);

		List<Object> list = result.getList();
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(new String[] { "callbacks", "committime",
				"list", "userid" });
		JSONArray array = JSONArray.fromObject(list, jsonConfig);
		return new ResponseStatus(100, array.toString(), String.valueOf(result
				.getTotalCount()));
	}

	@RequestMapping(value = "admin/newtask", method = RequestMethod.POST)
	@Transactional
	public @ResponseBody ResponseStatus create(HttpServletRequest request,
			@RequestParam MultipartFile[] myfiles,@RequestParam String remark) {
		String inputfile = "uplaodfile";
		final String dir = "/home/MapFacilityArea/uploadFile/"; // 文件存放目录
		String filepath = null;
		log.info("<------用户："+ MapperCommonUtil.getUserTruename(request) + "上传任务清单----->");
		log.info("<-------------------开始处理上传任务清单--------------------------->");
		@SuppressWarnings("unused")
		List<Task> returntasklists = new ArrayList<Task>(); // 用作上传完成返回参数
		try {
			log.info("<-----处理文件数:" + myfiles.length + "------>");
			for (MultipartFile myfile : myfiles) {
				String fileName =  myfile.getOriginalFilename();
				log.info("<------------开始处理文件:" + fileName + "------>");
				if (myfile.isEmpty()) {
					return new ResponseStatus(200, "无上传文件！");
				}
				Task  task = taskService.getProjectByName(fileName.substring(0, fileName.lastIndexOf(".")));
				log.debug("project exist ："+task);
				if(task!=null){
					return new ResponseStatus(200, "上传的项目已经存在！");
				}
				filepath = UploadUtil.fileUpload(myfile, dir, inputfile);
				log.info("<-------------------上传文件至服务器存储目录:" + filepath +"--------------------------->");
				DaoResult result = taskService.insertTasks(filepath,remark, MapperCommonUtil.getUserid(request));
				if (result.getStatusCode() == 100) {
					JSONArray json = new JSONArray();
					json.addAll(result.getList());
					return new ResponseStatus(100, json.toString(), "上传成功!");
				} else {
					return new ResponseStatus(200, result.getRemark());
				}
			}// end 处理上传文件
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			UploadUtil.delFile(filepath);
			log.info("<-------------------上传文件处理失败，服务器自动删除:" + filepath
					+ "文件--------------------------->");
			return new ResponseStatus(200, "", "上传文件内容不符合!");
		}
		return null;
	}

	@RequestMapping(value = "admin/task/{id}", method = RequestMethod.GET)
	public @ResponseBody ResponseStatus edit(Model model,
			@PathVariable String id) {

		try {
			int taskid = Integer.valueOf(id);
			model.addAttribute("task", taskService.getTaskById(taskid));
			model.addAttribute("page", 1);
			JSONArray array = JSONArray.fromObject(model);
			return new ResponseStatus(100, array.toString(), "success");
		} catch (Exception e) {
			return new ResponseStatus(100, "输入参数错误！", "error");
		}

	}

	@RequestMapping(value = "admin/task/{ids}", method = RequestMethod.DELETE)
	public @ResponseBody ResponseStatus delete(@PathVariable String ids) {
		Assert.notNull(ids);

		taskService.deleteTaskByIds(ids);
		return new ResponseStatus(100, "0", "删除成功！");
	}
	/**
	 * 
	 * @param projectname
	 * @return
	 */
	@RequestMapping(value = "task/commit", method = RequestMethod.POST)
		public @ResponseBody ResponseStatus commitTaskByTaskid(HttpServletRequest request,
			@RequestParam("taskid") int taskid) {
		Integer role = MapperCommonUtil.getUserRole(request);
		if (role == -1||role==0||role==3) { // seesion中不存在用户
			log.info("<—————————IP:" + request.getLocalAddr() + " —————————>");
			return new ResponseStatus(300, "非法请求查询项目列表，请先登录!","");
		} 
		// 获取未保存的作业项数量
		int count = taskService.getUnsaveCountByTaskid(taskid);
		if (count != 0) {
			return new ResponseStatus(200, "项目中还有"+count+"项未完成的任务项,", "请作业完毕后提交！");
		}
		int task_status = 1;
		if(role==2){
			taskitemService.commitOpTaskitem(taskid,3);
		}else if(role==1){//查询任务项是否质检ok
			//根据taskid去查询qa_result 的status==3？
			//yes   
//        		task_status = 0 ，
			
			//no
//			  task_status = 3 ，
		
		}
		taskService.commitByTaskid(taskid,task_status);//任务包提交
//		int taskitemStatus = role==1 ? 1:role==1 ? 0; // 作业项状态：提交(3)
//		taskitemService.commit(projectname, "", tasktype, taskitemStatus);// 作业项提交
		
		// 提交成功时对facility_achieve 进行一次反变形
//		cover(projectname);
		return new ResponseStatus(100, "", "提交成功！");
	}
}
