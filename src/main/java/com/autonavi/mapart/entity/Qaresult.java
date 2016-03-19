package com.autonavi.mapart.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.autonavi.mapart.util.GetGeometry;

/**
 * <p>
 * desc: 质检结果实体类
 * <p>
 * 
 * @author huandi.yang
 * 
 */
/**
 * @author zhentao.liu
 *
 */
public class Qaresult {

	private static final String LINESTRING = "LINESTRING";
	private int id; 		 	 // 质检结果id
	private int taskitem_id; 	 // 任务项id，关联task_item表id
	
	private String qamark; // 质检标记
	private String remark; // 质检备注
	private String reverse_qamark; //坐标反变形后的质检标记
	
	private String qc_code;		 // 检查项代码 
	private String qa_mark; 	 // 质检标记
	private String des;			 // 质检备注
	private String feedback; 	 // 作业反馈标记 a:数据错误，b:数据正确
	private String exp;			 // 作业反馈描述
	private String tuceng;       // 1:facilityArea 
	private static Logger log = Logger.getLogger("Qaresult");
	
	public Qaresult () {
		
	}
	public Qaresult (JSONObject json) {
		this.des = StringUtils.trim(json.getString("des"));
		this.taskitem_id = json.getInt("task_item_id");
		this.qc_code = json.getString("qc_code");
		this.qamark = json.getString("qamark");
		this.tuceng = json.getString("tuceng");
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTaskitem_id() {
		return taskitem_id;
	}

	public void setTaskitem_id(int taskitem_id) {
		this.taskitem_id = taskitem_id;
	}

	public String getQc_code() {
		return qc_code;
	}

	public void setQc_code(String qc_code) {
		this.qc_code = qc_code;
	}

	public String getQa_mark() {
		return qa_mark;
	}

	public void setQa_mark(String qa_mark) {
		this.qa_mark = qa_mark;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public String getExp() {
		return exp;
	}

	public void setExp(String exp) {
		this.exp = exp;
	}

	public String getTuceng() {
		return tuceng;
	}

	public void setTuceng(String tuceng) {
		this.tuceng = tuceng;
	}

	
	public String getQamark() {
		return qamark;
	}

	public void setQamark(String qamark) {
		this.qamark = qamark;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getReverse_qamark() {
		return reverse_qamark;
	}

	public void setReverse_qamark(String reverse_qamark) {
		this.reverse_qamark = reverse_qamark;
	}
	
	/**
	 * {"remark":"\t\t\t\t\t\t\tb\t\t","task_item_id":"747","path":[
	 * "116.463575,39.929517,116.454735,39.927181","116.455679,39.929254"]}
	 * 
	 * @return
	 */
	public static Qaresult[] createFromJson(JSONObject json) {
		List<Qaresult> list = new ArrayList<Qaresult>();

		JSONArray array = json.getJSONArray("path");

		int size = array.size();
		if( size == 0 ) {
			Qaresult rs = new Qaresult();
			rs.setRemark(StringUtils.trim(json.getString("remark")));
			rs.setTaskitem_id(json.getInt("task_item_id"));
			list.add(rs);
		} else {
				Pattern compile = Pattern.compile("\"([0-9,.]*)\"");
				Matcher m = compile.matcher(json.getString("path"));
				boolean isReverse = true;
				boolean isSecond = false;
				while( m.find()) {
					String geomOfJson = m.group(0).replace("\"", "").replace(",,", ",");
					log.debug(" 处理过的path字符串为：  "+geomOfJson);
					Qaresult rs = new Qaresult();
					rs.setRemark(StringUtils.trim(json.getString("remark")));
					rs.setTaskitem_id(json.getInt("task_item_id"));
					//变形前
					rs.setQamark(GetGeometry.getGeometry(geomOfJson, ! isReverse, LINESTRING, isSecond));
					//变形后
					rs.setReverse_qamark(GetGeometry.getGeometry(geomOfJson, isReverse, LINESTRING, isSecond));
					
					list.add(rs);
				}
		}

		return list.toArray(new Qaresult[list.size()]);
	}
	
	@Override
	public String toString() {
		return "Qaresult [id=" + id + ", taskitem_id=" + taskitem_id + ", qc_code=" + qc_code 
				+ ", des=" + des+ ", tuceng=" + tuceng
				+ ", qa_mark=" + qa_mark + "]";
	}


}
