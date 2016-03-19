var Main = (function($) {
	var init = function() {
		$('#project').on('click', function (){
			amplify.publish("qaproject.loaded", {})
		});
		
		$('#recevie').click( function (){
				amplify.publish("qaproject.recevie", {})
		});
		
	};
	
	return new init();
})(jQuery);


