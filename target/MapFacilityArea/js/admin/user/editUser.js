define([], function(){
	var f = 1;
	
	function editUser(v){
		$.get(path + "/admin/editu/" + v, function(data, status) {
			$("#taskTemplate").load(
					path + "/js/template/editUser.htm",
					function() {

						var scriptTemplate = Handlebars.compile($(
								'#taskTemplate').text());
						console.log(eval(data.restring));
						var rs = scriptTemplate(eval(data.restring)[0]);

						$('#main').html(rs);
						setTimeout(100);
						bindEditUserButton();
					});
		});
	};
	
	function delUser(v){
		if(v.indexOf('/')){
			v = v.substring(v.indexOf('/')+1);
		}
		if(confirm("是否删除选中用户?")){
			// ajax删除请求
			$.ajax({
				type : "POST",
				url : path+"/admin/delu",
				data : "uids="+v,
				success : function(data) {
					if(data.code == '100') {
						alert(data.description);
						//window.location.href='allu';
						getUserData();
						/*for(i = 0; i< v.length; i++) {
							$("#tab1 input[value="+v[i]+"]:checked").parent().parent().remove();
						}*/
					}
				}
			});
			//ajax end
		}
	};
	
	function editUserdocchange(e,i) {
		if (i==6) {
			$('#' + e).change(function() {
				if ($(this).val() == '0') {
					$(this).next().find("span").removeClass("img2").addClass("img1");
					$(this).next().next().css("display","block");
					return;
				} else {
					$(this).next().find("span").removeClass("img1").addClass("img2");
					$(this).next().next().css("display","none");
					return;
				}
			});
		} else {
			$('#' + e).bind("input propertychange",function() {
				if ($.trim($(this).val()) == '') {
					if(i==2 || i==3){
						return;
					}
					$(this).next().next().css("display","block");
					$(this).next().find("span").removeClass("img2").addClass("img1");
					return;
				} else {
					if (i == 2) {
						if($.trim($('#password1').val()).length < 6 || $.trim($('#password2').val()).length < 6) {
							$('#password2').next().find("span").removeClass("img2").addClass("img1");
							$('#password2').next().next().find("span").text("输入的密码长度不少于6位！");
							$('#password2').next().next().css("display","block");
							return;
						}else if ($.trim($('#password2').val()) != '' && $('#' + e).val() == $('#password2').val()) {
							$('#password2').next().next().css("display","none");
							$('#password2').next().find("span").removeClass("img1").addClass("img2");
							f = 0;
							return;
						}else if($.trim($('#password2').val()) != ''&& $('#' + e).val() != $('#password2').val()){
							$('#password2').next().find("span").removeClass("img2").addClass("img1");
							$('#password2').next().next().find("span").text("俩次输入的密码不相同！");
							$('#password2').next().next().css("display","block");
							return;
						}else if($.trim($('#password2').val()) == '') {
							$('#password2').next().find("span").removeClass("img2").addClass("img1");
							$('#password2').next().next().find("span").text("请重复输入密码！");
							$('#password2').next().next().css("display","block");
							return;
						}
					}
					if (i == 3) {
						if($.trim($('#password1').val()).length < 6 || $.trim($('#password2').val()).length < 6) {
							$('#password2').next().find("span").removeClass("img2").addClass("img1");
							$('#password2').next().next().find("span").text("输入的密码长度不少于6位！");
							$('#password2').next().next().css("display","block");
							return;
						}else if ($('#' + e).val() != $('#password1').val()) {
							$(this).next().find("span").removeClass("img2").addClass("img1");
							$(this).next().next().find("span").text("俩次输入的密码不相同！");
							$(this).next().next().css("display","block");
							return;
						}else{
							f = 0;
						}
						
					}
					if (i == 5) {
							var myreg = /^([a-zA-Z0-9]+[_|\_|\.|\-]?)+@([a-zA-Z0-9]+[_|\_|\.|\-]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
							if (!myreg.test($(this).val())) {
								$(this).next().find("span").removeClass("img2").addClass("img1");
								$(this).next().next().css("display","block");
								return;
							}
					}
					$(this).next().next().css("display","none");
					$(this).next().find("span").removeClass("img1").addClass("img2");
					return;
				}
			});
		}
	};
	
	function change(e){
		$(e).next().find("span").removeClass("img1").addClass("img2");
	};
	
	function bindEditUserButton() {
		//$('#username').focus();
		change($('#username'));
		change($('#truename'));
		change($('#email'));
		change($('#role'));
		var r ='${usere.role}';
		$('#role').val(r).attr("check", "true");
		$('#password1').next().css("display","none");
		$('#password1').next().next().css("display","block");
		editUserdocchange('password1', 2);
		editUserdocchange('password2', 3);
		editUserdocchange('truename', 4);
		editUserdocchange('email', 5);
		editUserdocchange('role', 6);
		
		$('#edit').on('click',function() {
			var uid;
			var password='';
			var truename='';
			var email='';
			var role='';
			if(f==0){
				password = $('#password1').val();
			}
			uid = $('#uid').val();
			truename = $('#truename').val();
			email = $('#email').val();
			role = $('#role').val();
				// ajax登录请求
				$.ajax({
					type : "POST",
					url : path+"/admin/editu",
					data : "uid="+uid+"&password="+password+"&truename="+truename+"&email="+email+"&role="+role,
					success : function(data) {
						if(data.code == '100') {
							$('span.mg3').html("<img src='"+path+"/images/admin/icn_alert_success.png'>修改完毕!");	
						}
					}
				});
			return false;
		});
		
		$('#back').on('click',function(){
			var main = 	
		         '<div id="showUser" class="module_content" >' 
		        + '</div>';
				$('#main').html(main);
				$("#taskTemplate").load(path+"/js/template/allUser.htm",function(){
					getUserData();
				});
		});
	};
	
	return {
		editUser: editUser,
		delUser: delUser,
	}
	
});
	