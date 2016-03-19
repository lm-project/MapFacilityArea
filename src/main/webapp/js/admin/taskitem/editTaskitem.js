/**
 * 作业项的回收、删除
 */
define([], function(){
	
	//回收作业项
	function recycleTaskitem(id,taskId){
		if(confirm("是否回收选中任务项?")){
    		//ajax
    		$.ajax({
    			type : "PUT",
    			url : path+"/admin/taskitem/recycle/"+taskId+"/"+id,
    			success : function(data) {
    				if(data.code == '100') {
    					alert(data.description);
    					getTaskItemsList(1,taskId);
    				}
    			}
    		})
    		//ajax end
    	}
	};
	
	//删除作业项
	function delTaskitem(v, taskId){
		if(v.indexOf('/')){
		    v = v.substring(v.indexOf('/')+1);
	    }
	    if(v!='' && confirm("是否删除选中任务项?")){
    		// ajax删除请求
    		$.ajax({
    			type : "DELETE",
    			url : path+"/admin/taskitem/"+ v,
    			success : function(data) {
    				if(data.code == '100') {
    					alert(data.description);
    					getTaskItemsList(1,taskId);
    				}
    			}
    		});
    		//ajax end
    	}
	};
	
	return {
		recycleTaskitem: recycleTaskitem,
		delTaskitem: delTaskitem,
	}
	
});