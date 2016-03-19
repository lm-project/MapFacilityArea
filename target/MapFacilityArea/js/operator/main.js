require([], function() {
	require([ '../common/handlebars-v1.3.0', '../common/selectcheckbox',
	          '../common/hideshow' , '../common/WdatePicker',
	          '../task/showProjects',  '../task/queryProject', '../operator/op'], function(Operator) {
		ShowProjects.getProject();
		QureyProjects.getProject();
	});

});