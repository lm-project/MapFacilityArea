require([ 'qa','../task/showProjects'], function( Qa ,getQAProjects){
	ShowProjects.getProject();
	/*$('#queryPro').on('click', function (){
		amplify.publish("qaproject.loaded", {})
	});
	
	$('#recevie').click( function (){
		amplify.publish("qaproject.recevie", {})
	});*/
	Qa.initEvent();
	 
});


