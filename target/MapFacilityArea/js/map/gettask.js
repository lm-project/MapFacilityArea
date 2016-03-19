//全局变量 标记作业项
var taskitemid = -1;
var completedTaskitems = []; 	// 标记已完成作业项
var uncompletedTaskitems =  []; // 标记未完成作业项
define(['../msg/msg'], function( Msg ) {
	console.log("加载map/gettask,定义全局变量taskitemid = -1返回去getTaskItems并且立即执行，见查询到的任务项渲染");
	var role ; //角色
	function getTaskItems(){
		role = sessionStorage.getItem("role");
		initSubscribe();
		$.ajax({
			type : "GET",
			url : path+"/"+role+"/taskitems/received/"+sessionStorage.getItem("taskids"),
			success : function(data) {
				process( data );
			}
		});
		console.log('get tasks init ... ...');
	}
	
	function process( data ) {
		var taskitems =  eval(data);
		if( taskitems.length <= 0 ) {
			$('#completedtasks').html('您还未选择开始的作业项!');
			$('#tasks').html('您还未选择开始的作业项!');
		} else {
			var undoneStatus = role == 'qa' ? 4 : 1;
			$.each(taskitems, function(idx, item){
				item.status == undoneStatus ?uncompletedTaskitems.push(item): completedTaskitems.push(item);
			});
			renderTaskList( completedTaskitems, 1);		//1——已完成	
			renderTaskList( uncompletedTaskitems, 0); 	//O——未完成
		}
	}
	
	function initSubscribe() {
		amplify.subscribe('search.task.item', function( data ){
			console.log("订阅任务项查询");
			renderTaskList( filterResult(data.array,data.keyword), data.type );
		});
		
		amplify.subscribe('facility.search.finish', function( data ) {
			console.log("当poi查询完毕后，显示结束按钮");
			Msg.info("Poi 查询完成.");
			$('#taskId'+taskitemid).parent().find('a').css("display", "block");// 结束按钮 显示
		});
		
		amplify.subscribe('displayResult', function( data ) {
			Msg.info("BD & QQ查询完成.");
		});
		
		amplify.subscribe('task.saved', function( taskitemid,isDel ){
			taskitemSaved( taskitemid,isDel );
		});
	}
	
	function taskitemSaved( taskitemid,isDel ) {
		var idx = 0,len = uncompletedTaskitems.length;
		console.log("未完成的作页项的长度："+len+",taskitemid："+taskitemid);
		for( ;idx < len; idx++ ) {
			if( uncompletedTaskitems[idx].id == taskitemid ) {
				break;
			}
		}
		console.log("要完成的作页项长度："+idx+",");
		if( uncompletedTaskitems[idx] ) {
			completedTaskitems.push(uncompletedTaskitems[idx]);
			uncompletedTaskitems.splice(idx,1);
			renderTaskList(uncompletedTaskitems, 0);
			var del_status = completedTaskitems[completedTaskitems.length-1].del_status;
			console.log("删除的状态为："+del_status);
			if(isDel==0){
				completedTaskitems[completedTaskitems.length-1].del_status=1;
			}
			renderTaskList(completedTaskitems, 1);
		}
	}
	
	function filterResult(array , keyword) {
		var arraytmp = [];
		if ( keyword ) {
			$.each(array, function(idx, item){
				if(item.name.indexOf(keyword) > -1) {
					arraytmp.push(item);
				}	
			});
			console.log("查询任务项的长度："+arraytmp.length);
			return arraytmp.length > 0 ? arraytmp: array;
		}
		return array;
	}
	
	function renderTaskList( array , type) {
		$("#taskTemplate").load(path+"/js/template/editTaskItems.htm",function(){
			var wrapper = { items: array };
			var scriptTemplate = Handlebars.compile($('#taskTemplate').text());
			Handlebars.registerHelper("havVal",function(exist){
		   		return exist==1 ?  "visible": "hidden";
		   	 });   
	        var rs = scriptTemplate( wrapper );
        	var tag = type == 0 ? '#tasks':'#completedtasks';
			$(tag).height($("html").height() - 150);
			$(tag).html(rs);
			role == 'qa' ? $("#main_left [id=edit_item]").text("开始检查") : false;
		});
	}
	
	return {getTaskItems: getTaskItems}
});

