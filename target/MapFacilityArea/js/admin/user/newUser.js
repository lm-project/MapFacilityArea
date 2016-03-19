/**
 * 新建用户
 */
define([], function(){
	
	$('#newUser').on('click',function(){
		NaviBar.reset("#main_a");
		NaviBar.add(this, 'user');
		$("#main").load(path+"/js/template/newUser.htm",function(){
			initNewUserEvent();
		});
	});
	
	var validations = [1,1,1,1,1,1];
	
	function initNewUserEvent(){
		$('.column').equalHeight();
		
		//$('#username').focus();
		$('#reset').trigger("click");
		docchange('username', 1);
		docchange('password1', 2);
		docchange('password2', 3);
		docchange('truename', 4);
		docchange('email', 5);
		docchange('role', 6);
		$('#submitNewUser').click(function() {
			var sum = validations[0]+validations[1]+validations[2]+validations[3]+validations[4]+validations[5];
			console.log(sum);
			username = $('#username').val();
			password = $('#password1').val();
			truename = $('#truename').val();
			email = $('#email').val();
			role = $('#role').val();
			if(sum == 0) {
				// ajax登录请求
				$.ajax({
					type : "POST",
					url : path+"/admin/newu",
					data : "username="+username+"&password="+password+"&truename="+truename+"&email="+email+"&role="+role,
					success : function(data) {
						if(data.code == '100') {
							$('span.mg3').html("<img src='"+path+"/images/admin/icn_alert_success.png'>添加完毕!");	
						}
					}
				});
			}
			return false;
		});
	};
	
	function docchange(e,i) {
		if (i==6) {
			$('#' + e).change(function() {
				if ($(this).val() == '0') {
					$(this).next().find("span").removeClass("img2").addClass("img1");
					$(this).next().next().css("display","block");
					validations[5] = 1;
					return;
				} else {
					$(this).next().find("span").removeClass("img1").addClass("img2");
					$(this).next().next().css("display","none");
					validations[5] = 0;;
				}
			});
		} else {
			$('#' + e).bind("input propertychange",function() {
				if ($.trim($(this).val()) == '') {
					if ($.trim($('#password1').val()) !='' && e == 'password2') {
						$(this).next().next().find("span").text("请重新输入密码！");
					}else if($.trim($('#password1').val()) =='' && e == 'password2'){
						validations[1] = 1;
					}
					$(this).next().next().css("display","block");
					$(this).next().find("span").removeClass("img2").addClass("img1");
					validations[i-1] = 1;
					return;
				} else {
					if (i == 2) {
						if($.trim($('#'+e).val()).length < 6) {
							$(this).next().find("span").removeClass("img2").addClass("img1");
							$(this).next().next().find("span").text("输入的密码长度不少于6位！");
							$(this).next().next().css("display","block");
							return;
						}else if ($.trim($('#password2').val()) != ''&& $('#' + e).val() != $('#password2').val()) {
							$(this).next().find("span").removeClass("img1").addClass("img2");
							$(this).next().css("display","none");
							$('#password2').next().find("span").removeClass("img2").addClass("img1");
							$('#password2').next().next().next().css("display","block");
							validations[1] = 1;
							return;
						}
					}
					if (i == 3) {
						if($.trim($('#password1').val()).length = 0 && $.trim($('#'+e).val()).length < 6) {
							$(this).next().find("span").removeClass("img2").addClass("img1");
							$(this).next().next().find("span").text("输入的密码长度不少于6位！");
							$(this).next().next().css("display","block");
							return;
						}else if ($.trim($('#password1').val()) != ''&& $('#' + e).val() != $('#password1').val()) {
							$(this).next().find("span").removeClass("img2").addClass("img1");
							$(this).next().next().find("span").text("俩次输入的密码不相同！");
							$(this).next().next().next().css("display","block");
							validations[2] = 1;
							return;
						}
					}
					if (i == 5) {
							var myreg = /^([a-zA-Z0-9]+[_|\_|\.|\-]?)+@([a-zA-Z0-9]+[_|\_|\.|\-]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;

							if (!myreg.test($(this).val())) {
								$(this).next().find("span").removeClass("img2").addClass("img1");
								$(this).next().next().css("display","block");
								validations[4] = 1;
								return;
							}
					}
					$(this).next().next().css("display","none");
					$(this).next().find("span").removeClass("img1").addClass("img2");
					validations[i-1] = 0;
					return;
				}
			});
		}
	};

	function reset(){
	};
	
});