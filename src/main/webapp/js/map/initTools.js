
define(function($) {
	console.log("加载map/initTool,定义了三个全局变量：searchTaskitemTap = 0; //默认为未完成任务页， completedTaskitems = []; //标记已完成作业项   var uncompletedTaskitems =  []; // 标记未完成作业项");
	console.log("执行Main给#map_container_tools中的元素添加一些事件：");
	var isRefermap = false;		// 标记是否叠加参考图层
	var isFullScreen = 0; 		// 标记是否全屏 1:全屏，0：不全屏
	var rectOptions = {			// 设置放大缩小时的拉动框的样式
		strokeStyle : "solid",
		strokeColor : "#FF33FF",
		strokeOpacity : 1,
		strokeWeight : 2,
		fillOpacity : 0.01
	};
	function changeMapLayer(obj){
		var referenceId = obj.id;
		console.log("点击的参考地图："+referenceId);
		var referenMap = referenceId === "googleTileLayer"? 'map.google' : 
						 referenceId === "baiduTileLayer" ? 'map.baidu' :
				         referenceId === "qqTileLayer"    ? 'map.qq' :
					     referenceId === "imgTileLayer"   ? 'map.img' : 'map.remove';
		amplify.publish(referenMap, {});
		if(referenMap === 'map.remove'){
			$('#displaynoneTileLayer').removeClass('display').addClass('undisplay');
			isRefermap=false;
			return;
		}
		isRefermap = true;
		$('#map_tools_container div').removeClass('toolsclick');
		$(obj).addClass('toolsclick');
		$('#displaynoneTileLayer').removeClass('undisplay').addClass('display');
	}
	function initreferncemap() {
		$('#map_tools_container .tools_container_item12').on('click',function() {						
			changeMapLayer(this);
		});

		$('#displayTileLayer').mouseover(function() {
			$('#map_tools_container').addClass('display');
		})
		
		$('#map_tools_container').mouseout(function() {
			if (!isRefermap) {
				$('#map_tools_container').removeClass('display');
			}
		});
	}


	function initArtTools() {
		$("#tools").mouseover(function() {
			$("#tools_container").css("display", "block");
		}).mouseout(function() {
			$("#tools_container").css("display", "none");
		});
		
		$("#artboard_tools").mouseover(function() {
			$("#artboard_tools_container").css("display", "block");
		}).mouseout(function() {
			$("#artboard_tools_container").css("display", "none");
		});
		
		
	}
	
	function enlarge(rectOptions) {// 拉框放大动作
		$(".MC002").css("background-position", "0px -19px");
		$(".MC001,.MC003").css("background-position", "0px 0px");
		mouseTool2.rectZoomIn(rectOptions); // 通过rectOptions更改拉框放大时鼠标绘制的矩形框样式
	}
	
	function ensmall(rectOptions){// 拉框缩小动作
		$(".MC003").css("background-position", "0px -19px");
		$(".MC001,.MC002").css("background-position", "0px 0px");
		mouseTool2.rectZoomOut(rectOptions); // 通过rectOptions更改拉框放大时鼠标绘制的矩形框样式
	}
	
	function moveMap(){// 移动动作
		$(".MC001").css("background-position", "0px -19px");
		$(".MC002,.MC003").css("background-position", "0px 0px");
		mapObj.setDefaultCursor("move");
	}
	
	function initContrlTools () {
		$('#enlarge,#ensmall,#move').click(function() {
			closeTools(mouseTool);
			closeTools(mouseTool2);
			var toolId = this.id;
			if(toolId==="move"){
				moveMap();
				return;
			}
			mapObj.plugin([ "AMap.MouseTool" ], function() {
				mouseTool2 = new AMap.MouseTool(mapObj);
			});
			mapObj.setDefaultCursor("crosshair");
			toolId==="enlarge" ? enlarge(rectOptions) : ensmall(rectOptions);
		});	
	}
	 
	function fullScreen(pixel){
		isFullScreen = 1;
		mapObj.containTolnglat(new AMap.Pixel(pixel.getX() - 80, pixel.getY() - 40));
		$("#mainTop, #main_left").css("display", "none");
		document.getElementById('map_container').style.marginLeft = "0";
		$("#mapFullScreen span").removeClass("not_display");
		$('#map').css("height" ,document.documentElement.clientHeight + "px");
	}
	
	var exitFullScreen = function(pixel){
		isFullScreen = 0;
		mapObj.containTolnglat(new AMap.Pixel(pixel.getX() + 80, pixel.getY() + 40));
		$("#mainTop, #main_left").css("display","block");
		document.getElementById('map_container').style.marginLeft = "330px";
		$("#mapFullScreen span").addClass("not_display");
	}
	
	function initFullScreen() {// 全屏动作事件
		$("#mapFullScreen").click(function() {
			var pixel = mapObj.lnglatTocontainer(mapObj.getCenter());
			if (isFullScreen == 0) {
				fullScreen(pixel);
			} else {
				exitFullScreen(pixel);
			}
		});

		$(document).keydown(function(event) {// 监听键盘Esc退出全屏事件
			var keynum = window.event ?  event.keyCode : 
						 event.which ? event.which : 0;
			if (keynum == 27 && isFullScreen == 1) {
				exitFullScreen(mapObj.lnglatTocontainer(mapObj.getCenter()));
			}
		});
	}
	
	function initsearchTaskitem() {
		console.log("搜索任务项，并且发布“search.task.item”");
		var searchTaskItem = function(){
			var arrays = searchTaskitemTap == 0 ? uncompletedTaskitems: completedTaskitems;
		 	amplify.publish('search.task.item',{array:arrays,type:searchTaskitemTap,keyword:$('#searchTaskitem').val()});
		}
		// 加载输入提示插件
		if (navigator.userAgent.indexOf("MSIE") > 0) {// 判断是否IE浏览器
			document.getElementById("searchTaskitem").onpropertychange = searchTaskItem;
		} else {
			document.getElementById("searchTaskitem").oninput = searchTaskItem;
		}
	}
	
	this.closeTools = function(e) {
		console.log("---关闭绘图工具------");
		if (typeof e != 'undefined' && e != null) {
			e.close();
			mapObj.setDefaultCursor("default");
			e = null;
		}
	}
	
	function initMeshCheckbox() {
		console.log("点击#meshbox，发布“mesh.loaded”")
		$('#meshbox').live('click', function(){
			if(!$(this).is(':checked')) {
				amplify.publish( "mesh.loaded", {});
			}else {
				if(meshBoxPolygon !=null){
					meshBoxPolygon.setMap(null);
				}
			}
		})
	}
	
	var Main = function() {
		initArtTools(); 		// 初始化绘图控件（视野内搜索、测距、标记）
		initContrlTools(); 		// 初始化控图控件（放大、缩小、移动）
		initreferncemap(); 		// 初始化参考地图控件
		initFullScreen(); 		// 初始化全屏和退出全屏事件
		initsearchTaskitem(); 	//注册搜索作业项
		initMeshCheckbox();		// ?
	}
	return new Main();
}(jQuery));
