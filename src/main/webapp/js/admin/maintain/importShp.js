/**
 * 导入设施区域shp文件
 */
define([], function() {
	
	function showChangeInfo(errorInfo,filePath){
		$('.mg4').text(errorInfo);
		$('#shpName').val(filePath);
	}
	
	function importFileError(data){
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
	
	function ajaxImportFile(shpName){
		$.ajaxFileUpload({
            url:'admin/importShp',
            secureuri:false,
            type:"PUT",
            data:{"uplaodfile": shpName, "dataVersion": $('#dataVersion').val()},
            fileElementId:'shpfiles',
            dataType: 'json',
            error: function (data, status){
            	importFileError(data);
            },
            success:function(data,status){
            	console.log("上传成功的状态："+status+","+data);
            	if ( data.indexOf('\"code\":100') >0 ) {
            		$(".mg4").text("上传成功！");
            	} 
            }
		});
	}
		
	
	function inputChangeEve(file){
		if(file == '' || file == null) {
			showChangeInfo("请选择所要上传的文件！",'');
			return "";
		} 
		var index = file.lastIndexOf(".");
		if(index < 0) {
			showChangeInfo("上传的文件格式不正确，请选择.zip文件！",'');
			return "";
		} 
		var ext = file.substring(index + 1, file.length);
		if(ext != "zip") {
			showChangeInfo("上传的文件格式不正确，请选择.zip文件！",'');
			return "";
		}
		showChangeInfo("",file);
		return file;
	}
	
	function initImportShpEvent() {
		var isImportFile = "";
		$('#chooseShp').on('click',function(){
			$('#shpfiles').click();
		});
		
		$('#shpfiles').on("change", function(){
			isImportFile = inputChangeEve($(this).val());		
		});
		
		$('#submit').click(function(){
			if(!isImportFile){
				return;
			}
			ajaxImportFile(isImportFile);
		});
	};
	
	$('a.importShp').on('click',function(){
		$("#main").load(path+"/js/template/importShp.htm", function(){
			initImportShpEvent();
		});
	});
	
});
