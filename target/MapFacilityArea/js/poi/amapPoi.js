/**
 * Amap POI的查询
 */
define(['../msg/msg',"../common/pointPath"], function(Msg,createPoint) {
	console.log("加载poi/amapPoi返回并执行initEvent：主要是渲染编辑设施区域属性栏");
	var facilityCache = {};
	var Map, Role, Polygon;
	
	function taskBeginEdit(){
		amplify.subscribe("task.begin.edit",function( data ) {
        	console.log("改变地图大小，显示右侧栏，渲染右侧poiform，如果融achieve表查到此任务项，将polygon渲染到地图");
            $("#main_right").css("display", "block");
            $("#map").css('margin-right','250px');
            loadFacilityArchive( data.taskitemid );//渲染右侧poiform，如果融achieve表查到此任务项，将polygon渲染到地图
        });
	}
	
	function poiAutoFinish(){
		amplify.subscribe("poi.auto.finish",function( data ) {
			if(! Role.isQa){
				 searchFacility( data.searchKey, data.city );
		          console.log("订阅“poi.auto.finish”，抓取AMapPOI关联"+data.searchKey+","+data.city);
			}else{
				 amplify.publish('facility.search.finish',{});
			}
	   });
	}
	
	function taskEndEdit(){
		 amplify.subscribe("task.end.edit",function( taskitemid ) {
	            $("#main_right").css("display", "none");
	            $("#map").css('margin-right','0px');
	        });
	}
	
    var initEvent = function(map, role, polygon) {
    	Map = map, Role = role, Polygon = polygon;
    	Handlebars.registerHelper('ifCond', function(v1, v2, options) {
    		console.log("Handlebars.registerHelper('ifCond')事件为:"+v1+","+v2);
    		  if(v1 === v2) {
    		    return options.fn(this);
    		  }
    		  return options.inverse(this);
    		});
    	taskBeginEdit();
    	poiAutoFinish();
    	taskEndEdit();
        console.log("initEvent facility");
    }
	
	function loadFacilityArchive( taskitemid ) {
		console.log("根据taskitemid区 achieve表查询此任务项的信息:"+taskitemid);
		$.ajax({
			type : "GET",
			url : path + '/common/taskitem/' + taskitemid,
			success : function(data) {
				console.log("查询返回任务项的信息："+data.restring);
				showEditPoiForm(data);
			}
		});
	}
	
	function showEditPoiForm( data ) {
		console.log("将查询到的数据渲染到右侧poiForm中");
		$("#taskTemplate").load(path+"/js/template/poiEditForm.htm?time=11", function(){
			console.log("加载template/poiEditForm.htm并赋值");
			//poi元数据备份
			poiOrg = getPoiOrgFromData( data );//poiForm对象值 
			renderEditForm( poiOrg );// 将渲染poiOrg到右侧编辑区域
			if ( Role.isQa ) {//删除 编辑多边形操作
        		$('#editPoint').remove();
        		$('#href' + taskitemid).remove();
        	} else {				
				removeQaDom();//删除 内检多余的dom元素
        	}
			if(poiOrg.polygon){
				console.log("************************************");
				Polygon.addEditPolygon(poiOrg.polygon);// 在地图上增加编辑多边形
			}
		});
	}
	function renderEditForm( data ) {
		var scriptTemplate = Handlebars.compile($('#taskTemplate').text());
        var rs = scriptTemplate( data );
    	$('#poiEditForm').html(rs);
	}
	
	function removeQaDom() {
		$('#qa_tabs').remove();
		$('#qa_tab_conbox').remove();
	}
	
	function getPoiOrgFromData( data ) {
		if ( data.code == 100 && data.restring != 'null'&& data.restring!="") {
			var obj =  JSON.parse(data.restring);
			var polgonStr = obj.lng_lat==null|| obj.lng_lat=="" ? geom : obj.lng_lat;
		    return {name_chn: obj.name_chn ,mesh: obj.mesh ,poi_id: obj.poi_id, polygon: polgonStr
		    		,fa_type: obj.fa_type, fa_flag: obj.fa_flag, disp_class: obj.disp_class
		    		,area_flag: obj.area_flag,fa_id: obj.fa_id, fa: obj.fa, gid: obj.taskitem_id
		    		,precision: obj.precision, sources: obj.sources
		    		,proname: obj.proname, fatype_id: obj.fatype_id};
		    
		} else {
			return {name_chn:$('#NAME_CHN').val(), mesh: $('#MESH').val(), poi_id: '', polygon: '' 
					,fa_type: parseInt($("#typecode"+taskitemid).html()), fa_flag: 0, disp_class: 0, area_flag: 0
		    		,fa_id: '', fa: '', gid: '', precision:'A', sources: 0, proname: $("#taskname"+taskitemid).html(), fatype_id: ''};		
		}
	}
    
	function getPoiByRequestAmap(poiName){
		 $.ajax({
			    type: "POST", dataType: 'json',
			    url : path+"/poi/searcher",data: 'searchKey='+poiName,
			    error: function(XMLHttpRequest, error, errorThrown) {  
					console.error(error)}, 
			    success: function(data){
		            if(data.code != '100') {
	                    return ;}
	                facilityCache[poiName]= data;                
	                displayPois(data);
	                initPoi();
	                console.log("发布facility.search.finish，标记结束状态");
	                amplify.publish('facility.search.finish',{});}
		    });
	}
	
    var searchFacility = function (text,helptext){
        if  ( facilityCache[helptext+text] ) {
            displayPois( facilityCache[helptext+text]);
            initPoi();
            amplify.publish('facility.search.finish',{});
            return;
        }
        getPoiByRequestAmap(helptext+text);
	   
    };
    function addPoiInfo(i,poi_id,mesh,type,type_id){
    	console.log("给poi增加需要关联的信息：poi_id:"+poi_id+",mesh:"+mesh+"type:"+type+",type_id"+type_id);
    	var linkInfo = "<input type='button' class='button orange middel floatr' " +
						"style='cursor:pointer;width:56px' id='delp'"+(i+1)+" value='关联'/>";
    	var addInfo = "<div style='display:none' id='poi"+(i+1)+"'>" +
					"POI_ID：<span id='POI_ID"+(i+1)+"'>" + poi_id + "</span>" +
					"<br>MESH：<span id='Meshcode"+(i+1)+"'>"+ mesh + "</span><br>" +
					"FA_TYPE：<span id='FA_TYPE"+(i+1)+"'>"+ type + "</span><br>" +
					"TYPE_ID：<span id='TYPE_ID"+(i+1)+"'>"+ type_id + "</span></div>";
		$("#result #divid"+(i+1)+" td:last").prepend(linkInfo);
		$("#result #divid"+(i+1)+" #poi"+(i+1)).remove();
		$("#result #divid"+(i+1)+" td:last").append(addInfo);
    }
    var displayPois = function(data) {
        var center = false; 
        var pois = data.restring;
		var as = pois.split(",");
		console.log("抓取amap poi 的信息："+pois+"长度为："+as.length);
		for(var i = 0; i< as.length; i++) {
			if(!as[i]){
				continue;
			}
			var poi_id = getValue(as[i].split('_')[1], 1);
			var mesh = getValue(as[i].split('_')[0], 1);
			var type = getValue(as[i].split('_')[2]);
			var type_id = getValue(as[i].split('_')[3]);	
			if( type && ! Role.isQa) {
				addPoiInfo(i,poi_id,mesh,type,type_id);
			}
		}
    }

    var initPoi = function() {
    	console.log("给关联增加点击事件，并且实现关联功能");
    	var k = 0;
	    $("#result input").on("click", function(){
		    var div = $(this).parents("div").attr('id');
		    var currentId = div.match(/\d+/);
		    var img = $("#result #"+div+" img");
		    var isAttach = ($("#result #"+div+" input").val() == "已关联");
		    
		    if(k[0] == currentId[0] && isAttach) {
			    $("#result #divid"+k+" img").attr("src", path+"/images/"+k+".png");
			    $("#result #divid"+k+" input").val("关联");
			    $("#NAME_CHN").val(poiOrg.name_chn);//referance gettask.js : function:openedit()
			    $("#MESH").val(poiOrg.mesh);
			    $("input#POI_ID").val(poiOrg.poi_id);
			    $("input#FA_TYPE").val(poiOrg.fa_type);
			    $("input#TYPE_ID").val(poiOrg.fatype_id);
		    } else {
			    $("#result #divid"+k+" img").attr("src", path+"/images/"+k+".png");
			    $("#result #divid"+k+" input").val("关联");
			    $("#result #"+div+" input").val("已关联");
			    img.attr("src", path+"/images/poi.png");
			    k = div.match(/\d+/);
			    $("#NAME_CHN").val($(this).next().find('span#Name').html());
                $("#MESH").val($("#Meshcode"+k).html());
                $("input#POI_ID").val($("#POI_ID"+k).html());
                $("input#FA_TYPE").val($("#FA_TYPE"+k).html());
                $("input#TYPE_ID").val($("#TYPE_ID"+k).html());
		    }
	    });


    }

    var getValue = function(v, type) {
	    if(typeof v == 'undefined' || $.trim(v)=='') {
		    if(type == 1) {
			    return "未找到";
		    }
		    return '';
	    }else {
		    return v;
	    }
    }


    var check = function(e) {
	    var mesh = $('#MESH').val();
	    if($(e).prop("checked")){
		    if(mesh){
			    meshBox(mesh);
		    }
	    }
    }
    return {
    	init: initEvent
    }
});
