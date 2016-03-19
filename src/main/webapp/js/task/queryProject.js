define(function() {
	function gotoEdit(){
        var taids = [];//获取页面上已选择任务
        $('#tab3 input[type=checkbox]:checked').each(function(){
        	taids.push( $.trim($(this).val()) );  
    	});
        if(taids!=null && taids!=""){
        	sessionStorage.setItem("taskids", taids);
        	sessionStorage.setItem("role", 'operator');
        	window.location.href =path + '/operator/art';
        } else {
        	alert("您还未选取任务！");
        }
	};
	
	function lookTask(){
		$('#current').text("查询任务");
		$('#article1').css("display","block");
		$('#article2').css("display","none");
		$('#shape').css("display","none");
	}
	function lookReceviedTask(){
		console.log("------------------------- lllllll");
		$('#current').text("已领取任务");
		$('#article1').css("display","none");
		$('#article2').css("display","block");
		$('#shape').css("display","none");
		$("#unfinished").attr("checked","true");
		getRecevie(1, '#tab3');
	}
	function showTasks(data, htmlEle, status){
		$("#taskTemplate").load(path+"/js/template/received.htm",function(){
    		var scriptTemplate = Handlebars.compile($('#taskTemplate').text());
    		var wrapper = { items: eval(data.restring) ,path:path };
    		var rs = scriptTemplate( wrapper );
    		$(htmlEle).html(rs);
    		if(status == 0) {
    			$('[id=op_commit]').remove();
    			$('[id=selectalltask]').remove();
    			$("#number").html(data.description);
    		}
    	});
	}
	
	function qeuryTask( page, projectname, s_time, e_time, htmlEle, status){
		$.ajax({
			type : "POST",
//			url : path+"/operator/getrecevie/",
			url : path +"/getTask/",
			data : "page=" + page +"&projectname= "+projectname +"&status= "+ status+ "&s_time= "+ s_time +"&e_time= "+ e_time,
			success : function(data) {
				if(data.code == '100') {
					console.log(data.description);
					console.log(data.restring);
					if(data.restring == 'true'){
						$("#number").html(0);
						$(htmlEle).html("");
					}else {
						showTasks(data, htmlEle, status);
					} 
				}
			}
		});	
	}

	
	function getRecevie (status, htmlTab){
		var page = 1;
		var projectname = $('#project').val()== 0 ? "":$('#project').val(); 
		var s_tiem = $.trim($('#time1').val());
		var e_tiem = $.trim($('#time2').val());
		if((s_tiem && !e_tiem)||(!s_tiem && e_tiem)){
			alert("时间没有填入！");
			return;
		}else if(s_tiem && e_tiem){
			s_time = s_tiem+" 00:00:00";
			e_time = e_tiem+" 24:00:00";
		}else{
			s_time = e_time = "";
		}
		qeuryTask( page, projectname, s_time, e_time, htmlTab, status);

	};
	function commitTaskByTaskId(projectname) {
		$.ajax({
			type : "POST",
			url : path + '/admin/project/commit',
			data : "projectname=" + projectname,
			success : function(data) {
				alert(data.description + data.restring);
			}
		});

	};
	
	function commitOPTask(_this){
		$.ajax({
			type : "POST",
			url : path + '/op/task/commit',
			data : "taskid=" + $(_this).val(),
			success : function(data) {
				alert(data.restring  + data.description );
			}
		});
//		Project.commitPro(projectname);
	}
	
	(function() {
		$('#query').click(function() {//查询未领取的任务包
			getRecevie(0, '#tab2');
		});
		$('#gotoEdit').click(function(){//点击开始作业事件
			gotoEdit();
		});
		$("#lookTask").on("click",function(){ //查看未领取任务
			lookTask();
		});
		$("#lookReceviedTask").on("click",function(){ //查看已领取任务
			lookReceviedTask();
		});
		$("#tab3").on("click"," #op_commit", function(){ //提交作业任务包
			commitOPTask(this);
		});
	})();
});
