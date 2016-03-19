define(['../msg/msg', '../poi/poiform','../task/taskitem'],function( Msg, PoiForm, Taskitem ) {
	var Map, CheckResult, Polyline;
	
	function startCheck( btn ,id ){
		taskitemid = id;										// 标记当前作业项
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
	function addEditItemEvent(){
//		Taskitem.qaInitBtnName();
		console.log("qa edit Map inited.");
		$('#main_left [id=edit_item]').live('click',function(){
			$(this).text()=== "结束检查" ? endCheck(): startCheck(this, $(this).attr('value'));
		});
	}
	
	function subscribeEditBegin(){
		amplify.subscribe("task.begin.edit",function( msg ) {
			$("#main_right").css("display", "block");
            $("#map").css('margin-right','250px');
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
		var remark = $.trim($('#polyline_memo').val());
		String.prototype.replaceAll = function(s1,s2) { 
		    return this.replace(new RegExp(s1,"gm"),s2); 
		}
		if( (remark == '' && CheckResult.polylines.length > 0) || (remark != '' && CheckResult.polylines.length == 0)) {
			if(remark == ''){
				alert("请填写备注信息！");
				$('#polyline_memo').focus()
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
		    $('#polyline_memo').val( remark );
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

	function init(map, checkResult, polyline) {
		Map = map, CheckResult = checkResult, Polyline = polyline;
		addEditItemEvent();
		subscribeEditBegin();
		subscribeEditEnd();
	}
	
	return {
		init: init
	}
});