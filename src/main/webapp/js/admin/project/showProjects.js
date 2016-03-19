/**
 * 项目列表
 */
var _processProDom;
define(['project/editProject'], function( Project ){
	Handlebars.registerHelper('compare', function(v1, v2, options) {
        if(v1 > v2) {
            return options.fn(this);
        }
        return options.inverse(this);
    });
	
	$("a.showAllTask").on("click" ,function() {
		_processProDom = processDom;
		NaviBar.reset("#main_a");
		NaviBar.add(this, 'pro');
		var main = '<div id="showProject" class="module_content" ></div>';
		$('#main').html(main);
		$("#taskTemplate").load(path+"/js/template/allProject.htm",function(){
			getProject();
		});
		$("a#pro").on("click" ,function() {
			$("a.showAllTask").click();
		});
		
	});
	
	  //上一页
	this.prev = function(page){
	    var curPage = $("span.current").html();
	    if(parseInt(curPage)>1){
	    	getProject(parseInt(curPage)-1);
	    }
    };
    
    //下一页
    this.next = function(page){
	    var curPage = $("span.current").html();
	    if(parseInt(curPage)<parseInt(page)){
	    	getProject(parseInt(curPage)+1);
	    }
    };
	
	
//	amplify.subscribe("project.checkbox",function(data) {
//    	Checkbox.select(data.button, data.table);
//    });
	
	//处理DOM元素
	function processDom() {
		
    	$("[id='prc_commit']").on("click" , function(e) {
    		var projectname =  $($($( this ).parent().parent()).children()[1]).text();
    		Project.commitPro(projectname);
    	});
    	
    	$("[id='prc_edit']").on("click" , function(e) {
    		NaviBar.reset("#ts");
    		NaviBar.add($(this).parent().parent().children()[1], 'ts');
    		var projectname =  $( $(this).parent().parent().children()[1] ).text();
    		Project.editPro(projectname);
    		$("a#ts").on("click" ,function() {
    			NaviBar.reset("#ts");
    			Project.editPro(projectname);
    		});
    	});
    	
    	$("[id='project_delall']").on("click" , function(e) {
    		var projects = Checkbox.getSelect("prc");
    		Project.delPro(projects);
    	});
    }
	
	//查询项目
	 this.getProject = function (page, callback){
    	if(typeof page == 'undefined') {
		    page = 1;
	    }
	    var url = path+"/admin/project/";
        var scriptTemplate = Handlebars.compile($('#taskTemplate').text());

	    // ajax翻页请求
	    $.ajax({
		    type : "GET",
		    url : url,
		    data : "page="+ page,
		    success : function(data) {
			    if(data.code == '100') {
                    var str = '' ;
                    
                    var pagecunt = parseInt(data.description)%9>0? parseInt(data.description/9)+1:parseInt(data.description/9);
				    if(pagecunt > 1){
	    	   		    if(page==1){
	    	   			    str += " <span class='disabled'><< prev</span>";
	    	   		    }else{
	    	   			    str += " <a style='cursor:pointer;' onclick='prev("+ (page-1)+ ")')><< prev</a>";
	    	   		    }
	    	   		    
		       		    for (var k = 1; k <= pagecunt; k++) { 
		       			    if(k==page){
		       				    str += "<span class='current'>" + k + "</span>";
		       			    }else{
		       				    str += "<a onclick='getProject(" + k +  ")'>" + k + "</a>";
		       			    }
		       		    }
		       		    if(page == pagecunt){
		       			    str += "<span class='disabled'>next >></span>";
		       		    }else{
		       			    str += "<a style='cursor:pointer;' onclick='next("+ (page+1)+ ")'>next >></a>";
		       		    }
                    }
                    
                    var wrapper = { items: eval(data.restring),totalNum: data.description, paging:str ,page:page,path:path };
                    var rs = scriptTemplate( wrapper );
                    $('#showProject').html(rs);
                    $("input[id='prc_detail']").remove();
                    $('#prc_checkbox').on('click', function (){
//						amplify.publish("project.checkbox", {"button":this, "table":'#prc_tab'})
						Checkbox.select(this, '#prc_tab');
					})
					
					_processProDom();
                    if(callback) {
                    	callback();	
                    }
			    }
                
		    }
	    });
    };
    
    return{
    	getProject : getProject
    }
});