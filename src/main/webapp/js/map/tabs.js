var searchTaskitemTap = 0; 		//默认为未完成任务页
define(function(){
	console.log("加载map/tabs并且运行,jQuery.jqtab1函数，#taskitem中第一个li放的是未完成的任务项，第二个li放的是已完成的任务项");
	$(document).ready(function() {
		console.log("给已完成和未完成任务项标签注册点击事件，当点击时，记录点击的标签到searchTaskitemTap，然后显示相应的任务");
		jQuery.jqtab1 = function(tabtit,tab_conbox,shijian) {
			$(tab_conbox).find("li").hide();
			$(tab_conbox).find("li:first").show();
		
			$(tabtit).find("li").click(function(){
				$(this).addClass("action").siblings("li").removeClass("action"); 
				var activeindex = $(tabtit).find("li").index(this);
				searchTaskitemTap = activeindex;
				$(tab_conbox).children().eq(activeindex).show().siblings().hide();
				return false;
			});
		
		};
		$.jqtab1(".top2","#taskitem","click");
	});
});
