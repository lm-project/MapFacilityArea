function initToolbar() {
	console.log("初始化map/initToolbar,并且给id=“editpolygon”增加点击事件。。。。。。");
		$("#editpolygon").click(function() {
			closeTools(mouseTool2);
		    var p = $("#editpolygon");
	
	        if( ! RefSearcher.isOpen ) {
		        if(  editorTool ) {
		            editorTool.open();
	                RefSearcher.isOpen = true;
	            }
	        }else {
		        if(  editorTool ) {
		            closeTools(editorTool);
	                RefSearcher.isOpen = false;
	            }
	        }
		});
}