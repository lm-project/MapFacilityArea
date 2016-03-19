/**
 * 导入设施区域shp文件
 */
define([], function() {
	$('a.importShp').on('click',function(){
		$("#main").load(path+"/js/template/importShp.htm", function(){
			initImportShpEvent();
		});
	});
	
	function initImportShpEvent() {
		var f = false;
		
		$('#chooseShp').on('click',function(){
			$('#shpfiles').click();
		});
		
		$('#shpfiles').on("change", function(){
			var file = $(this).val();
			if(file == '' || file == null) {
				$('.mg4').html("请选择所要上传的文件！");
				$('#shpName').val('');
			} else {
				var index = file.lastIndexOf(".");
				if(index < 0) {
					$('.mg4').text("上传的文件格式不正确，请选择.zip文件！");
					f = false;
				} else {
					var ext = file.substring(index + 1, file.length);
					if(ext != "zip") {
						$('.mg4').text("上传的文件格式不正确，请选择.zip文件！");
						$('#shpName').val('');
						f = false;
					} else {
						$('.mg4').text('');
						$('#shpName').val(file);
						f = true;
					}
				}
			}
			
		});
		
		$('#submit').click(function(){
			var shpName = $('#shpName').val();
			var dataVersion = $('#dataVersion').val();
			console.log(dataVersion);
			if(shpName ==''){
				$('.mg4').text("请选择所要上传的文件！");
				f = false;
			}
			
			if(f) {
				$.ajaxFileUpload({
		            url:'admin/importShp',
		            secureuri:false,
		            type:"PUT",
		            data:{"uplaodfile": shpName, "dataVersion": dataVersion},
		            fileElementId:'shpfiles',
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
		            		$(".mg4").text("连接服务器失败，未能导入shape文件!");
		            	}
		            }
				});
			}
		});
	};
	
	return;
	
});
