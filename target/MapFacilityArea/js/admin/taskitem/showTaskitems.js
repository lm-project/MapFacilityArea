/**
 * 作业项列表
 */
define(['taskitem/editTaskitem'], function( Taskitem ){
	
	function initTaskitemEvent(id){
		
		$("#taskTemplate").load(path+"/js/template/taskItems.htm",function(){
			getTaskItemsList(1, id);
    	});
		
		amplify.subscribe("taskitem.loaded",function(data) {
        	initTaskitemDomEvent(data);
        });
        
        /*amplify.subscribe("taskitem.selectall",function(data) {
        	Checkbox.select(data.button, data.table);
        });*/
		
	};
	
	//初始化按钮操作
	function initTaskitemDomEvent(data) {
		
	    $('#taskitem_delall').on('click',function() {
		    var v='';
		    $('#taskitem_tab input[type=checkbox]:checked').each(function(){
			    v+= this.value +",";    
		    });    
		    Taskitem.delTaskitem(v.substring(0,v.length-1),data);
	    });

    	$("[id='taskitem_recycle']").on("click" , function(e) {
    		var id = $($($( this ).closest('tr')).children()[0]).children()[0].value;
    		Taskitem.recycleTaskitem(id, data);
    	});
	    
	    $('.column').equalHeight();
    };
    
  //上一页
    this.taskItemPrev = function (page, taskId){
        var curPage = $("span.current").html();
        if(parseInt(curPage)>1){
        	getTaskItemsList(parseInt(curPage)-1, taskId);
        }
    };

    //下一页
    this.taskItemNext = function (page, taskId){
        var curPage = $("span.current").html();
        if(parseInt(curPage)<parseInt(page)){
        	getTaskItemsList(parseInt(curPage)+1, taskId);
        }
    };	
    
  //异步获取子任务信息
    this.getTaskItemsList = function (page, taskId, callback){
    	if(typeof page == 'undefined') {
    		page = 1;
    	}
    	if(typeof taskId == 'undefined') {
    		taskId = '';
    	}
    	// ajax翻页请求
    	$.ajax({
    		type : "GET",
    		url : path+"/admin/taskitem/"+taskId,
    		data : "page=" + page,
    		success : function(data) {
    			if(data.code == '100') {
    				var pagecunt = parseInt(data.description)%9>0? parseInt(data.description/9)+1:parseInt(data.description/9);
    				var str = '' ;
    				var no = (taskId==''?'':","+taskId);
    				if(pagecunt > 1){
    					if(page==1){
    						str += " <span class='disabled'><< prev</span>";
    					}else{
    						str += " <a style='cursor:pointer;' onclick='taskItemPrev("+pagecunt+ no+ ")')><< prev</a>";
    					}
    					
    					for (var k = 1; k <= pagecunt; k++) { 
    						if(k==page){
    							str += "<span class='current'>" + k + "</span>";
    						}else{
    							str += "<a onclick='getTaskItemsList(" + k  + no+ ")'>" + k + "</a>";
    						}
    					}
    					if(page == pagecunt){
    						str += "<span class='disabled'>next >></span>";
    					}else{
    						str += "<a style='cursor:pointer;' onclick='taskItemNext("+pagecunt + no + ")'>next >></a>";
    					}
    				}
    				var scriptTemplate = Handlebars.compile($('#taskTemplate').text());
    				var wrapper = { items: eval(data.restring),totalNum: data.description, paging:str ,page:page,path:path };
    				var rs = scriptTemplate( wrapper );
    				$('#main').html(rs);
    				
    				$('#taskitem_selectall').on('click', function (){
//						amplify.publish("taskitem.selectall", {"button":this, "table":'#taskitem_tab'})
    					Checkbox.select(this, '#taskitem_tab');
					});
					
                    if(callback) {
                    	callback();	
                    }
    			}
    			amplify.publish("taskitem.loaded" , taskId);
    		}
    	});
    };
	
	return {
		initTaskitemEvent: initTaskitemEvent
    }
});