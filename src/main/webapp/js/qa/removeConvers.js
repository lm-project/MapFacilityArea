define(function(){
	var Marker = Polyline = Polygon = null;
	
	function removeConvers() {
		Marker.clearMarker();
		Polyline.removePolyline();
		Polygon.removePolygon();
	}
	
	
	
	function init(marker,polygline,polygon){
		Marker = marker;
		Polyline = polygline;
		Polygon = polygon;
		$("#removeCovers").on("click",function(){		//移出地图上的标记覆盖物
			removeConvers();
		});
	}
	
	return {
		init:init,
		removeConvers:removeConvers
	}
});
