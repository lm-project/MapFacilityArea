/**
 * 导出shape数据
 */
define(['project/showProjects','task/showTasks'], function( Project, Task ){
	
	$('a.shapeHref').on("click" ,function() {
		_processProDom = processDomElement;
		var main = 	
	        '<div id="showProject" class="module_content" >' 
	       + '</div>';
		$('#main').html(main);
		$("#taskTemplate").load(path+"/js/template/allProject.htm",function(){
			Project.getProject();
		});
    });
	
	//处理dom元素
	function processDomElement(){
		$('[id=prc_commit]').remove();
		$('[id=prc_detail]').remove();
		$("#exportShpByPro").css("display","block");
		
		$("[id='prc_edit']").on("click" , function(e) {
			var projectname =  $($($( this ).parent().parent()).children()[1]).text();
			var main = 	
		        '<div id="task_tab" class="module_content" >' 
		       + '</div>';
			$('#main').html(main);
			$("#taskTemplate").load(path+"/js/template/tasks.htm",function(){
				_processTaskDom = processTaskExportDom;
				Task.getTaskList(1, projectname);
			});
		});
		
		
		$("#exportShpByPro").on("click" , function(e) {
			exportByPro();
    	});
		
	};
	
	function processTaskExportDom(data){
		$('#task_delall').remove();
		$('[id=task_recycle]').remove();
		$('[id=task_edit]').remove();
		$("#exportShpByTask").css("display","block");
		$("#exportShpByTask").on("click" , function(e) {
			exportByTask();
    	});
	}
	
	//按项目导出
	function exportByPro() {
		var projectname = '';
	    var count = 0;
		$('#prc_tab input[type=checkbox]:checked').each(function(){
			projectname = $(this).val();    
	        count ++;
		});    

	    if ( ! projectname ){
	        alert("请选择项目.");
	        return;
	    } 
	    if ( count > 1 ) {
	        alert("只能选择一个项目");
	        return;
	    }
	    
	    exportShape(projectname,'project');
	}
	
	//按任务导出
	function exportByTask() {
		var taskId = '';
		
		$('#task_tab input[type=checkbox]:checked').each(function(){
			taskId += $(this).val() + "&";    
		});    
		
		if ( ! taskId ){
			alert("请选择任务.");
			return;
		}
		exportShape(taskId.substring(0,taskId.length-1), 'task');
	}
	
	//导出请求
	function exportShape(param, type){
		
		//ajax导出请求
		$.ajax({
			type : "POST",
			url : path+"/facility/exportShape",
			data : "param=" + param + "&type="+type,
			success : function(data) {
				if ( data.code == 100 ){
					console.log("点击下载"+data.restring);
					$('span.mg').html(data.description + "点击下载：<a href='" + data.restring+"' target='_blank'>" +  data.restring +"</a>");
				} else {
					$('span.mg').html(data.description + data.restring);
				}
			}
		});
		//ajax end
	};
	
});
