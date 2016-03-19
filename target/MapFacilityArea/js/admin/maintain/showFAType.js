/**
 * 显示、查询fatype
 */
define(['maintain/updateFAType'], function(Fatpye){
	
	$('a.updateFAType').on('click',function(){
		$("#main").load(path+"/js/template/updateFAType.htm", function(){
			Fatpye.initFatypeEvent();
			getFacategory();
			getFatype();
			getPoitype();
			initQueryEvent();
		});
	});
	
	function initQueryEvent(){
		$("#query_fatype").on("click", function(e){
			Fatpye.getFapoiRelation();
		});
		
		$("#add_fatype").on("click", function(e){
			Fatpye.addFapoiRelation();
		});
		
		$("#facategory").change(function(){
			getFatype();
		});
		
		$("#fatype").change(function(){
			getPoitype();
		});
	}
	
	//获取设施区域大类列表
	function getFacategory(){
		$.ajax({
			type : "POST",
			url : path+"/common/getFacategory",
			success : function(data) {
				if(data.code == 100) {
					var tl=eval(data.restring); 
					var objDom = document.getElementById("facategory");
					objDom.options.length=1; 
	    		    for(var i=0; i<tl.length; i++){
	    				objDom.options.add(new Option(tl[i], tl[i]));
	    			}// end for
				}// end if
			}
		});
	}
	
	//获取设施区域中类列表
	function getFatype(){
		var fa_category;
		if (typeof fa_category == 'undefined') {
			fa_category = $("#facategory").val()== 0 ? "":$('#facategory').val();
		}
		$.ajax({
			type : "POST",
			url : path+"/common/getFatypeList/",
			data : "fa_category="+fa_category,
			success : function(data) {
				if(data.code == 100) {
					var tl=eval(data.restring); 
					var objDom = document.getElementById("fatype");
					objDom.options.length=1; 
	    		    for(var i=0; i<tl.length; i++){
	    				objDom.options.add(new Option(tl[i], tl[i]));
	    			}// end for
				}// end if
			}
		});
	}
	
	//获取poi类型列表
	function getPoitype(){
		var fa_type;
		if (typeof fa_type == 'undefined') {
			fa_type = $("#fatype").val()== 0 ? "":$('#fatype').val();
		}
		$.ajax({
			type : "POST",
			url : path+"/common/getPoitypeList/",
			data : "fa_type="+fa_type,
			success : function(data) {
				if(data.code == 100) {
					var tl=eval(data.restring); 
					var objDom = document.getElementById("poitype");
					objDom.options.length=1; 
	    		    for(var i=0; i<tl.length; i++){
	    				objDom.options.add(new Option(tl[i], tl[i]));
	    			}// end for
				}// end if
			}
		});
	}
	
	return;
});
