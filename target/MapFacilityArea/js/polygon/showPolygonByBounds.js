define(function() {
	console.log("加载polygon/showPolygonByBounds,并且订阅boundsChange ，当发布以后显示该范围内所有的已存在的设施区域");
	var polygons,CreatePoint;
	function addBoundsPolygon(points){
		return new AMap.Polygon({
			map : mapObj,
			path : points,
			strokeColor : "purple",
			strokeOpacity : 0.5,
			strokeWeight : 2,
			fillColor : "gray",
			fillOpacity : 0,
			zIndex: 0
		});
	}
	
	function show( data ) {
		var p = eval(data.restring);
		for ( var i = 0; i < p.length; i++) {
			var taskitemid =  p[i].taskitem_id ;
			if( polygons.get(  taskitemid ) ) {
				continue;
			}
			polygons.add( taskitemid , addBoundsPolygon(CreatePoint(p[i].geom)));
		}
    }

	var render = function render( bounds ) {
			$.ajax({
				type : "GET",
				url : path + "/getPolygons/"+ bounds,
				success : function(data) {
					if(data.code == 300){
						alert(data.description);
						window.location.href= path+"/"+data.restring;
					}else {
                        show(data);
					}
				}
			});
	}
	
    
	function removePolygon( taskitemid ) {
		polygons.get( taskitemid ).setMap(null);
		polygons.del( taskitemid );
	}
	
	
	 function boundsChange(PolygonList,createPoint) {
		polygons = PolygonList;
		CreatePoint = createPoint;
		amplify.subscribe("boundsChange", function(data) {
			render( data.toString().replace(/;/, "&") );
		});
	}
	
	 return boundsChange;
	 
});
