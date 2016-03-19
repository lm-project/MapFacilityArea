
define(function(){
	
	var polygons, Polylines, createPoint; 
	
	function addPolyline(points){
		return new AMap.Polyline({
			map : mapObj,
			path : points,
			strokeColor : "purple",
			strokeOpacity : 0.5,
			zIndex: 1,
			strokeWeight : 1,
		});
	}
	
	function successFunction(data){
		var p = eval(data.restring);
		for ( var i = 0; i < p.length; i++) {
			var taskitemid = p[i].taskitem_id;
			var points = createPoint(p[i].qamark);
			if( polygons.get(  taskitemid ) ) {
				continue;
			}
			polygons.add( taskitemid ,addPolyline());
		}
	}
	
	function render( bounds ) {
			$.ajax({
				type : "GET",
				url : path + "/getPolylines/"+ bounds,
				success : function(data) {
					if(data.code == 300){
						alert(data.description);
						window.location.href= path+"/"+data.restring;
					}else {
						successFunction(data);
					}
				}
			});
			console.log( "当前视野内质检图形数量 :  " + polygons.length );
	}
	
	
	return function(PolygonList,polyline,PointPath) {
		polygons = PolygonList, Polylines = polyline, createPoint = PointPath; 
		amplify.subscribe("boundsChange", function(data) {
			render( data.toString().replace(/;/, "&") );
		});
	}
	
});
