var Resize = (function($) {
	var init = function(h) {
		$('#map').height($("html").height() - h);
	};
	
	var resize = function(h) {
		window.onresize = function() {
			$('#map').height($("html").height() - h);
		};
	}
	
	return {
	　　init : init,
	　　resize : resize
	};
})();
