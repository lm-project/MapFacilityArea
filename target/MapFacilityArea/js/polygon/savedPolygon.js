
define(function($) {
	console.log("加载polygon/savePolygon,并且订阅boundsChange ，当发布以后");
	var polygons = createLinkList(); 
	
	function addPolygon(points){
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
			var points = [];
			var as = p[i].geom.split(",");
			for ( var j = 0; j < as.length - 1; j = j + 2) {
				points.push(new AMap.LngLat(as[j], as[j + 1]));
			}
			if( polygons.get(  taskitemid ) ) {
				removePolygon(taskitemid);
			}
			polygons.add( taskitemid , addPolygon(points));
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
			console.log( "saved polygons.length :  " + polygons.length );
	}
	
    
	function removePolygon( taskitemid ) {
		polygons.get( taskitemid ).setMap(null);
		polygons.del( taskitemid );
	}
	(function() {
		amplify.subscribe("boundsChange", function(data) {
			render( data.toString().replace(/;/, "&") );
		});
	}());
}(jQuery));
