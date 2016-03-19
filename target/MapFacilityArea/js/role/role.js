define(function(){
	return {
		isQa : function() {
			return "qa" === sessionStorage.getItem("role");
		}
	}
});