/**
 * 地图编辑右侧的POI Form表单
 * 功能：poi表单的清空操作，开始编辑赋值操作
 */
define(function(){
	/**
	 * 清空表单操作
	 */
	function clear() {
		$('#NAME_CHN').val('');
		$('#MESH').val('');
	    $('#POI_ID').val('');
	    $('#FA_TYPE').val('');
	    $('#FA_FLAG').val('0');
	    $('#DISP_CLASS').val('0');
	    $('#polygon').val('');
	}
	
	/**
	 * 开始编辑，部分值域来源于作业项的信息
	 */
	function beginEdit(tname, mesh, taskitemid) {
		console.log("给poiForm表单的NAME_CHN，MESH，meshbox赋值,但还是没有任何意义，因为poifrom还没有加载");
		console.log("tname  :"+tname+"   mesh  :"+mesh+"   taskitemid:  "+taskitemid);
		$('#MESH').val(mesh);
//        $('#FA_TYPE').val(parseInt($("#typecode"+taskitemid).html()));
        $('#meshbox').attr("checked",false);
	}
	
	return {
		clear: clear,
		beginEdit: beginEdit
	}
});