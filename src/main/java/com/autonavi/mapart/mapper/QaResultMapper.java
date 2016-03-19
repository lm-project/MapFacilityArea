package com.autonavi.mapart.mapper;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.autonavi.mapart.entity.Qaresult;
import com.autonavi.mapart.entity.ResponseStatus;
import com.autonavi.mapart.service.AnShpService;
import com.autonavi.mapart.service.ArtService;
import com.autonavi.mapart.service.FacilityachieveService;
import com.autonavi.mapart.service.QaResultService;
import com.autonavi.mapart.service.TaskService;
import com.autonavi.mapart.service.TaskitemService;

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
public class QaResultMapper {

	@Autowired
	private ArtService artService;
	
	@Autowired
	private QaResultService qaResultService;
	
	@Autowired
	private TaskService taskService;

	@Autowired
	private TaskitemService taskitemService;
	
	@Autowired
	private FacilityachieveService facilityachieveService;

	@Autowired
	private AnShpService anShpService;
	
	Logger log = Logger.getLogger(getClass());
	
	/**
	 * 
	 * 保存质检内容
	 * @param request
	 * @param facility
	 * @return
	 */
	@RequestMapping(value = "/qa/qa_result/save", method = RequestMethod.POST)
	public @ResponseBody ResponseStatus saveCheckInfo(HttpServletRequest request,  
			@RequestParam("qa_result") String qa_result) {
		log.debug("***************:"+qa_result);
		String uid = request.getSession().getAttribute("uid").toString();
		if (uid == null) {
			ResponseStatus status = new ResponseStatus(300, "你还未登陆,访问被拒绝!", "index");
			return status;
		}

		Qaresult qa = qaResultService.insertQaResult( new Qaresult(JSONObject.fromObject(qa_result)));
		log.debug("___________________"+qa.toString());
//		return qa;

		/**
		 * update task commit status
		 **/
//		Taskitem taskitem = new Taskitem();
////		taskitem.setId(fa[0].getTaskitem_id());
//		taskitem.setStatus(TTStatus.QA_PASSED);
//		taskitemService.updateTaskitem(taskitem);

		ResponseStatus status = new ResponseStatus(100, "设施保存成功！","");
		return status;
	}


}

