/* 任务列表
 */
var _processTaskDom;
define(['task/editTask'], function( Task ){
	
	function initTaskEvent(projectname){
		$("#taskTemplate").load(path+"/js/template/tasks.htm",function(){
			_processTaskDom = initTaskDomEvent;
			getTaskList(1,projectname);
    	});
		
		/*amplify.subscribe("task.loaded",function(data) {
	        initTaskDomEvent(data);
	    });*/
	    
	    /*amplify.subscribe("task.selectall",function(data) {
	    	Checkbox.select(data.button, data.table);
	    });*/
		
	};
	
	//初始化按钮操作
    function initTaskDomEvent(data) {

	    $('#task_delall').on('click',function() {
		    var v='';
		    $('#task_tab input[type=checkbox]:checked').each(function(){
			    v+= this.value +",";    
		    });    

		    Task.delTask(v.substring(0,v.length-1),data);
	    });
    	
    	$("[id='task_edit']").on("click" , function(e) {
    		NaviBar.reset("#ts");
    		NaviBar.add( $("[id='task_edit']").closest('tr').children()[1] , 'ta' );
    		var id = $($($( this ).closest('tr')).children()[0]).children()[0].value;
    		Task.editTask(id);
    	});

    	$("[id='task_recycle']").on("click" , function(e) {
    		var id = $($($( this ).closest('tr')).children()[0]).children()[0].value;
    		Task.recycleTask(id);
    	});
    	
	    $('.column').equalHeight();
    };
    
  //上一页
    this.prevTask = function(page, name){
	    var curPage = $("span.current").html();
	    if(parseInt(curPage)>1){
		    getTaskList(parseInt(curPage)-1, name);
	    }
    };
    
    //下一页
    this.nextTask = function(page, name){
	    var curPage = $("span.current").html();
	    if(parseInt(curPage)<parseInt(page)){
		    getTaskList(parseInt(curPage)+1, name);
	    }
    };

    //获取task列表
    this.getTaskList = function (page, name, callback){
	    if(typeof page == 'undefined') {
		    page = 1;
	    }
	    if(typeof name == 'undefined') {
		    name = '';
	    }

	    // ajax翻页请求
	    $.ajax({
		    type : "POST",
		    url : path+"/admin/alltask",
		    data : "page="+page+"&projectname="+name,
		    success : function(data) {
			    if(data.code == '100') {
			    	var pagecunt = parseInt(data.description)%9>0? parseInt(data.description/9)+1:parseInt(data.description/9);
			    	var str = '' ;
			    	var no = (name==''?'':", \""+name +"\"");
			    	if(pagecunt > 1){
			    		if(page==1){
			    			str += " <span class='disabled'><< prev</span>";
			    		}else{
			    			str += " <a style='cursor:pointer;' onclick='prevTask("+pagecunt+ no+ ")')><< prev</a>";
			    		}
			    		
			    		for (var k = 1; k <= pagecunt; k++) { 
			    			if(k==page){
			    				str += "<span class='current'>" + k + "</span>";
			    			}else{
			    				str += "<a onclick='getTaskList(" + k  + no+ ")'>" + k + "</a>";
			    			}
			    		}
			    		if(page == pagecunt){
			    			str += "<span class='disabled'>next >></span>";
			    		}else{
			    			str += "<a style='cursor:pointer;' onclick='nextTask("+pagecunt + no + ")'>next >></a>";
			    		}
			    	}
			    	var scriptTemplate = Handlebars.compile($('#taskTemplate').text());
			    	var wrapper = { items: eval(data.restring),totalNum: data.description, paging:str ,page:page,path:path };
			    	var rs = scriptTemplate( wrapper );
			    	
			    	$('#task_tab').html(rs);
			    	
			    	$('#task_selectall').on('click', function (){
//						amplify.publish("task.selectall", {"button":this, "table":'#task_tab'})
			    		Checkbox.select(this, '#task_tab');
					});
					
//			    	initTaskDomEvent(name);
			    	_processTaskDom(name);
			    	
                    if(callback) {
                    	callback();	
                    }
			    }
                amplify.publish("task.loaded" , name);
		    }
	    });
    };

    
    return {
    	initTaskEvent: initTaskEvent,
    	getTaskList : getTaskList
    }
});