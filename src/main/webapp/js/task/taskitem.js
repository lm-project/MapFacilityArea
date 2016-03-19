/**
 * 作业项表单管理
 * 功能：作业员（opr）/质检（qa）开始、结束编辑，表单的聚焦和按钮状态的变化
 * 获取表单的字段信息
 */
define( function() {
	var startCheckEditTxt = "开始检查";
	var endCheckEditTxt = "结束检查";
	var startOprEditTxt = "开始编辑";
	var endOprEditTxt = "结束编辑";
	
	/**
	 * that 点击开始编辑的对象
	 */
	function operatorBeginEditItem( that ) {
		beginEditItem( that, startOprEditTxt, endOprEditTxt);		
		amplify.subscribe( "facility.search.finish" , function() {
        	$( that ).text( endOprEditTxt ); //标记本按钮为结束状态
        });
	}
	
	function getTaskitem( taskitemid ) {
		console.log("返回此任务项的anmeChn,status,city,mesh   "+taskitemid);
		return {
			nameChn: getNameChn( taskitemid ),
			status: $("#status"+taskitemid).text(),
			city: getCity( taskitemid ),
			mesh: getMesh( taskitemid ),
		}
	}
	function getNameChn( taskitemid ) {
		return $("#tname"+taskitemid).text();
	}
	function getMesh( taskitemid ) {
		return $("#mesh"+taskitemid).text();
	}
	function getCity( taskitemid ) {
		return $("#city"+taskitemid).text();
	}
	
	function operatorEndEditItem( taskitemid ) {
		endEditItem( startOprEditTxt, taskitemid );
	}
	
	function qaBeginEditItem( that ) {
		beginEditItem( that, startCheckEditTxt, endCheckEditTxt);
	}
	
	function qaEndEditItem( taskitemid ) {
		endEditItem( startCheckEditTxt, taskitemid );
	}
	
	function qaInitBtnName() {
		$('#main_left [id=edit_item]').text(startCheckEditTxt);
//		$('#main_left [id=edit_item]').each( function(){
//			$(this).text(startCheckEditTxt);
//		});
	}
	
	function endEditItem( startTxt, taskitemid ) {
		$('[id=edit_item]').each(function(){
        	$(this).text( startTxt );
        })
		$('.tasks ul').removeClass("focus").addClass("ul");
		$('.tasks ul').find('li:first a').css("display","block");
		
		//删除“删除”和“编辑顶点”按钮
		$("#href"+taskitemid).remove();
		$('#editPoint').remove();
	}
	
	function beginEditItem( that, startTxt, endTxt) {
		console.log("改变点击编辑的样式为结束编辑");
		$('.tasks #edit').text(startTxt);
		$(that).text( endTxt );
		$('.tasks ul').removeClass("focus").addClass("ul");
		$(that).parent().parent().removeClass("ul").addClass("focus");
		$('.tasks ul[class!=focus]').find('li:first a').css("display","none");
	}
	
	return {
		operatorBeginEditItem: operatorBeginEditItem,
		qaEndEditItem: qaEndEditItem,
		qaBeginEditItem: qaBeginEditItem,
		operatorEndEditItem: operatorEndEditItem,
		getNameChn: getNameChn,
		getMesh: getMesh,
		getCity: getCity,
		getTaskitem: getTaskitem,
		qaInitBtnName: qaInitBtnName
	}
});