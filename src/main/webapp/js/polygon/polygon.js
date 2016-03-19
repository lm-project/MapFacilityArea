/**
 * 多边形的增加和删除
 */
var editorTool = null; 			 // polygon编辑工具
define(function(){
	var Taskitem,polygons,createPoint,Role,RemoveConvers;
	var drawPolygonObj = null;
	//港澳地区图幅号列表
	var hk_mc_area =  new Array('F49F019048','F49F020048','F49F021047','F49F021048','F49F022046','F49F022047','F49F022048','F49F023046',
			'F49F023047','F49F023048','F50F018002','F50F018003','F50F018004','F50F019001','F50F019002','F50F019003','F50F019004',
			'F50F020001','F50F020002','F50F020003','F50F020004','F50F021001','F50F021002','F50F021003','F50F021004','F50F022001',
			'F50F022002','F50F022003','F50F022004','F50F023001','F50F023002','F50F023003','F50F023004','F49F019049','F49F022045','F49F023045');
	//设置画面时多边形的属性  
	var polygonOption = {  
		strokeColor:"#FF33FF", 
		zIndex: 1,
		strokeOpacity:1,  
		strokeWeight:2 };
	
	function addDrawPolygonEvent(){
		$("#playploygon").on("click",function() {
			mapObj.setDefaultCursor("crosshair");
			closeTools(mouseTool);
			closeTools(mouseTool2);
		    mapObj.plugin(["AMap.MouseTool"],function(){   //在地图中添加MouseTool插件  
		        mouseTool = new AMap.MouseTool(mapObj); 
				mouseTool.polygon(polygonOption);   //使用鼠标工具绘制多边形
		        AMap.event.addListener(mouseTool, "draw", function(e){ //鼠标工具绘制覆盖物结束时触发此事件 
		        	if(!Role.isQa){
		        		addEditPolygon( e.obj );
		    		}else{
		    			RemoveConvers.removeConvers();
		    			drawPolygonObj = e.obj;
		    		}
		        	
		        }); 
		    });
		});
	}
	
	
	function editPolygon(p,hrefObj) {
		closeTools(editorTool);
	    if($(hrefObj).val() == "编辑顶点") {
		    mapObj.plugin(["AMap.PolyEditor"], function() {// 折线、多边形编辑插件
	            if( !editorTool ) {
			        editorTool = new AMap.PolyEditor(mapObj, polygonEdit);
	            }
	            editorTool.open();
		    });
	        $(hrefObj).val("结束编辑顶点");
	    } else {
	        $(hrefObj).val("编辑顶点");
	    }
	}
	
	function deleteEditPolygon(p, thiss) {//删除绘制的polygon
		$(thiss).next().remove();
		$(thiss).remove();
	    $('#polygon').val('');
		$("#tname"+taskitemid+" input[id=editPoint]").remove();
		if(polygonEdit !== null) {
		    polygonEdit.setMap(null);
		    polygonEdit = null;
	    }
		closeTools(editorTool);
		closeTools(mouseTool);
	    amplify.publish("deletePolygon");
	}
	
	function addEditPointEvent(){
		$('#main_left').on("click","[id=editPoint]",function(){
			closeTools(mouseTool);
			mapObj.setDefaultCursor("default");
	        editPolygon(taskitemid,this);
		});
	}
	
	function addDeletePolygonEvent(){
		$('#main_left').on("click","[name=deletePolygon]",function(){
			deleteEditPolygon(taskitemid, this);
		});
	}
	
	
	function setExtPolygonData(drawObj){
		var arr = drawObj.getPath().toString().split(","); 
		var lngs = 0;
		var lats = 0;
		for (var i = 0; i < arr.length; i=i+2) {
			lngs += parseFloat(arr[i]);
			lats += parseFloat(arr[i+1]);
		};
		//设置用户自定义属性，支持JavaScript API任意数据类型，如Polygon的id等 
		drawObj.setExtData(new AMap.LngLat((lngs*2/arr.length).toFixed(6), (lats*2/arr.length).toFixed(6)));
	}
	
	function addEditDeleteBnt( path ){
		$("#tname"+taskitemid+" input[id=editPoint]").remove();
		$("#href"+taskitemid).remove();
		if("qa" != sessionStorage.getItem("role")){
			$("#tname"+taskitemid).append(" <input type='button' class='a orange small floatr' id='editPoint'  value='编辑顶点'>");
			$("#city"+taskitemid).append("<a class='a orange small floatr' id='href"+taskitemid+"' name='deletePolygon' >删除</a>");
			getArea_flag(taskitemid,path);
		}
	}
	
	function addShowPolygonEvent(){
		$("#li"+taskitemid).parent("ul").on("mouseover", function(){
			onmouseover_Polygon(taskitemid,this);
		}).on("mouseout", function(){
			onmouseout=onmouseout_Polygon(taskitemid,this);
		});
	}
	
	function addEditPolygon(drawObj) {
		if(polygonEdit) {
		    polygonEdit.setMap(null);
		    polygonEdit = null;
		    $('#polygon').val('');
		}
		closeTools(editorTool);
		closeTools(mouseTool);
		if(typeof drawObj==="string"){
			polygonEdit = new AMap.Polygon({
			    map: mapObj,
	            path: createPoint(drawObj),
			    strokeColor: "blue",
			    strokeOpacity: 0.9,
			    strokeWeight: 3,
			    fillColor: "#f5deb3",
			    fillOpacity: 0.1,
			    zIndex: 1
		    });
		}else{
			polygonEdit = drawObj;
		}
	    setExtPolygonData(polygonEdit);
		addEditDeleteBnt(polygonEdit.toString);
		$('#polygon').val(polygonEdit.toString());
//		addShowPolygonEvent();
	}
	
	/**
	 * 根据mesh,polygon判断area_flag
	 */
	function getArea_flag( taskitemid, pointString ){
		var mesh = Taskitem.getMesh( taskitemid );
		console.log("mesh:"+mesh+"____pointString:"+pointString);
		if($.inArray(mesh, hk_mc_area) > -1){//判断当前mesh是否为港澳地区mesh
			console.log("___________mesh："+mesh+" in hk&mc mesh area!")
			$.ajax({//ajax请求查询polygon所属区域
				type : "GET",
				url : path + '/common/area_flag',
				data : "pointString="+pointString,
				success : function(data){
					if(data.code == 100){
						$("select#AREA_FLAG").val(data.restring);
					}
				}
			});
		}
		$("select#AREA_FLAG").val(0);
	}

	function onmouseover_Polygon(p, thiss, lat, lng){
	    if( ! polygons.get(p)){
	        return;
	    }
		polygons.get(p).setOptions({ fillColor:"#ee2200" });
		console.log(polygons.get(p).getExtData());
//		if(!mapObj.getBounds().contains(polygons.get(p).getExtData())){//指定点坐标是否在矩形范围内
//			mapObj.setCenter(polygons.get(p).getExtData());
//		}
	}
	
	function onmouseout_Polygon(p, thiss){
	    if( ! polygons.get(p)){
	        return;
	    }
		polygons.get(p).setOptions({ fillColor:"#1791fc" });
	}
	
	
	function init(taskitem,PolygonList,PointPath,role,removeConvers){
		Taskitem = taskitem,polygons = PolygonList,createPoint = PointPath,
		Role=role;
		addDrawPolygonEvent();		//绑定点击绘图工具按钮事件
		if(!Role.isQa){
			addEditPointEvent();		//增加编辑顶点事件
			addDeletePolygonEvent();	//增加删除绘制的polygon事件
		}else{
			RemoveConvers = removeConvers
		}
	}
	/*******************************************QA*******************************************************/
	function getPolygonPosition(){
		if(drawPolygonObj !== null) {
			return drawPolygonObj.getPath();
	    }
		return null;
	}
	
	function removePolygon(){
		if(drawPolygonObj !== null) {
			drawPolygonObj.setMap(null);
			drawPolygonObj = null;
	    }
		return null;
	}
	return {
		init:init,
		addEditPolygon: addEditPolygon,
		editPolygon:editPolygon,
		deleteEditPolygon:deleteEditPolygon,
		removePolygon:removePolygon,
		polygon:getPolygonPosition,
	}
});

