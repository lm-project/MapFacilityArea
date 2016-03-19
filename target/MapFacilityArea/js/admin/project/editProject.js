/**
 * TODO：项目提交 & 获取项目详细信息
 */
define([ 'task/showTasks' ], function(Task) {
	// 项目详细信息编辑
	function editPro(projectname) {
		console.log(projectname);
		$.ajax({
			type : "POST",
			url : path + '/admin/project/edit',
			data : "projectname=" + projectname,
			success : function(data) {
				if (data.code == '100') {
					$("#taskTemplate").load(path + "/js/template/proDetail.htm",
							function() {
								var scriptTemplate = Handlebars.compile($('#taskTemplate').text());
								var rs = scriptTemplate( eval('['+data.restring+']')[0] );
								$('#main').html(rs);
								Task.initTaskEvent(projectname);
							});
				}// end if
			}
		});// end ajsx
	}
	;

	// 项目提交
	function commitPro(projectname) {
		$.ajax({
			type : "POST",
			url : path + '/admin/project/commit',
			data : "projectname=" + projectname,
			success : function(data) {
				alert(data.description + data.restring);
			}
		});

	};
	
	//删除项目
	function delPro(projects){
	    if(projects!='' && confirm("是否删除选中项目?")){
		    $.ajax({ // ajax删除请求
			    type : "POST",
			    url : path+"/admin/delproject/",
			    data : "projects=" + projects,
			    success : function(data) {
				    if(data.code == '100') {
					    alert(data.description);
					    $("a.showAllTask").click();
				    }
			    }
		    });
		    //ajax end
	    }
	};

	return {
		editPro : editPro,
		commitPro : commitPro,
		delPro : delPro
	}

});