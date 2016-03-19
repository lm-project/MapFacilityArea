function geotoMct(mctXX, mctYY){
  		 var baiduMap = new BMap.Map("container");
 	     var x_pi = 3.14159265358979324 * 3000.0 / 180.0;
         var mctXY = new BMap.Pixel(mctXX,mctYY);    
         var projection2 = baiduMap.getMapType().getProjection();
         var LngLat = projection2.pointToLngLat(mctXY);
         lng = LngLat.lng, lat = LngLat.lat;
         var x = lng - 0.0065, y = lat - 0.006;
         var z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
         var theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
         lng = z * Math.cos(theta);
         lat = z * Math.sin(theta);
 	     var lngs = Math.abs(parseFloat(lng));
 	     var lats = Math.abs(parseFloat(lat));
 	     return lngs+","+lats+ ",";
     }


function batchGeotoMct( context ){
	var rs = '';
	var array = JSON.parse( context );
	for(var  j = 0 ; j < array.length-1 ; j=j+2 ){
		rs += geotoMct(array[j], array[j+1]);
	}
	return rs.substr(0, rs.length-1);
}