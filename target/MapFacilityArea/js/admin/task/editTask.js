/**
 * 任务的回收、删除 & 获取任务详细信息
 */

define(['taskitem/showTaskitems'], function( Taskitem ){
	
	//获取任务详细信息
	function editTask(id) {
		console.log('taskid = ' + id);
		Taskitem.initTaskitemEvent(id);
	};
	
	//回收任务
	function recycleTask(id) {
		if (confirm("是否回收选中任务?")) {
			// ajax begin
			$.ajax({
				type : "PUT",
				url : path + "/admin/taskitem/recycle/" + id,
				success : function(data) {
					if (data.code == '100') {
						alert(data.description);
					}
				}
			})
			//ajax end
		}
	};
	
	//删除任务
	function delTask(v, name){
		if(v.indexOf('/')){
		    v = v.substring(v.indexOf('/')+1);
	    }
	    if(v!='' && confirm("是否删除选中任务?")){
		    // ajax删除请求
		    $.ajax({
			    type : "DELETE",
			    url : path+"/admin/task/" +v,
			    success : function(data) {
				    if(data.code == '100') {
					    alert(data.description);
					    getTaskList(1,name);
				    }
			    }
		    });
		    //ajax end
	    }
	};
	
	return{
		editTask: editTask,
		recycleTask: recycleTask,
		delTask: delTask,
	}
});