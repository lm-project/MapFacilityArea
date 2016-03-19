var meshBoxPolygon = null;
define(function(){
	function drawMeshPolygon(rect,createPoint) {
//		var mesh = $('#MESH')val();
		
		meshBoxPolygon = new AMap.Polygon({
				map : mapObj,
				path : createPoint(rect.box),
				strokeColor : "#FF6699",
				strokeOpacity : 0.9,
				strokeWeight : 4,
				fillOpacity : 0,
				zIndex : 1
			});
	}
	
	
	var MeshUtils = function(createPoint) {
		amplify.subscribe("mesh.loaded",function(data) {
			console.log("订阅mesh.loaded,显示mesh polygon");
			var center = mapObj.getCenter();
			drawMeshPolygon(xymeshBox(center.lng, center.lat),createPoint);
		});
	}
	
	var pi = 3.14159265358979324;
	var a = 6378245.0;
	var ee = 0.00669342162296594323;
	
	function transform_lon(wgLat, wgLon)	{
	    if (outOfChina(wgLat, wgLon))
	    {
	        mgLat = wgLat;
	        mgLon = wgLon;
	        return;
	    }
	    var ret;
	    var dLat = transformLat(wgLon - 105.0, wgLat - 35.0);
	    var dLon = transformLon(wgLon - 105.0, wgLat - 35.0);
	    var radLat = wgLat / 180.0 * pi;
	    var magic = Math.sin(radLat);
	    magic = 1 - ee * magic * magic;
	    var sqrtMagic = Math.sqrt(magic);
	    dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
	    dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
	    //mgLat = wgLat + dLat;
	    //mgLon = wgLon + dLon;
	    ret = wgLon + dLon;
	    return ret
	    
	}
	
	function transform_lat(wgLat, wgLon)	{
	    if (outOfChina(wgLat, wgLon))
	    {
	        mgLat = wgLat;
	        mgLon = wgLon;
	        return;
	    }
	    var ret;
	    var dLat = transformLat(wgLon - 105.0, wgLat - 35.0);
	    var dLon = transformLon(wgLon - 105.0, wgLat - 35.0);
	    var radLat = wgLat / 180.0 * pi;
	    var magic = Math.sin(radLat);
	    magic = 1 - ee * magic * magic;
	    var sqrtMagic = Math.sqrt(magic);
	    dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
	    dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
	    //mgLat = wgLat + dLat;
	    //mgLon = wgLon + dLon;
	    ret = wgLat + dLat;
	    return ret;
	    
	}        
	
	
	function transform(wgLat, wgLon, mgLat, mgLon)	{
	    if (outOfChina(wgLat, wgLon))
	    {
	        mgLat = wgLat;
	        mgLon = wgLon;
	        return;
	    }
	    
	    var dLat = transformLat(wgLon - 105.0, wgLat - 35.0);
	    var dLon = transformLon(wgLon - 105.0, wgLat - 35.0);
	    var radLat = wgLat / 180.0 * pi;
	    var magic = Math.sin(radLat);
	    magic = 1 - ee * magic * magic;
	    var sqrtMagic = Math.sqrt(magic);
	    dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
	    dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
	    mgLat = wgLat + dLat;
	    mgLon = wgLon + dLon;           
	}        
	
	
	function outOfChina(lat, lon)	{
	    if (lon < 72.004 || lon > 137.8347)
	        return true;
	    if (lat < 0.8293 || lat > 55.8271)
	        return true;
	    return false;
	}
	
	
	
	function transformLat(x, y)
	{
	    var ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
	    ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
	    ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
	    ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
	    return ret;
	}
	
	function transformLon(x, y)
	{
	    var ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
	    ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
	    ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
	    ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0 * pi)) * 2.0 / 3.0;
	    return ret;
	}
	
	//===================================================================
	
	//===================================================================
	//----------将火星坐标转换为WGS84坐标--------------------------------
	function mars2wgstransform_lon(mLat, mLon){
	    var wgLat, wgLon;
	    var x, y;
	    x = transform_lon(mLat, mLon);
	    y = transform_lat(mLat, mLon)
	    wgLon = 2 * mLon - x;
	    wgLat = 2 * mLat - y;
	    return wgLon;
	}
	
	
	function mars2wgstransform_lat(mLat, mLon){
	    var wgLat, wgLon;
	    var x, y;
	    x = transform_lon(mLat, mLon);
	    y = transform_lat(mLat, mLon)
	    wgLon = 2 * mLon - x;
	    wgLat = 2 * mLat - y;
	    return wgLat;
	}
	
	function xymeshBox(lng , lat){
		var result; //返回值
		var xycoord;
		var xx, yy;
		var deltaX, deltaY;
		var deltaXX, deltaYY;
		
		var H0, I;
		var HH;
		var deltaH, deltaI;
		
		var c, c1, c2, c3, c4, c5;
	
		deltaX = 6;
		deltaY = 4;
		deltaXX = 0.125;
		deltaYY = 5/60;
		
		xx = mars2wgstransform_lon(parseFloat(lat), parseFloat(lng));
		yy = mars2wgstransform_lat(parseFloat(lat), parseFloat(lng));
	
	
		H0 = Math.floor(yy / deltaY) + 1;
		HH = String.fromCharCode(H0 + 64);
		
		I = Math.floor(xx / deltaX) + 31;
	
	
		deltaH = deltaY / deltaYY - Math.floor( (yy- ((H0-1) * deltaY)) / deltaYY );
		deltaI = Math.floor((xx - ((I-31) * deltaX)) / deltaXX) + 1;
		
		c1 = HH;
		c2 = String(I);
	
		if (I < 10) {
			c2= "0" + c2;
		}
		c3 = "F";
	
		c4 = String(deltaH);
	
		if (deltaH < 10) {
			c4 = "00" + c4;
		}
		else if (deltaH < 100) {
			c4 = "0" + c4;	
		}
		 
		c5 = String(deltaI);
		
		if (deltaI < 10) {
			c5 = "00" + c5;
		}
		else if (deltaI < 100) {
			c5 = "0" + c5;	
		}
		
		c=c1 + c2 + c3 + c4 + c5;
	
		var H, L, h, l;
		var sCode;
	 	var delX;  
		var delY;
		var x, y, x1, y1; 
		var mmeshID, meshID;
	
		var i;
		
		var chkBox = document.getElementsByName("chkDisplay");
		meshID = c
	
		mmeshID = meshID.split(",");
		nRow=meshID.split(',').length;
	
		for (i=0; i<nRow; i++)  {
	
		//H=vbAsc(mmeshID[i].substring(1,0))-vbAsc("A");
		H=mmeshID[i].substring(1,0).charCodeAt(0)-"A".charCodeAt(0);
		L=mmeshID[i].substring(3,1);
		sCode=meshID.substring(4,3);
		h=mmeshID[i].substring(7,4);
		l=mmeshID[i].substring(10,7);
	
		switch(sCode) {
			case "A": 
				delX=6;
				delY=4;
				break; 
	
			case "B": 
				delX=6/2;
				delY=4/2;	
				break; 
	
			case "C": 
				delX=6/4;
				delY=4/4;		
				break; 
	
			case "D": 
				delX=6/12;
				delY=4/12;		
				break; 
	
			case "E": 
				delX=6/24;
				delY=4/24;		
				break; 
	
			case "F": 
				delX=6/48;
				delY=4/48;		
				break; 
	
			case "G":
				delX=6/96;
				delY=4/96; 
				break; 
	
			case "H":
				delX=6/192;
				delY=4/192; 
				break; 
	
			default: 
				delX=0.125;
				delY=1/12;
				break; 
		} 
		
		x=(L-31)*6 + (l-1) * delX;
		//y=(H-1)*4 + ( 4/delY-h) * delY;
		y=(H+1)*4 - ( h-1) * delY;
		
		x1= x + delX;
		y1= y - delY;
		
		//======================
		//转换成变形后坐标
		var px1, py1, px2, py2, px3, py3, px4, py4;
		
		px1=transform_lon(y, x);
		py1=transform_lat(y, x);
		
		px2=transform_lon(y, x1);
		py2=transform_lat(y, x1);
			
		px3=transform_lon(y1, x1);
		py3=transform_lat(y1, x1);
		
		px4=transform_lon(y1, x);
		py4=transform_lat(y1, x);		
		//======================
		
	
		if (i==0) {
			xMax=px3;
			xMin=px1;
			yMax=py1;
			yMin=py3;
		} else {
		
			if (xMax < px3) {
				xMax=px3
			}
		
			if (xMin > px1) {
				xMin=px1
			}
			if (yMax < py1) {
				yMax=py1
			}
		
			if (yMin > py3) {
				yMin=py3
			}
		}
		
		result = {
				box :  px1 + ","+py1 +"," + px2 + "," + py2 + "," +px3 +"," + py3 +","+ px4+ ","+ py4,
				mesh : meshID
		};
		
	  }//end form
		
		return result;
	}
	
	return MeshUtils;
});


