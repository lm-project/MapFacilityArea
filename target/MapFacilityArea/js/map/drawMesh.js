var meshBoxPolygon = null;
var DrawMesh = (function($) {
	var DrawMesh = function() {
		amplify.subscribe("mesh.loaded.finish", function(rect) {
//			var mesh = $('#MESH')val();
			
			var path = [];
			var as = rect.box.split(",");
			lngs = 0,lats = 0;
			for(var i = 0;i < as.length-1; i=i+2) {
				path.push(new AMap.LngLat(as[i],as[i+1]));
			}
			
			meshBoxPolygon = new AMap.Polygon({
					map : mapObj,
					path : path,
					strokeColor : "#FF6699",
					strokeOpacity : 0.9,
					strokeWeight : 4,
					fillOpacity : 0,
					zIndex : 1
				});
		});

		amplify.subscribe("mesh.clean", function() {
			console.log(1111+"____________________-");
			meshBoxPolygon.setMap(null);
		})
	};
	
	return new DrawMesh();
}(jQuery));
