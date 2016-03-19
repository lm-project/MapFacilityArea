/**
 * 项目列表
 */
define(['user/editUser'], function( User ){
	
	$("#showAllUser").on("click" ,function() {
		NaviBar.reset("#main_a");
		NaviBar.add(this, 'user');
		var main = 	
         '<div id="showUser" class="module_content" >' 
        + '</div>';
		$('#main').html(main);
		$("#taskTemplate").load(path+"/js/template/allUser.htm",function(){
			getUserData();
		});
	});
	
	amplify.subscribe("user.loaded",function(data) {
        initUserEvent();
    });
	
	amplify.subscribe("user.selectall",function(data) {
    	Checkbox.select(data.button, data.table);
    });
	
	function initUserEvent(){
		
		$('#searchrole').change(function() {
			getUserData(1, $(this).val());
		});
		
		$('#user_delall').click(function() {
			var v='';
			$('#tab1 input[type=checkbox]:checked').each(function(){
			    v+=$(this).val()+",";    
			});    
			User.delUser(v.substring(0,v.length-1));
		});
		
		$("[id='user_edit']").on("click" , function(e) {
    		var uid = this.value;
    		User.editUser(uid);
    	});

    	$("[id='user_del']").on("click" , function(e) {
    		var uid = this.value;
    		User.delUser(uid);
    	});
    	
    	$('.column').equalHeight();
	};
	
	//上一页
	this.prevUserData = function(page, role, truename){
		var curPage = $("span.current").html();
		if(parseInt(curPage)>1){
			getUserData(parseInt(curPage)-1, role, truename);
		}
	};
	//下一页
	this.nextUserData = function(page, role, truename){
		var curPage = $("span.current").html();
		if(parseInt(curPage)<parseInt(page)){
			getUserData(parseInt(curPage)+1, role, truename);
		}
	};
	
	this.getUserData = function(page, role, truename){
		if(typeof page == 'undefined') {
			page = 1;
		}
		if(typeof role == 'undefined') {
			role = 0;
		}
		if(typeof truename == 'undefined') {
			truename = '';
		}
		var scriptTemplate = Handlebars.compile($('#taskTemplate').text());
		// ajax翻页请求
		$.ajax({
			type : "POST",
			url : path+"/admin/user",
			data : "page="+page+"&role="+role+"&truename="+truename,
			success : function(data) {
				if(data.code == '100') {
                    var pagecunt = parseInt(data.description)%9>0? parseInt(data.description/9)+1:parseInt(data.description/9);
                    var str = '' ;
                    if(pagecunt > 1){
                    	
	    	   		    if(page==1){
	    	   				str += " <span class='disabled'><< prev</span>";
	    	   			}else{
	    	   				str += " <a style='cursor:pointer;' onclick='prevUserData("+pagecunt+", "+ role + (truename==''?'':","+truename)+ ")')><< prev</a>";
	    	   			}
	    	   		    for (var k = 1; k <= pagecunt; k++) { 
		       				if(k==page){
		       					str += "<span class='current'>" + k + "</span>";
		       				}else{
		       					str += "<a onclick='getUserData(" + k + ","+ role + (truename==''?'':","+truename)+ ")'>" + k + "</a>";
		       				}
		       				
		       			}
	
		       		    if(page == pagecunt){
		       				str += "<span class='disabled'>next >></span>";
		       			}else{
		       				str += "<a style='cursor:pointer;' onclick='nextUserData("+pagecunt+", "+ role + (truename==''?'':","+truename)+ ")'>next >></a>";
		       			}
                    }
				    
                    var wrapper = { items: eval(data.restring),totalNum: data.description, paging:str ,page:page,path:path };
                    var rs = scriptTemplate( wrapper );

                    $('#showUser').html(rs);
                    
                    $('#user_selectall').on('click', function (){
						amplify.publish("user.selectall", {"button":this, "table":'#user_tab'})
					})
				}
				amplify.publish("user.loaded" , {});
			}
		});
	};
	
	
});

	
	
	this.editUser = function(v){
		$.get(
			path+"/admin/editu/"+v,
			function(data,status) {
	    		$("#taskTemplate").load(path+"/js/template/editUser.htm",function(){
	    			
	    			var scriptTemplate = Handlebars.compile($('#taskTemplate').text());
	    			console.log(eval(data.restring));
	    			var rs = scriptTemplate( eval(data.restring)[0] );

                    $('#main').html(rs);
                    setTimeout(100);
                    bindEditUserButton();
	    		});
			}
		);
	};
	
	this.delUser = function(v){
		if(v.indexOf('/')){
			v = v.substring(v.indexOf('/')+1);
		}
		if(confirm("是否删除选中用户?")){
			// ajax删除请求
			$.ajax({
				type : "POST",
				url : path+"/admin/delu",
				data : "uids="+v,
				success : function(data) {
					if(data.code == '100') {
						alert(data.description);
						//window.location.href='allu';
						getUserData();
						/*for(i = 0; i< v.length; i++) {
							$("#tab1 input[value="+v[i]+"]:checked").parent().parent().remove();
						}*/
					}
				}
			});
		}
	};