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
		function uploadError(data, status){
			var result = eval(data).responseText;
        	if( result != 'undefined' ) {
        		if ( result.indexOf('\"code\":100') >0 ) {
            		$(".mg4").text("上传成功！");
            	} else if ( result.indexOf('\"code\":200')  >0) {
            		$(".mg4").text(result.substring(result.lastIndexOf("description")+14,result.lastIndexOf("</pre>")-2));
            	}
        	} else {
        		$(".mg4").text("连接服务器失败，未能导入任务清单！");
        	}
		}
		function ajaxFileUpload(filename,remark){
			$.ajaxFileUpload({
	            url:'admin/newtask',
	            secureuri:false,
	            type:"PUT",
	            data:{"uplaodfile": filename, "remark": remark},
	            fileElementId:'myfiles',
	            dataType: 'json',
	            error: function (data, status) {
	            	uploadError(data, status);
	            }
			});
		}
		
		$('#submit').click(function(){
			var filename = $('#filename').val();   //得到文件的路劲
			var remark = $('#remark').val();       //上传任务时的备注
			if(filename ==''){
				$('.mg4').text("请选择所要上传的文件！");
				return;
			}
			ajaxFileUpload(filename,$.trim(remark)===''? "无":remark);
		});
	}
	function getUrlParam(name) {
		var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
		var r = window.location.search.substr(1).match(reg);
		if (r!=null) return unescape(r[2]); return null;
	};
	
	return;
});
