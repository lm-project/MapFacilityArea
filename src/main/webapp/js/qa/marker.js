define(function(){
	var marker = RemoveConvers = clickEvent = null;
	function addListner(removeConvers) {
		RemoveConvers = removeConvers;
		$("#marker").on("click",function() {
			mapObj.setDefaultCursor("pointer");	
			closeTools(mouseTool);
			closeTools(mouseTool2);
			clickEvent = AMap.event.addListener(mapObj,'click',function(e){
				RemoveConvers.removeConvers();
				addMarker(e.lnglat);
			});
		});
	}
	
	function addMarker(point) {
		marker = new AMap.Marker({				  
			icon:"../images/marker_sprite.png",
			position:point,
			draggable:true
		});
		marker.setMap(mapObj);  //在地图上添加点
		if(clickEvent!=null){
			AMap.event.removeListener(clickEvent);
			clickEvent = null;
		}	
	}
	
	function clearMarker() {
		if(marker!=null){
			marker.setMap(null);
			marker = null;
		}
	}
	function getMarkerPosition(){
		if(marker!=null){
			return marker.getPosition();
		}
		return null;
	}
	function init() {
		addListner();
	}
	
	return {
		init : init,
		marker:getMarkerPosition,
		clearMarker:clearMarker
	}
	
});
