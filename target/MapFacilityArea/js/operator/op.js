var Operator = (function($) {
	$("#selectall1").click(function() {
		Checkbox.select(this, "#tab2");
	});
	
	$("#selectalltask").live("click", function() {
		Checkbox.select(this, "#tab3");
	});
	
	$("#receive").click(function() {
		receive();
	}); 
	
	
	 function receive(){
	    	var v=[];
	    	var taskitemids= '';
	    	var i = 0;
	    	$('#tab2 input[type=checkbox]:checked').each(function(){
			    v[i]= $.trim($(this).val());  
			    i++;
			});    
	    	if( i == 0){
	    		alert("您还未选取任务！");
	    		return;
	    	}
	    	if(confirm("是否领取选中任务?")){
				$.ajax({
					type : "POST",
					url : path+"/operator/receive",
					data : "taskids="+v,
					success : function(data) {
						if(data.code == '100') {
							alert(data.description);
							for(i = 0; i< v.length; i++) {
								$("#tab2 input[value="+v[i]+"]:checked").parent().parent().remove();
							}
							var count = $('span font').html();
							$('span font').html(count-v.length);
						}
					}
				});//end ajax
			}// end if
	    }//end function
	
	return {
	};
	
})(jQuery);

