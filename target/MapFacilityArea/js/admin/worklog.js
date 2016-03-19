
function showWorkLog(){
	$('#current').css("display","none");
	$('#article1').css("display","none");
	$('#article2').css("display","none");
	$('#shape').css("display","block");
	$('#tab_container').css("display","none");
	$('#tab1').css("display","none");
	$('#loadmorepage').css("display","none");
	
    var main = 	
         '<div id="shape" class="module_content" >' 
        + '</div>';
	$('#main').html(main);


    $.ajax({
    	type : "POST",
    	url : path+"/workrecord",
    	success : function(data) {
    		renderthis(data);
    	}
    });	


}

    var renderthis = function (data) {
	    var tl = eval(data);
	    
	    var str = '';
	    str += "<table class=\"tablesorter\" cellspacing=\"0\"> ";
        str +=" <thead>  <tr> <th>时间</th> <th>任务</th>  <th>作业员</th></tr>  </thead> ";
        var date = new Date();
	    for(var i=0; i<tl.length; i++){
	 	    date.setTime(tl[i].committime);
		    str +="<tr><td>"+ date.getFullYear()+"-"+date.getMonth()+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+"</td>";
		    str +="<td>"+ tl[i].taid+"</td>";
		    str +="<td>"+ tl[i].truename +"</td>";
	 	    str +="</tr>";
	    }
	    str += "</table>";
	    str += "<footer> <div style=\"margin-left: 20px;padding: 5px 0;\">";
        //	str += "<input type='button' value='刷新' class='alt_btn' style='margin-left:13px' onclick='shapeRefresh()'>";
        str += "</div>";
        str += "</footer> ";
	    $('#shape').text('');
	    $('#shape').append(str);
		$('#shape').jScrollPane();

    }


