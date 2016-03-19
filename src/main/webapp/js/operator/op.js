define(function() {

	function queryTaskByTaskName(taskids,Checkbox){
		
		$.ajax({
			type : "POST",
//			url : path+"/operator/receive",
			url : path+"/operator/receiveTask",
			data : "taskids="+taskids,
			success : function(data) {
				if(data.code == '100') {
					alert(data.description);
					for(i = 0; i< taskids.length; i++) {
						$("#tab2 input[value="+taskids[i]+"]:checked").parent().parent().remove();
					}
					$('span font').html($('span font').html()-taskids.length);
					if($("#selectall1").val()=="取消"){
						$("#selectall1").val("全选");
					}
				}
			}
		});
	}
	
	
	 function receive(Checkbox){
	    	var taskids = [];
	    	$('#tab2 input[type=checkbox]:checked').each(function(){
	    		var task_id = $.trim($(this).val());
	    		console.log("receive taskid is "+task_id);
	    		taskids.push(task_id); //获取任务id 
			});    
	    	if( taskids.length <= 0){
	    		alert("您还未选取任务！");
	    		return;
	    	}
	    	if(confirm("是否领取选中任务?")){
	    		queryTaskByTaskName(taskids,Checkbox);
			}
	    }
	
	function init(Checkbox){
		$("#receive").click(function() {     //增加领取任务事件
			receive(Checkbox);
		}); 
		$("#selectall1").click(function() {  //全选任务包按钮
			Checkbox.select(this, "#tab2");
		});
		$("#selectalltask").live("click", function() {//全选作业已领取按钮
			Checkbox.select(this, "#tab3");
		});
	};
	return init;
});

