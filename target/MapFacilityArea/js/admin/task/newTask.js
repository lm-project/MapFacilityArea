define(['task/ajaxfileupload'], function(){
	
	$('a.newTask').on('click',function(){
		$("#main").load(path+"/js/template/newTask.htm", function(){
			initNewTaskEvent();
		});
	});
	
	function initNewTaskEvent() {
		var f = false;
		if(getUrlParam('message')!=null){
			$('.mg3').text('上传文件内容不符合！');
		}
		
		$('#chooseFile').on('click',function(){
			$('#myfiles').click();
		});
		
		$('#myfiles').on("change", function(){
			var file = $(this).val();
			if(file == '' || file == null) {
				$('.mg4').html("请选择所要上传的文件！");
				$('#filename').val('');
			} else {
				var index = file.lastIndexOf(".");
				if(index < 0) {
					$('.mg4').text("上传的文件格式不正确，请选择excel文件！");
					f = false;
				} else {
					var ext = file.substring(index + 1, file.length);
					if(ext != "xlsx" && ext != "xls") {
						$('.mg4').text("上传的文件格式不正确，请选择excel文件！");
						$('#filename').val('');
						f = false;
					} else {
						$('.mg4').text('');
						$('#filename').val(file);
						f = true;
					}
				}
			}
			
		});
		
		/*
		 * $('#submit').click(function(){ var filename = $('#filename').val();
		 * if(filename ==''){ $('.mg4').text("请选择所要上传的文件！"); f = false; } return
		 * f; });
		 */
		
		$('#submit').click(function(){
			var filename = $('#filename').val();
			var remark = $('#remark').val();
			
			if(filename ==''){
				$('.mg4').text("请选择所要上传的文件！");
				f = false;
			}
			
			if(f) {
				$.ajaxFileUpload({
		            url:'admin/newtask',
		            secureuri:false,
		            type:"PUT",
		            data:{"uplaodfile": filename, "remark": remark},
		            fileElementId:'myfiles',
		            dataType: 'json',
		            error: function (data, status)
		            {
		            	var result = eval(data).responseText;
		            	if( result != 'undefined' ) {
		            		if ( result.indexOf('\"code\":100') >0 ) {
			            		$(".mg4").text("上传成功！");
			            	} else if ( result.indexOf('\"code\":200')  >0) {
			            		$(".mg4").text("上传文件出错，请选择正确的上传文件!");
			            	}
		            	} else {
		            		$(".mg4").text("连接服务器失败，未能导入任务清单！");
		            	}
		            }
				});
			}
		});
	};
	
	function getUrlParam(name) {
		var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
		var r = window.location.search.substr(1).match(reg);
		if (r!=null) return unescape(r[2]); return null;
	};
	
	return;
});
