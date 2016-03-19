var NaviBar = (function($) {
	var add = function( e , id) {
		var t = $(e).text();
		var bar = "<div class='breadcrumb_divider'></div><a class='current' id='"+id+"' href='javascript:void(0)'>"+t+"</a>";
		$('.breadcrumbs').append(bar);
	};
	var reset = function( e ) {
		$('.breadcrumbs '+e).nextAll().remove();
	};
	
	return {
	　　add : add,
	　　reset : reset
	};
}(jQuery));