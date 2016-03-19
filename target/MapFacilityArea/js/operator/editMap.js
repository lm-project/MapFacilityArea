var poiOrg = {};// 原始poi数据
define(['../msg/msg', '../poi/poiform'],function(Msg,PoiForm) {
	console.log("加载operator/editMap，返回并执行init，增加#edit_item点击事件，进行编辑");
	
	var Map, Taskitem ,Polygon,polygons;
	function init(map,taskitem ,polygon,polygonList) {
		Map = map, Taskitem = taskitem, Polygon = polygon, polygons = polygonList;
		$('#main_left [id=edit_item]').live('click',function(){// unsave
			var taskItem_id = $(this).attr('value');
			console.log("响应点击开始（结束）编辑事件--"+$(this).text());
			$(this).text() === "结束编辑" ? endEditPolygon(taskItem_id, this):
										beginEditPolygon(taskItem_id, this);
			$('.top1').html("参考信息");
		});
		console.log("初始化editMap,给开始（结束）编辑添加点击事件");
	}
	
	
	/**
	 * 判断又没有polygon，有：保存数据，无：删除数据，
	 * 如果已入cms库，不删除任务项，新增一条记录condition状态为2（删除）
	 * 如果没入cms库，删除任务项
	 */
	function endEditPolygon(id, btn){
		if ( polygonEdit && polygonEdit.getPath().toString()!='' ) {
			if(confirm("是否保存数据") ) {
	            if( !submittask( id ) ){ 
	            	console.log(" validate erro.");
	            	return;
	            }
	        }
		} else{
			if($("#del"+id).css("visibility")=="visible"){
				 Msg.info("此任务项已经删除,如果需要撤销请重新绘制区域");
				 return;
			}else{
				if(confirm("确定删除数据")){
					console.log("----删除此条记录------")
		            deleteTaskItem(id);
		        }
			}
		}
		clearMap();//清空地图
	}
	function clearMap(){
		Msg.clearAll();
		Taskitem.operatorEndEditItem( taskitemid );
		Polygon.deleteEditPolygon(taskitemid,$("#href"+taskitemid)); //删除绘制的polygon
        PoiForm.clear();
		taskitemid = -1 ;
        amplify.publish( "task.end.edit" , taskitemid);
	}
	function opFinish(taskitemid,isDel){
        amplify.publish( "task.saved" , taskitemid,isDel );
    	amplify.publish( "boundsChange", mapObj.getBounds());
    	isDel===0 ? $("#del"+taskitemid).css("visibility","visible"):
    					$("#del"+taskitemid).css("visibility","hidden");
        return true;
	}
	
	
	function deleteTaskItem(taskitemid){
		$.ajax({
			type : "POST",
			url : path + "/facility/delete",
			data : "taskitemid=" + taskitemid,
			success : function(data) {
				if(data.code == '100') {
					opFinish(taskitemid,0);
				}else if(data.code == '300'){
					window.location.href= '/index';
				}
			}
		});
	    return true;//
	}
	function beginEditPolygon(id, btn) {
		taskitemid = id;// 标记当前作业项
	    Msg.info('查询中... ...');
		Taskitem.operatorBeginEditItem( btn );							//改变点击编辑的样式
		var taskitem = Taskitem.getTaskitem( taskitemid );				//获取此任务项的anmeChn,status,city,mesh
		$("#searchKey").val( taskitem.nameChn );        
		Map.search( taskitem.city );
		console.log("发布“task.begin.edit”渲染右侧栏和出现参考地图和绘图工具！");
        amplify.publish( "task.begin.edit" , {searchKey:taskitem.nameChn
        				,city:taskitem.city, taskitemid:taskitemid,taskstatus: taskitem.status} );
	}
	function taskItemProVal(){
		
	}
	function submittask(taskitemid) {
		if($('#editPoint').val() == "结束编辑顶点") {
			Polygon.editPolygon(taskitemid, $('#editPoint'));
		}
		var poi_id = validate('POI_ID');
		var polygonArea = polygonEdit.getArea();
		var polygon = polygonEdit.toString();
		console.log("面积："+polygonArea);
		console.log("中心点坐标为："+polygonEdit.getBounds().getCenter());
		if(poi_id == '' || polygon == ''){
			Msg.error('没有POI_ID 或者 没有绘制polygon');
			return false;
		}
		var status = $('#status'+taskitemid).html();
		var name_chn = validate('NAME_CHN');
		if(name_chn ==''){alert('没有中文名称');return false;}
		var mesh = validate('MESH');
		if(mesh ==''){alert('没有图幅');return false;}
		var fa_type = validate('FA_TYPE');
		if(fa_type ==''){alert('没有FA TYPE');return false;}
		var fa_flag = validate('FA_FLAG');
		if(fa_flag ==''){alert('没有FA FLAG');return false;}
		var disp_class = validate('DISP_CLASS');
		if(disp_class ==''){alert('没有dis_class');return false;}	
	   	var facility = new FacilityPro(name_chn,mesh,poi_id,disp_class,fa_flag,polygon,taskitemid,fa_type,polygonArea);
	   	saveTaskItem(facility,taskitemid);
	   	console.log(JSON.stringify(facility));
	    return true;
	}
	function saveTaskItem(facility,taskitemid){
		$.ajax({
			type : "POST",
			url : path + "/facility/save",
			data : "facility=" + JSON.stringify(facility),
			success : function(data) {
				if(data.code == '100') {
					opFinish(taskitemid,1);
				}else if(data.code == '300'){
					window.location.href= '/index';
				}
			}
		});
	}
	
	
	function validate(e) {
		var value = $("#"+e).val();
		if ( ! value ) {
			$("#"+e).focus();
			$('#properties tr').css("border-top-color", "#888888");
			$("#"+e).parent("td").css("border-color", "red");
			return "";
		}
		return value;
	}
	function FacilityPro(name_chn, mesh, poi_id, disp_class, fa_flag, polygon, taskitem_id, fa_type,polygonArea) {
		console.log("加载facility/facility，创建facility对象（）");
		this.gid = $("input#GID").val();
		this.fa = $("input#FA").val() ;
		this.fa_id = $("input#FA_ID").val();
		this.name_chn =name_chn;
		this.mesh = mesh;
		this.poi_id = poi_id;
		this.disp_class = disp_class ;
		this.fa_flag = fa_flag ;
		this.area_flag = $("#AREA_FLAG").val();
		this.polygon = polygon;
		this.taskitem_id = taskitem_id;
		this.fa_type = fa_type;
		this.precision = $("#PRECISION").val();
		this.sources = $("#SOURCES").val();
		this.proname = $("input#PRONAME").val();
		this.fatype_id = $("input#TYPE_ID").val();
		this.polygonArea = polygonArea;
	}
	return {init: init};
	
});