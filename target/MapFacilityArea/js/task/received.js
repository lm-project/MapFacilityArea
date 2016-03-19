
function getReceive(iscommit, page){
	if(typeof page == 'undefined') {
			page = 1;
		}
    	$("#selectalltask").attr("value", "全选");
    	isSelect = 0;
    	if(iscommit == 0){
			 $("#edit").css("display","block");
		 }else{
			 $("#edit").css("display","none");
		 }
		 
    	// 异步查询请求
		$.ajax({
			type : "GET",
			url : path+"/task/receive",
			data :"status="+1 +"&pageIndex="+ page,
			success : function(data) {
				 var tl = eval(data);
				 if ( ! JSON.parse(tl.restring).length ){
				 	return;
				 }
				 $("#taskTemplate").load(path+"/js/template/received.htm",function(){
    	    			var scriptTemplate = Handlebars.compile($('#taskTemplate').text());
					 	
						var wrapper = { items: eval(data.restring)[0].list,totalNum: data.description, page:page,path:path };
                    	var rs = scriptTemplate( wrapper );
						$('#tab3').html(rs);
    	    		});
				 
			}
		});
    }