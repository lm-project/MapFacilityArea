define(function(){
	function info(msg, dom, append) {
	 	if(dom){
			$(dom).val( msg );
	 	}else {
	 		$('#message').html( msg );
	 	}
	}
	function error(msg, dom, append) {
	 	if(dom){
			$(dom).val( msg );
	 	}else {
	 		$('#errormessage').html( msg );
	 	}
	}
	function clearAll() {
		$('#message').html('');
		$('#errormessage').html('');
	}
	
	return {
		info: info,
		error: error,
		clearAll: clearAll
	}
});