define(function() {
	var select = function(_this, e) {
		if ($(_this).val() === "全选") {
			$(_this).attr("value", "取消");
			$(e + ' input[type=checkbox]').attr("checked", "false");
		} else {
			$(_this).attr("value", "全选");
			$(e + ' input[type=checkbox]').removeAttr("checked");
		}
	};

	var getSelect = function(e) {
		var projectname='';
		$("input[name='" + e + "']:checked").each(function() {
			projectname += this.value +",";
		});
		
		return projectname.indexOf(",") > -1 ?  projectname.substring(0, projectname.length -1): ''; 
	}
	return {
		select : select,
		getSelect : getSelect
	}
});
