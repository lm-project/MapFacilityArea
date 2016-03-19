
var mouseTool = null; 			// 鼠标绘图工具
var mouseTool2 = null; 			// 鼠标放大缩小等工具 var mouseTool2 = null; 		// 是否打开图形编辑
var polygonEdit = null; 		// 修改参考面对象

define([], function(){
	console.log("加载map/map,并且返回一个对象包含 search,addmarker,mapInit");
	console.log("视野改变事件，触发boundsChange，发布“触发boundsChange”,参数为bounds");
	var markers = [];
	var msearch = null; // 地点搜索服务对象
	var keys = {city:'', searchKey:''};//搜搜关键字
	var f = 0; 	// 标记是否已添加参照地图   	
	(function(){
		searchKuangLisener();	// 监听搜索框的变化
		addSearchResultEve();	// 针对搜索增加鼠标移上移出事件
		keyDownSearch();  		//增加按Enter键搜索poi
		closeDrawObj();	  		//鼠标点击MAP左和上方时，关闭鼠标绘图对象
		loadExistedData();		//复选框选中自动加载已有数据
		clearHistoryRecord();	//复选框选中,清除搜索历史纪录
	}());
	
	function searchKuangLisener() {
		$("[id!='result1']").on("click",function() {
			if ($("#result1").css("display") == "block") {
				$("#result1").css("display", "none");
			}
		});
	}
	function closeDrawObj(){
		console.log("给#mainTop, #main_left注册点击事件，触发时移出绘图状态");
		$("#mainTop, #main_left").click(function() {// 鼠标点击MAP左和上方时，关闭鼠标绘图对象
			console.log("鼠标点击MAP左和上方时，关闭鼠标绘图对象");
			closeTools(mouseTool);
			closeTools(mouseTool2);
		});
	}
	function keyDownSearch(){
		$("#searchKey").keydown(function(event) {
			if (event.keyCode == 13) {
				search();
			}
		});
	}
	function loadExistedData(){// 是否自动加载已有数据
    	$("#load").click(function() {
    		if ($("#load").prop("checked")) {
    			boundsChange();
    		} 
    	});
    	console.log("给#load注册点击事件，是否自动加载已有数据");
	}
    function clearHistoryRecord(){// 是否清除查询记录
    	$("#clear").click(function() {
    		if ($("#clear").prop("checked")) {// 复选框选中,清除搜索历史纪录
    			var qqLength = qqPolygonArr.length;
    			var bdLength = bdPolygonArr.length;
    			console.log("在地图上清空百度、qq的参考polygon，qq的数量："+qqLength+",bd的数量："+bdLength);
    			var length = bdLength >= qqLength ? bdLength : qqLength;
    			for (var i = 0; i < length; i++) {
    				if(i < bdLength){
    					bdPolygonArr[i].setMap(null);
    				}
    				if(i < qqLength){
    					qqPolygonArr[i].setMap(null);
    				}
    			}
    			bdPolygonArr = [];
    			qqPolygonArr = [];
    		} 
    	});
    	console.log("给#clear注册点击事件，触发时复选框选中,清除搜索历史纪录");
    }
	
	// 视野改变事件
	function boundsChange() {
		var getBounds = function(){
			amplify.publish("boundsChange", mapObj.getBounds());
		}
		getBounds();
		AMap.event.addListener(mapObj, "moveend", getBounds);
	}
	    	
	// 获取地图的级别
	function getGdZoom() {
		var getZoom = function(){
			var gdZoom = mapObj.getZoom();
			$("#gdMapZoom").html("当前级别：" + gdZoom);
		}
		getZoom();
		AMap.event.addListener(mapObj, "zoomchange", getZoom);
	}	
	
	
	
	// 点击属性编辑按钮，窗口展开和关闭动作
	function attributePanel() {
		var mainRight = $("#main_right");
		mainRight.toggle();
		if (mainRight.is(":visible")) {
			$(".MC006").css("background-position", "0 -19px");
		} else {
			$(".MC006").css("background-position", "0 0");
		}
	}

	// 地图操作工具条插件
	function AmaptoolPlugin() {
		mapObj.plugin([ "AMap.ToolBar", "AMap.OverView", "AMap.Scale",
				"AMap.MapType", "AMap.Autocomplete" ], function() {
			mapObj.addControl(new AMap.ToolBar(new AMap.Pixel(10,30)));	// 加载工具条
			mapObj.addControl(new AMap.OverView());						// 加载鹰眼
			mapObj.addControl(new AMap.Scale());						// 加载比例尺
			var mapType = new AMap.MapType({							// 地图类型切换
				defaultType : 0,
				showRoad : true
			});
			mapObj.addControl(mapType);
			// 加载输入提示插件
			if (navigator.userAgent.indexOf("MSIE") > 0) {// 判断是否IE浏览器
				document.getElementById("searchKey").onpropertychange = autoSearch;
			} else {
				document.getElementById("searchKey").oninput = autoSearch;
			}
		});
	
	}
	
	// 从输入提示框中选择关键字并查询
	function selectResult(index) {
		if (navigator.userAgent.indexOf("MSIE") > 0) {
			document.getElementById("searchKey").onpropertychange = null;
			document.getElementById("searchKey").onfocus = focus_callback;
		}
		var helptext = document.getElementById("divid" + (index + 1)).innerHTML
				.match(/>(.*)</g);
		// 截取输入提示的关键字部分
		var text = document.getElementById("divid" + (index + 1)).innerHTML
				.replace(/<[^>].*?>.*<\/[^>].*?>/g, "");
		document.getElementById("searchKey").value = text;
		document.getElementById("result1").style.display = "none";
		// 根据选择的输入提示关键字查询
		if (helptext != null && typeof helptext != 'undefined') {
			helptext = String(helptext);
			search(helptext.substring(1, helptext.length - 1));
		} else {
			search();
		}
	}
	
	this.search = function(helptext) {
		var text = $.trim($("#searchKey").val());
		console.log("amap API搜索poi:        "+helptext +","+text);
		if (text === '') {
			return;
		}
		$("#result1").css("display","none");
        var helpTmp = typeof helptext == 'undefined' ? "" : helptext;
		keys.city = helpTmp;
		keys.searchKey = text;
		mapObj.clearMap();	
		 //关键字查询
		msearch.search(helpTmp + text, function(status, result){
        	if(status === 'complete' && result.info === 'OK'){
        		placeSearch_CallBack(result);
        	}else{
        		alert("搜索出错。。。。");
        	}
        }); 
	}
	
	// 输入提示
	function autoSearch() {
		var keywords = document.getElementById("searchKey").value;
		if ($.trim(keywords) === '') {
			return;
		}
		var autoOptions = {
			pageIndex : 1,
			pageSize : 10,
			city : "" // 城市，默认全国
		};
		var auto = new AMap.Autocomplete(autoOptions);
		// 查询成功时返回查询结果
		auto.search(keywords, function(status, result){
        	if(status === 'complete' && result.info === 'OK'){
        		autocomplete_CallBack(result);
        	}else{
        		$("#result1").css("display", "none");
        	}
        });
	}
	
	// 输出输入提示结果的回调函数
	function autocomplete_CallBack(data) {
		var resultStr = "";
		var tipArr = data.tips;
		if (tipArr.length > 0) {
			for ( var i = 0; i < tipArr.length; i++) {
				resultStr += "<div id='divid"+ (i + 1)+"'>"
						+ tipArr[i].name 
						+ "<span>" + tipArr[i].district + "</span></div>";
			}
		} else {
			resultStr = " π__π 亲,人家找不到结果!<br />要不试试：<br />1.请确保所有字词拼写正确<br />2.尝试不同的关键字<br />3.尝试更宽泛的关键字";
		}
		document.getElementById("result1").innerHTML = resultStr;
		$("#result1").css("display","block");
	}
	
	// 鼠标滑过右侧POI查询结果改变背景样式
	openMarkerTipById1 = function(thiss) {
		thiss.style.background = '#CAE1FF';
		for(var i = 0; i < markers.length; i++) {
			if (i == $(thiss).index()) {
				markers[i].setIcon(path+'/images/'+ (i+1) + '.png');
				markers[i].setOffset(new AMap.Pixel(-10, -35));
			}else {
				markers[i].setIcon(path+'/images/marker_red.png');
				markers[i].setOffset(new AMap.Pixel(-6, -8));
			}
			
		}
		
	}
	
	// 输入提示框鼠标移出时的样式
	this.onmouseout_MarkerStyle = function(thiss) { // 鼠标移开后点样式恢复
		thiss.style.background = "";
		for(var i = 0; i < markers.length; i++) {
			markers[i].setIcon(path+'/images/marker_red.png');
			markers[i].setOffset(new AMap.Pixel(-6, -8));
		}
	}
		
	// 定位选择输入提示关键字
	function focus_callback() {
		if (navigator.userAgent.indexOf("MSIE") > 0) {
			document.getElementById("searchKey").onpropertychange = autoSearch;
		}
	}
	
	// 添加查询结果的marker&infowindow
	function addmarker(i, d) {
		var lngX = d.location.getLng();
		var latY = d.location.getLat();
		var markerOption = {
			map : mapObj,
			offset : new AMap.Pixel(-6,-8),
			icon : path + "/images/marker_red.png",
			position : new AMap.LngLat(lngX, latY)
		};
		var mar = new AMap.Marker(markerOption);
		markers.push(mar);
	}
	function addSearchResultEve(){
		$("#result").on("mouseover mouseout","div",function(event){
			event.type === "mouseover" ?
			openMarkerTipById1(this): onmouseout_MarkerStyle(this);
		});
		$("#result1").on("mouseover mouseout click","div",function(event){
			event.type === "click" ? selectResult($(this).index()):
			event.type === "mouseover" ?
					this.style.background = '#CAE1FF' :this.style.background = "";
		});
	}
	
	
	// 输出关键字查询结果的回调函数
	function placeSearch_CallBack(data) {
		markers = [];
		$('#meshbox').attr("checked",false);
		var poiArr = data.poiList.pois;
		var resultCount = poiArr.length;
		console.log("api查询成功后的poi数量:"+resultCount);
		if( resultCount <= 0 ){return;}
		var resultStr1 = "";
		for ( var i = 0; i < resultCount; i++) {
			resultStr1 += "<div id='divid"+ (i + 1)+"'><table><tr>" 
					+ "<td><img src='"+ path+ "/images/"+ (i + 1)+ ".png'></td>"
					+ "<td width=200px>" +
						  "<h3><font><span id='Name'>"+ poiArr[i].name + "</span></font></h3>"
					+ "</td></tr></table></div>";
			addmarker(i, poiArr[i]);
		}
		var lng = $("#lng"+taskitemid).text();
		var lat = $("#lat"+taskitemid).text();
		console.log("poi的坐标点："+lng+","+lat);
		mapObj.setCenter(lng === "" || lat === "" ? poiArr[0].location :new AMap.LngLat(lng,lat));
		$("#result").height($("html").height() - 364);
		$("#result").html(resultStr1);
		console.log("发布“poi.auto.finish”，抓取百度和qq的polygon作为参考，抓取amap的poi进行关联");
		amplify.publish("poi.auto.finish", keys);
	}
	

	// 创建地点搜索服务实例
	function ASearchObj() {
		AMap.service(["AMap.PlaceSearch"], function() {       
			msearch = new AMap.PlaceSearch({ // 构造地点查询类
				city : "全国", // 城市 _默认全国
				pageSize : 10, // 每页结果数,默认10
				pageIndex : 1, // 请求页码，默认1
			});
	    });
		console.log("amap search has inited... ...");
	}
	
	// 初始化地图
	function mapInit( callback ) {
		var opt = {
				resizeEnable : true,
				dragEnable:true,
				rotateEnable:true,
				zoomEnable:true,
				view: new AMap.View2D({	
					zoom : 15, // 设置地图缩放级别
					center : new AMap.LngLat(116.397428, 39.90923)// 设置地图中心点
				})	
		}; 
		var mapObj = new AMap.Map("map", opt);
		window.mapObj = mapObj;
		AmaptoolPlugin(); 			// 加载工具插件
		ASearchObj();				//初始化搜索
		getGdZoom();				//增加缩放级别事件
		boundsChange();				//视野范围变化后的的事件
		if(callback) {
			callback();
		}
		console.log("初始化高德地图并显示");
	}
	
	return {
		search: search,
		mapInit: mapInit
	}
});


