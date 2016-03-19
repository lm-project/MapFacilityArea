function getUnRecevied (){
	var page, assid, taskname, origin, stime, etime , city;
	if(typeof page == 'undefined'){
		page = 1;
	}
		
	if(typeof assid == 'undefined'){
		assid = $('#assid').val()== 0 ? "":$('#assid').val(); 
	}
	if(typeof taskname == 'undefined'){
		taskname = $('#tname').val();
	}
	if(typeof origin == 'undefined'){
		origin = $('#origin').val()== 0 ? "":$('#origin').val(); 
	}
	if(typeof stime == 'undefined'){
		stime = $('#time1').val();
	}
	if(typeof etime == 'undefined'){
		etime = $('#time2').val();
	}
	if(typeof city == 'undefined'){
		city = $('#city').val()==0 ? "":$('#city').val(); 
	}
	if(page == 1){
		index = 0;
	}
	$.ajax({
		type : "GET",
		url : path+"/operator/unrecevied/",
		data : "page=" + page,
		success : function(data) {
			if(data.code == '100') {
		    	$("#taskTemplate").load(path+"/js/template/received.htm",function(){
		    		var scriptTemplate = Handlebars.compile($('#taskTemplate').text());
		    		var wrapper = { items: eval(data.restring) ,path:path };
		    		var rs = scriptTemplate( wrapper );
		    		$('#tab2').html(rs);
		    		
		    	});
			}
		}
	});	
}
