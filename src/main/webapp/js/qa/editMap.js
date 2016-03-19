define(['../msg/msg', '../poi/poiform','../task/taskitem'],function( Msg, PoiForm, Taskitem ) {
	var Map, CheckResult, Polyline, Marker, polygon;
	function startCheck( btn ,id ){
		taskitemid = id;										// 标记当前作业项
		getQaResultListByTaskitemid();
		Taskitem.qaBeginEditItem( btn );						// 改变检查按钮样式
		$("#searchKey").val( Taskitem.getNameChn( taskitemid ) );
		Map.search( Taskitem.getCity( taskitemid ));			//搜索poi通过amap api
		console.log("发布task.begin.edit");
    	amplify.publish( "task.begin.edit" , {searchKey:Taskitem.getNameChn( taskitemid )
    				,city:Taskitem.getCity( taskitemid ),taskitemid:taskitemid} );
	}
	function endCheck(){
		if( confirm("是否保存数据?") ) {
	           var isCommit = submittask();
	           if(!isCommit) {
	        	   return;
	           }
	    }
        mapObj.clearMap();
        Polyline.clearAllPolyline( CheckResult.polylines );
		Taskitem.qaEndEditItem( taskitemid );
        amplify.publish( "task.end.edit" , taskitemid);
	}
	
	function saveQaInfoSucess(id){
		$("#qc_code").val("");
		$("#des").val("");
		Marker.clearMarker();
		var str = "";
		if($("#qaInfo").attr("id")=="qaInfo"){
			str="<a style='cursor:pointer;'>质检问题"+qaresult+"</a>";
		}
		var str = "<li id='qaInfo'><a style='cursor:pointer;'><span class='span1'>质检ID:</span><span class='span2'>"+id+"</span></a></li>";
		$("#ul"+taskitemid).append(str);
	}
	function renderQaresultInfo(qaResultList){
		var str = "";
		for(var qaresult in qaResultList){
			console.log(qaresult+":"+qaResultList[qaresult].taskitem_id);
			 str += "<li id='qaInfo'><a style='cursor:pointer;'>质检问题"+qaresult+"</a>" +
			 		"<div style='display:none;' id='qaresult"+qaResultList[qaresult].taskitem_id+"'>" +
			 		"<span id='qccode'>"+qaResultList[qaresult].qc_code+"</span>" +
			 		"<span id='qa_des'>"+qaResultList[qaresult].des+"</span>" +
			 		"<span id='op_feedback'>"+qaResultList[qaresult].feedback+"</span>" +
			 		"<span id='op_exp'>"+qaResultList[qaresult].exp+"</span>" +
			 		"<span id='qamark'>"+qaResultList[qaresult].qamark+"</span>" +
			 		"</div></li>";
		}
		$("#ul"+taskitemid).append(str);
	}
	
	function lookQaResult(obj){
		alert($(obj).next().find("#qccode"));
	}
	
	function getQaResultListByTaskitemid(){
		console.log("query qaresult by taskitemid :"+taskitemid);
		$.ajax({
			type : "GET",
			url : path + "/qa_result/getQaInfo/"+taskitemid,
			data : "taskitemid=" + taskitemid,
			success : function(data) {
				var qaResultList = eval(data);
				console.log("query qaresult by taskitemid length : "+qaResultList.length);
				if(qaResultList.length>0) {
					renderQaresultInfo(qaResultList);
			    	$("li[id=qaInfo] a").click(function(){
						lookQaResult(this);
					});
				}else{
					window.location.href= '/index';
				}
			}
		});
	}
	function saveQaInfo(resultObj){
		$.ajax({
			type : "POST",
			url : path + "/qa/qa_result/save",
			data : "qa_result=" + JSON.stringify(resultObj),
			success : function(data) {
				if(data.code == '100') {
					saveQaInfoSucess(data.description);
				}else if(data.code == '300'){
					window.location.href= '/index';
				}
			}
		});
		return true;
	}
	function getQaInfo(){
		if(!$.checkValue("qaCon")){//检查表单
			return null;
		}
		if(!taskitemid){
			Msg.error("任务项id为空！");
			return null;
		}
		if( confirm("是否保存数据?") ) {
			var qa_mark = Marker.marker() ? Marker.marker() : 
							Polyline.polyline() ? Polyline.polyline() : 
								Polygon.polygon() ? Polygon.polygon() : "";
			var resultObj = {
					task_item_id: taskitemid,
					qamark : qa_mark.toString(),
					qc_code : $("#qc_code").val(),
					tuceng : $("#tuceng").val(),
					des :  $("#des").val()
			}
			return resultObj;
	    }
		return null;
	}
	function addEditItemEvent(){
//		Taskitem.qaInitBtnName();
		console.log("qa edit Map inited.");
		$('#main_left [id=edit_item]').live('click',function(){
			$(this).text()=== "结束检查" ? endCheck(): startCheck(this, $(this).attr('value'));
		});
		$('#main_right').on('click',"#saveQA",function(e){
			var resultObj = getQaInfo();			//获取符合条件的参数质检信息
			if(resultObj){
				saveQaInfo(resultObj);				//保存符合条件的质检信息
			}
		});
	}
	
	function subscribeEditBegin(){
		amplify.subscribe("task.begin.edit",function( msg ) {
			$("#map").css('margin-right','250px');
			$("#main_right").css("display", "block");
			$.ajax({
				type : "GET",
				url : path + '/common/taskitem/' + msg.taskitemid,
				success : function(data) {
					fillData( data ,msg.taskitemid);
				}
			});
			
        });
	}
	
	function subscribeEditEnd() {
        amplify.subscribe("task.end.edit",function( taskitemid ) {
			$("#main_right").css("display", "none");
            $("#map").css('margin-right','0px');
        });
        console.log('qa inited ....');
	}
	
	function fillData( data, taskitemid ) {
		$("#taskTemplate").load(path+"/js/template/poiEditForm.htm", function(){
			var scriptTemplate = Handlebars.compile($('#taskTemplate').text());
	        var rs = scriptTemplate( data );
        	$('#poiEditForm').html(rs);
            loadQaresult( taskitemid );
            amplify.publish("poiEditForm.loaded",{});
		});
	}
		
	function submittask() {
		var remark = $.trim($('#des').val());
		String.prototype.replaceAll = function(s1,s2) { 
		    return this.replace(new RegExp(s1,"gm"),s2); 
		}
		if( (remark == '' && CheckResult.polylines.length > 0) || (remark != '' && CheckResult.polylines.length == 0)) {
			if(remark == ''){
				alert("请填写备注信息！");
				$('#des').focus()
			}else {
				alert("质检形状不存在！");
			}
			return false;
		}
		remark = remark.replaceAll("\n" , " ");
		CheckResult.remark = remark;
		console.log(JSON.stringify(CheckResult.toString()));
		$.ajax({
			type : "POST",
			url : path + "/facility/check/save",
			data : "facility=" + JSON.stringify(CheckResult.toString()),
			success : function(data) {
				if(data.code == '100') {
	                amplify.publish( "task.saved", taskitemid );
	               
				}else if(data.code == '300'){
					window.location.href= '/index';
				}
	            
			}
		});
		return true;
	}
	
	function loadQaresult( taskitemid ) {
		$.ajax({
			type : "GET",
			url : path + "/facility/check/"+taskitemid,
			success : function( data ) {
				showEditPolyline2ndRemark( data );
			}
		});
	}
	
	function showEditPolyline2ndRemark( datas ) {
        if( datas.length > 0 ) {
        	String.prototype.replaceAll = function(s1,s2) { 
    		    return this.replace(new RegExp(s1,"gm"),s2); 
    		}
    		var remark = datas[0].remark.replaceAll(" " , "\n");
		    $('#des').val( remark );
        }

		for( var idx = 0; idx < datas.length; idx++ ) {
            try {
			    var data = datas[idx];
			    var id = data.id;
			    var points = [];
			    var as = data.qamark.replace(/LINESTRING(.*)/,"$1")
				    .replace(/\(/,'').replace(/\)/,'').split(",");
			    for ( var j = 0; j < as.length - 1; j = j + 2) {
				    points[j] = new AMap.LngLat(as[j], as[j + 1]);
			    }
			
			    Polyline.addPolyline(new AMap.Polyline({
				    map : mapObj,
				    path : points,
				    strokeColor : "blue",
				    zIndex: 10,
				    strokeOpacity : 1,
				    strokeWeight : 3
			    }));
            }catch( err ) {
                console.log( err );
            }
		}
	}
	
	function init(map, checkResult, polyline,marker,Polygon) {
		Map = map, 
		CheckResult = checkResult, 
		Polyline = polyline,
		Marker = marker;
		polygon = Polygon;
		addEditItemEvent();
		subscribeEditBegin();
		subscribeEditEnd();
	}
	
	return {
		init: init
	}
});