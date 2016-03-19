define(function(){
	console.log("加载处理坐标串");
	function createPoint( path ) {
		var as = path.replace(/;/g,",").split(",");
		var points = [];
		for(var i = 0;i < as.length-1; i=i+2){
			points.push(new AMap.LngLat(as[i], as[i+1]));
		}
		return points;
	}
	return createPoint;
});  