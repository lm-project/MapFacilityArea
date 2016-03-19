require([ '../common/handlebars-v1.3.0', 
          '../common/Checkbox',
          '../common/hideshow' , 
          '../common/WdatePicker',
          '../task/showProjects',  
          '../task/queryProject', 
          '../operator/op'], function(H,Checkbox,HideShow,WdatePicker,
        		  ShowProjects,QueryProjects,Op) {
					Op(Checkbox);
});
