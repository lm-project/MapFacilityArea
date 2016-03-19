define(function() {
	var checkok = true;
	function checkInfo(data){
		var checkInfo = data.description;
		var chekArr = checkInfo.split(";");
		var infoLenght = chekArr.length;
   	    if( infoLenght>2){
   	    	var erroInfo = "";
   	    	for(var i=1;i<infoLenght;i++){
   	    		erroInfo += i+"."+chekArr[i];
   	    		console.log((i)+"."+chekArr[i]);
   	    	}
   	    	alert(erroInfo);
   	    	return checkok = false;
   	    }
	}
	
	function checkPolygonByReqService(editPolygon){//请求后端进行检查
		$.ajax({
		    type: "post",
		    url : path + "/checkPolygon",
		    dataType: 'json',
		    data: 'polygon=' + editPolygon.toString(),
		    error: function(XMLHttpRequest, error, errorThrown){  
		    	console.log("请求出错:  "+error);
		    },
		    success: function(data){
		    	console.log("请求ok:  "+data.description);
		       if(data.code==100){
		    	   checkInfo(data);
		       }else{
		    	   alert("检查出错");
		    	   return checkok = false ;
		       }
	        }
        });
	}
	
	
	return function checkEditPolygon(editPolygon){
		 checkPolygonByReqService(editPolygon);    //通过后端检查polygon，如果所有检查项前端能解决，可以删掉此方法
		
		 return checkok ;
	}
});