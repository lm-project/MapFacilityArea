(function($) {
	var css = "<style type='text/css'>" +
			 "#div{position:absolute; width:102px;height:20px; border:1px solid #C5C5C5;background:#FFFFFF; " +
			       "box-shadow: 0px 3px 2px #DADBE0; border-radius:5px;display:none;z-index: 99999999999999999999999;} " +
			  ".s1{ position:absolute; top:-13px;  left:50px; width:0px; height:0px; " +
			       "border-top:5px solid transparent; border-left:5px solid transparent; " +
			       "border-right:5px solid transparent;border-bottom:5px solid black; }" +
			  ".s2{ position:absolute;top:-13px;left:50px;width:0px;height:0px;  " +
			       "border-top:5px solid transparent; border-left:5px solid transparent;  " +
			       "border-right:5px solid transparent;border-bottom:5px solid #fff;}" +
			  ".errLogal{color:#ffffff;position:absolute;text-align:center;font-weight:600;" +
			  			"top:4px; left:5px;width:13px;height:13px;background:#FF6D00;}" +
			  ".ErroText{font-family:monospace;text-align:center;position:absolute;top:3px; left:25px;}" +
			  "</style>";
	var domElement = "<div id='div'>" +
			"<span class='errLogal'>!</span>" +
			"<span class='ErroText'>请填写此字段<span>" +
			"<span class='s1'></span>" +
			"<span class='s2'></span>" + "</div> ";
	$(css).appendTo("head");
	$("body").prepend(domElement);
	$(document).click(function(){
		 $("#div").css("display","none");
	});
	
	$.checkValue = function (parentEle){
		var ok = true;
		$("#"+parentEle).find("[required=required]").each(function(){
			if( $.trim($(this).val())===""){
				var position = $(this).offset();
				$("#div").css("top",position.top + parseInt($(this).css("height").replace(/px/,"")));
				$("#div").css("left",position.left);
				$("#div").css("display","block");
				return ok = false;
			}
		});
		$("#div").css("display","none");
		return ok;
	}
}(jQuery));