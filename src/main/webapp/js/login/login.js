$(document).ready(function() {
	var url=document.URL;//获取URL
	if(url.indexOf("error") > -1){
		$("lable#mg").text("用户名或密码错误!");
		$(".mg").css("display","block");
	}
	
	
	$("#userName").val('');
	$("#password").val('');
	$("#userName").keydown(function(event) {
		if (event.keyCode == 13) {
			submitLoginForm();
		}
	});

	$("#password").keydown(function(event) {
		if (event.keyCode == 13) {
			submitLoginForm();
		}
	});
	if ($.cookie("rmbUser") == "true") {
		$("#ck_rmbUser").attr("checked", true);
		$("#userName").val($.cookie("username"));
		$("#password").val($.cookie("password"));
	}
});
function submitLoginForm() {
	var name_value = $("#userName").val();
	var pass_value = $("#password").val();
	var code_value = $("#index_code").val();
	var mg = $("lable#mg");
	// 验证用户名
	if (name_value == '') {
		mg.text("请输入用户名!");
		$(".mg").css("display","block");
		$("#userName").focus();
		return false;
	}
	// 验证密码
	if (pass_value == '') {
		mg.text("请输入密码!");
		$(".mg").css("display","block");
		$("#password").focus();
		return false;
	}
//	if(code_value == ''){
//		mg.text("请输入验证码!");
//		$(".mg").css("display","block");
//		$("#index_code").focus();
//		return false;
//	}
	$.ajax({
		type : "GET",
		url : "/MapFacilityArea/getCode",
		success : function(data) {
			console.log("获取验证码的信息："+data.code+"  "+data.description);
			var code = data.description.toLowerCase();
			console.log("前端验证码："+code_value.toLowerCase()+"  后端验证码："+code);
//			if(data.code==100 && code_value.toLowerCase()===code){
				$("#login").submit();
				Save();
//			}else{
//				mg.text("验证码有误!");
//				$(".mg").css("display","block");
//				$("#index_code").focus();
//				return false;
//			}
			
		}
	});
	
}

// 记住用户名密码
function Save() {
	if ($("#ck_rmbUser").attr("checked")) {
		var str_username = $("#userName").val();
		var str_password = $("#password").val();
		$.cookie("rmbUser", "true", {
			expires : 7
		}); // 存储一个带7天期限的cookie
		$.cookie("username", str_username, {
			expires : 7
		});
		$.cookie("password", str_password, {
			expires : 7
		});
	} else {
		$.cookie("rmbUser", "false", {
			expire : -1
		});
		$.cookie("username", "", {
			expires : -1
		});
		$.cookie("password", "", {
			expires : -1
		});
	}
};