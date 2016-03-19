/**
 * 任务项分页
 */

//上一页
function prev(page, assid, tname, origin, stime, etime , city){
	var curPage = $("span.current").html();
	if(parseInt(curPage)>1){
		getData(parseInt(curPage)-1, assid, tname, origin, stime, etime , city);
	}
}

//下一页
function next(page, assid, tname, origin, stime, etime , city){
	var curPage = $("span.current").html();
	if(parseInt(curPage)<parseInt(page)){
		getData(parseInt(curPage)+1, assid, tname, origin, stime, etime , city);
	}
}

function getData(page, assid, tname, origin, stime, etime , city){
	if(typeof page == 'undefined'){
		page = 1;
	}
	if(typeof assid == 'undefined'){
		assid = $('#assid').val()== 0 ? "":$('#assid').val(); 
//		assid = assid == 0 ? "":assid;
	}
	if(typeof tname == 'undefined'){
		tname = $('#tname').val();
	}
	if(typeof origin == 'undefined'){
		origin = $('#origin').val()== 0 ? "":$('#origin').val(); 
//		origin = origin==0 ? "": origin;
	}
	if(typeof stime == 'undefined'){
		stime = $('#time1').val();
	}
	if(typeof etime == 'undefined'){
		etime = $('#time2').val();
	}
	if(typeof city == 'undefined'){
		city = $('#city').val()==0 ? "":$('#city').val(); 
//		city = city== 0 ? "": city;
	}
	if(page == 1){
		index = 0;
	}
	$.ajax({
		type : "GET",
		url : path+"/Operator/searchByPage",
		data : "page="+page,
		success : function(data) {
			if(data.code == '100') {
				
	    		$("#taskTemplate").load(path+"/js/template/alltask.htm",function(){
	    			var scriptTemplate = Handlebars.compile($('#taskTemplate').text());
	    			var wrapper = { items: eval(data.restring) ,path:path };
	    			var rs = scriptTemplate( wrapper );
	    			console.log( rs );
                    $('#tab2').html(rs);
                    $('[id=edit]').remove();
                    $('[id=delete]').remove();
                    $('[id=delall]').remove();
                    $('[id=selectall]').remove();
	    		});
			}
		}
	});	
}