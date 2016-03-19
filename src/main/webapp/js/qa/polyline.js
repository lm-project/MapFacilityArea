define([ '../qa/checkresult' ], function( CheckResult){
	var drawPolylineObj = RemoveConvers = null;
	
	var polylineOption = {  
		strokeColor:"red", 
		zIndex: 10,
		strokeOpacity:1,  
		strokeWeight:1,
	};
	
	function addListner(removeConvers) {
		RemoveConvers = removeConvers;
		$("#playline").on("click",function() {
			mapObj.setDefaultCursor("crosshair");
			closeTools(mouseTool);
			closeTools(mouseTool2);
		    mapObj.plugin(["AMap.MouseTool"],function(){  	// 在地图中添加MouseTool插件 
		        mouseTool = new AMap.MouseTool(mapObj);   
		        mouseTool.polyline( polylineOption );  	    // 使用鼠标工具绘制多边形
		        AMap.event.addListener( mouseTool,"draw",function(e){  
		        	RemoveConvers.removeConvers();
			        drawPolylineObj = e.obj;
		        }); 
		    });
		});
	}
	
	function getPolylinePosition(){
		if(drawPolylineObj!=null){
			return drawPolylineObj.getPath();
		}
		return null;
	}
	
	function removePolyline() {
		if(drawPolylineObj!=null){
			drawPolylineObj.setMap(null);
			drawPolylineObj = null;
		}
	}
	
	return {
		init : addListner,
		polyline : getPolylinePosition,
		removePolyline: removePolyline
	}
	
});

