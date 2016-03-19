var f = 0
var QureyProjects = (function($) {
	
	var getProject = function() {
		$('#query').click(function() {
			getRecevie(0, '#tab2');
		});
		
		$('#recevied').click(function() {
			recevied();
		});
		
		$('#gotoEdit').click(function(){
			gotoEdit();
		});
		
		
	}
	
	this.change = function(f){
	    	if( f ==0 ) {
	    		$('#current').text("查询任务");
	    		$('#article1').css("display","block");
	    		$('#article2').css("display","none");
	    		$('#shape').css("display","none");
	    	}else {
	    		$('#current').text("已领取任务");
	    		$('#article1').css("display","none");
	    		$('#article2').css("display","block");
	    		$('#shape').css("display","none");
	    		$("#unfinished").attr("checked","true");
	    		getRecevie(1, '#tab3');
	    	}
	    }
	
	function getRecevie (status, e){
		var page, projectname, taskname, processtype;
		
		if(typeof page == 'undefined'){
			page = 1;
		}
		if(typeof projectname == 'undefined'){
			projectname = $('#project').val()== 0 ? "":$('#project').val(); 
		}
		if(typeof taskname == 'undefined'){
			taskname = $('#tname').val();
		}
		if(typeof processtype == 'undefined'){
			processtype = $('#processtype').val()== 0 ? "":$('#processtype').val(); 
		}
		
		$.ajax({
			type : "POST",
			url : path+"/operator/getrecevie/",
			data : "page=" + page +"&projectname= "+projectname +"&taskname= "+ taskname +"&processtype= "+ processtype+"&status= "+ status,
			success : function(data) {
				if(data.code == '100') {
					if(data.restring == 'true'){
						$("#number").html(0);
						$(e).html("");
					}else {
						$("#taskTemplate").load(path+"/js/template/received.htm",function(){
				    		var scriptTemplate = Handlebars.compile($('#taskTemplate').text());
				    		var wrapper = { items: eval(data.restring) ,path:path };
				    		var rs = scriptTemplate( wrapper );
				    		$(e).html(rs);
				    		if(status == 0) {
				    			$('[id=selectalltask]').remove();
				    			$("#number").html(data.description);
				    		}else{
				    			
				    		}
				    		
				    	});//end load
					} // end if
			    	
				}// end if
			}
		});	
	};
	
	function gotoEdit(){
        var taids = [];//获取页面上已选择任务
        $('#tab3 input[type=checkbox]:checked').each(function(){
        	taids.push( $.trim($(this).val()) );  
    	});
        if(taids!=null && taids!=""){
        	//set sessionStorage
        	sessionStorage.setItem("taskids", taids);
        	sessionStorage.setItem("role", 'operator');
        	window.location.href =path + '/operator/art';
        } else {
        	alert("您还未选取任务！")
        }
	};
	
	return {
		　　getProject : getProject,
		};
		
}( jQuery ));
