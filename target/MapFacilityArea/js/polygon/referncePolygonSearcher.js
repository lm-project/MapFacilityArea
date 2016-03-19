
var bdPolygonArr = new Array(); 	// 存放搜索记录绘制参考面对象
var qqPolygonArr = new Array(); 	// 存放搜索记录绘制参考面对象
define(function() {
	var My = {}, Role, Polygon,createPoint;
	var polygonBdu = null; 			// 搜索后绘制参考面对象
	var polygonQq = null;			// 搜索后绘制参考面对象
    My.isOpen = false;
    var cache = {};
    function initEvent( role, polygon,PointPath) {
    	Role = role, Polygon = polygon,createPoint = PointPath;
    	amplify.subscribe("poi.auto.finish",function(data) {
    		console.log("订阅poi.auto.finish，抓取百度   qq的polygon并显示");
            $("#refer").css("display", "block");
            searchRef(data.searchKey, data.city);
        });  
        amplify.subscribe("task.end.edit",function(data) {
            $("#refer").css("display", "none");
            earseBQ();
        });
        amplify.subscribe("deletePolygon",function() {
            console.log(" receive delete msg...");
            if ( ! Role.isQa ) {
	            $("#referpolygons1 a[value='B']").text("关联");
	            $("#referpolygons2 a[value='Q']").text("关联");
            }
        });
        console.log("initEvent Ref_Polygon_Searcher");
    }

    function attachArea(path,hrefObj) {
	    if($(hrefObj).text() === "关联") {
		    $(hrefObj).text("已关联");
            $("#polygon").val(path);
            $(hrefObj).attr('value') === 'Q'? $("#referpolygons1 a[value='B']").text("关联") :
                									$("#referpolygons2 a[value='Q']").text("关联");
            Polygon.addEditPolygon(path);
	    } else {
		    $(hrefObj).text("关联");
            $("#polygon").val('');
	        $("#tname"+taskitemid+" input[id=editPoint]").remove();
	        $("#href"+taskitemid).remove();
	    }
	    
    }


    var searchRef = function(text,helptext){
    	console.log("查询百度，qq的polygon并显示到地图！");
    	if( cache[helptext + text]){
    		 displayResult(cache[helptext + text]);
    		 return;
    	}
	    $.ajax({
		    type: "post",
		    url : path + "/polygon/searcher",
		    dataType: 'json',
		    data: 'searchKey='+helptext+text,
		    error: function(XMLHttpRequest, error, errorThrown){  
		    	console.log(error);
		    }, 
		    success: function(data){
                cache[helptext + text]= data;
                displayResult(data);
	        }
        });
    }
    var displayResult = function(data) {
		earseBQ();
		if(data.code == '100'){
		    var ass = data.restring.split(";");
		    displayRefPolygon(ass[0],"#ff0000",bdPolygonArr,"BD","slidercontainer1","#referpolygons1");
		    displayRefPolygon(ass[1],"#00CC66",qqPolygonArr,"QQ","slidercontainer2","#referpolygons2");
        }else {
        	$('.top1').html("参考信息(未找到)");
        }
		amplify.publish( "displayResult" , {});
    }
    var earseBQ = function() {
		$("#referpolygons1").text("");
		$("#referpolygons2").text("");
        if(polygonBdu){
            polygonBdu.setMap(null);
            console.log("delete bd ");
        }
        if(polygonQq) {
            polygonQq.setMap(null);
            console.log("delete q");
        }
    }
    
    function addRefrenceElement(type,slidercontainer,referpolygons,color,path,length){
    	var attachTxt = "";
    	if ( ! Role.isQa) {
			attachTxt = "<a class='small a orange floatr' target='_blank' value="+type.charAt(0)+">关联</a>";
		}
		var referStr = "<ul style='padding:3px;color:"+color+"'>" +
				"<li><span>"+type+"参考面</span>" +
				"<a id='hidden' class='small a orange floatr'>隐藏</a>"+ attachTxt+"</li>"+ 
				"<li>顶点数:<span >"+length+"</span></li><li>"+ 
				"<span style='float:left; height:24px'>轮廓线透明度</span>" +
				"<div style='float:left;width:200px;margin: 10px 0 0 5px;' " +
				"id="+slidercontainer+"></div></li></ul>"
		$(referpolygons).html(referStr);
		$(referpolygons+" a[value="+type.charAt(0)+"]").on("click",function(){
        	attachArea(path,this);
        });
    }
    
	var hideRefPolygon = function(referpolygons,obj) {
		$(referpolygons+" a#hidden").on("click", function(){
		    if($(this).text() === "隐藏"){
		        $(this).text( "显示");
		        obj.hide();
		    }else{
		        $(this).text( "隐藏");
		        obj.show();
		    }
		});       
	} 
    
    function displayRefPolygon(polygonStr,color,polygonArr,type,slidercontainer,referpolygons){
    	if(!polygonStr || $.trim(polygonStr)===''){
			return;
        }
		var path = createPoint(polygonStr);
	    var polygon = createPolygon(path,color);
	    type==="BD"? polygonBdu = polygon:polygonQq = polygon;
	    polygonArr.push(polygon);
		addRefrenceElement(type,slidercontainer,referpolygons,color,polygonStr,path.length);//增加参考消息
		hideRefPolygon(referpolygons,polygon);							  //隐藏显示参考polygons事件
        AddSlider("#"+slidercontainer,polygon);							  //增加polygon透明度滑动条
    }
    
    function createPolygon(polygonArr,color){
    	return new AMap.Polygon({ 
    		   map: mapObj,
    		   path:polygonArr,	//设置多边形边界路径
    		   strokeColor:color, //线颜色
    		   strokeOpacity:0.9, //线透明度 
    		   strokeWeight:2,    //线宽 
    		   strokeStyle:"solid",
    		   fillOpacity: 0//填充透明度
    		  }); 
    }
    

    function AddSlider(slider,obj){
    	 $(slider).noUiSlider({
		        start: 0.9,
		        range: {
		            'min': 0,
		            'max': 1
		        }
		    }).on({
		        slide: function(){
			        var polygonOption = {strokeOpacity:0.9};
			        polygonOption.strokeOpacity = $(this).val();
			        obj.setOptions(polygonOption);
		        }
		    });
    }
    
    return {init:initEvent};
});







//var geotoMct = function(mctXX, mctYY){
//    var x_pi = 3.14159265358979324 * 3000.0 / 180.0;
//    var mctXY = new BMap.Pixel(mctXX,mctYY);    
//    var projection2 = baiduMap.getMapType().getProjection();
//    var LngLat = projection2.pointToLngLat(mctXY);
//    lng = LngLat.lng, lat = LngLat.lat;
//    var x = lng - 0.0065, y = lat - 0.006;
//    var z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
//    var theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
//    lng = z * Math.cos(theta);
//    lat = z * Math.sin(theta);
//    lngs += parseFloat(lng);
//    lats += parseFloat(lat);
//
//    return new AMap.LngLat(lng,lat);
//}