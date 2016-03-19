define(function() {
	var Role = {isQa : "qa" === sessionStorage.getItem("role")}
	console.log("登陆的角色为qa吗？："+Role.isQa);
	Role.isQa ? loadQa():loadOperator();
	
	function loadOperator() {
		console.log("加载作业项!");
		$('#marker').remove();
		$('#playline').remove();
		require([
		         'polygon/showPolygonByBounds', 
		         'map/map',
		         'map/tabs',
		         'map/initTools',
		         'map/gettask',
		         'polygon/checkEditPolygon',
		         'operator/editMap',
		         'task/taskitem',
		         'polygon/polygon',
		         'map/amapDrawTool',
		         'map/refernceLayer',
		         'poi/amapPoi',
		         'polygon/referncePolygonSearcher',
		         'map/meshUtils',
		         'common/polygonList',
		         'common/pointPath'
		         ],
			function(ShowPolygonByBounds,Map,Tabs,InitTools,GetTask,CheckEditPolygon,EditMap,Taskitem,Polygon,DrawTool,
					RefernceLayer,POI,ReferncePolygon,MeshUtils,PolygonList,PointPath) {
				ShowPolygonByBounds(PolygonList,PointPath);
				Map.mapInit();
				GetTask.getTaskItems();
				EditMap.init( Map,Taskitem,Polygon,PolygonList,CheckEditPolygon);
				Polygon.init(Taskitem,PolygonList,PointPath,Role);
				POI.init(Map,Role,Polygon);
				ReferncePolygon.init(Role,Polygon,PointPath);
				MeshUtils(PointPath);
		});
	}
	
	function loadQa() {
		console.log("加载质检项!");
		require(['map/map', 'map/initTools', 'map/gettask' ,'poi/amapPoi', 'qa/editMap', 
		         'qa/checkresult', 'qa/marker','qa/polyline','polygon/polygon','qa/removeConvers','task/taskitem','common/polygonList',
		         'common/pointPath',
					'qa/savedPolylines','polygon/referncePolygonSearcher','map/amapDrawTool','map/refernceLayer','map/tabs'],
		function(Map,initTools, GetTask, Poi,EditMap, CheckResult,Marker, Polyline,Polygon,RemoveConvers,
				Taskitem,PolygonList,PointPath,Saved) {
			console.log("加载作业项!");
			Map.mapInit();
			GetTask.getTaskItems();
			EditMap.init(Map, CheckResult, Polyline,Marker,Polygon);
			RemoveConvers.init(Marker, Polyline, Polygon);
			Marker.init(RemoveConvers);
			Polyline.init(RemoveConvers);
			Saved(PolygonList,Polyline,PointPath);
			Polygon.init(Taskitem,PolygonList,PointPath,Role,RemoveConvers);
			Poi.init(Map, Role, Polygon);
		  
		});
	}
	
	
});
