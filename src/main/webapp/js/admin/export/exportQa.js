/**
 * 导出质检数据
 */
define(['project/showProjects'], function( Project ){
	
	$("a.qaResult").on("click" ,function() {
		_processProDom = processQaDom;
		var main = 	
	         '<div id="showProject" class="module_content" >' 
	        + '</div>';
		$('#main').html(main);
		$("#taskTemplate").load(path+"/js/template/allProject.htm",function(){
			Project.getProject();
		});
	});
	
	//处理dom元素
	function processQaDom() {

		$('[id=prc_commit]').remove();
		$('[id=prc_detail]').remove();
		$("#exportQaResult").css("display", "block");

		$("[id='prc_edit']").on(
				"click",
				function(e) {
					var projectname = $(
							$($(this).parent().parent()).children()[1]).text();
					console.log(projectname);
				});

		$("#exportQaResult").on("click", function(e) {
			exportQaResult();
		})
	};
	
	//导出质检数据
	function exportQaResult() {
		var projectname = '';
	    var count = 0;
		$('#prc_tab input[type=checkbox]:checked').each(function(){
			projectname = $(this).val();    
	        count ++;
		});    

	    if ( ! projectname ){
	        alert("请选择项目.");
	        return;
	    } 
	    if ( count > 1 ) {
	        alert("只能选择一个项目");
	        return;
	    }
	    var type = 'qaresult';
	    //ajax
	    $.ajax({
			type : "POST",
			url : path+"/facility/exportShape",
			data : "param=" + projectname + "&type="+type,
			success : function(data) {
				if ( data.code == 100 ){
					$('span.mg').html(data.description + "点击下载：<a href='" + data.restring+"' target='_blank'>" +  data.restring +"</a>");
				} else {
					$('span.mg').html(data.description + data.restring);
				}
			}
		});
	    //ajax end
	};
	
});