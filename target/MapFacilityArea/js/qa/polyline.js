define([ 'map/map','../qa/checkresult' ], function( Map,CheckResult){
	
	var polygons = createLinkList();
	
	var QaCheckResult = {};
	
	var polylineOption = {  
		strokeColor:"blue", 
		zIndex: 10,
		strokeOpacity:1,  
		strokeWeight:3,
	};
	
	function addListner() {
		$("#playline").on("click",function() {
			mapObj.setDefaultCursor("crosshair");
			$(".MC001,.MC002,.MC003").css("background-position","0px 0px");
			closeTools(mouseTool);
			closeTools(mouseTool2);
			
			// 在地图中添加MouseTool插件
		    mapObj.plugin(["AMap.MouseTool"],function(){   
		        mouseTool = new AMap.MouseTool(mapObj);   
		        mouseTool.polyline( polylineOption );   // 使用鼠标工具绘制多边形
		        AMap.event.addListener( mouseTool,"draw",function(e){  
			        var drawObj = e.obj;
	                addPolyline( drawObj );
		        }); 
		    });
		});
		
		 amplify.subscribe("task.begin.edit",function( taskitemid ) {
            $("#main_right").css("display", "block");
            $("#map").css('margin-right','250px');
        });
	}
	
	function addPolyline( drawObj ) {
		var path = drawObj.getPath();
		CheckResult.polylines.add( path.toString(), drawObj);
		AMap.event.addListener( drawObj, "mouseover", function(e){  
            if( confirm("是否要删除?") ) {
                removePolyline( drawObj );
            }
        });
		        
	}
	
	function clearAllPolyline() {
		var rs = CheckResult.polylines.getAll();
		for(var i=0 ; i < rs.length ; i++) {
			if( rs[i] ) {
				rs[i].setMap(null);
			}
		}
		 CheckResult.polylines = createLinkList();
	}
	
	function removePolyline( drawObj ) {
		drawObj.setMap(null);
		var path = drawObj.getPath().toString();
		deletePolyline(path);
	}
	
	function deletePolyline(path) {
		 console.log("before del polyline length:" + CheckResult.polylines.length );
		if(CheckResult.polylines.get( path )){
			console.log("remove polyline . " + path + ",del:" + CheckResult.polylines.del( path ) );
			console.log("after del polyline length:" + CheckResult.polylines.length );
		}
	}
	
	function init() {
		addListner();
	}
	
	return {
		init : init,
		addPolyline: addPolyline,
		clearAllPolyline: clearAllPolyline
	}
	
});
