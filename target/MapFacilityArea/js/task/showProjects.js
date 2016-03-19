var ShowProjects = (function($) {
	var getProject = function (){
		$.ajax({
			type : "GET",
			url : path+"/common/getprojects",
			success : function(data) {
				if(data.code == '100') {
					var tl=eval(data.restring); 
					var objDom = null;
	    		    var obj = document.getElementById("project");
					var qaSelectId = document.getElementById("prolist");
					if(qaSelectId==null){
						objDom = obj;
					}else {
						objDom = qaSelectId;
					}
	    		    for(var i=0; i<tl.length; i++){
	    				objDom.options.add(new Option(tl[i].projectname,tl[i].projectname));
	    			}// end for
				}// end if
			}
		});	
	}// enf function
	
	return {
	　　getProject : getProject,
	};
	
}( jQuery ));