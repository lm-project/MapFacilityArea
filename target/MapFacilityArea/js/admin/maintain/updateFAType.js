/**
 * 编辑、更新fatype
 */
define([], function() {

	function initFatypeEvent() {
		$("#taskTemplate").load(path + "/js/template/fapoiRelations.htm", function() {
			getFapoiRelation();
		});

		amplify.subscribe("fatype.loaded", function(data) {
			initFatypeDomEvent(data);
		});

	};
	
	//处理dom元素
	function initFatypeDomEvent(data){
		
		$("[id='fatype_edit']").on("click" , function(e) {
			console.log("begin edit type:" + $(this).val());
			Array.prototype.slice.call($($( this ).closest('tr')).find('input.edit_type'))
					.forEach(function(e){
					    $(e).css('border','1px solid #ccc');
					    $(e).removeAttr("disabled");
					  });
			$($($( this ).closest('tr')).find('input.edit_type')).first().focus();
    	});
		
		$("[id='fatype_update']").on("click" , function(e) {
			console.log("end edit type:" + $(this).val());
			var data = {
					id : $(this).val(),
					fa_category : $($( this ).closest('tr')).find('input#fa_category').val(),
					fa_type :  $($( this ).closest('tr')).find('input#fa_type').val(),
					poi_type : $($( this ).closest('tr')).find('input#poi_type').val(),
					poi_typecode : $($( this ).closest('tr')).find('input#poi_typecode').val()	
				}
			if(confirm("确定提交更新?")){
				commitUpdate(data);
			}
			Array.prototype.slice.call($($( this ).closest('tr')).find('input.edit_type'))
					.forEach(function(e){
					    $(e).css('border','0');
					    $(e).attr("disabled",true);
					  });
    	});
	}
	
	function commitUpdate(data){
		// ajax请求
		$.ajax({
			type : "POST",
			url : path + "/common/updateFatype",
			data : "fapoiRelation="+JSON.stringify(data),
			success : function(data){
				if(data.code == 100) {
					alert("更新成功！");
				}
			}
		});
	}
	
	//获取fatype列表
	function getFapoiRelation(callback) {
		var fa_category, fa_type, poi_type;
		
		if (typeof fa_category == 'undefined') {
			fa_category = $("#facategory").val()== 0 ? "":$('#facategory').val();
		}
		if (typeof fa_type == 'undefined') {
			fa_type = $("#fatype").val()== 0 ? "":$('#fatype').val();
		}
		if (typeof poi_type == 'undefined') {
			poi_type = $("#poitype").val()== 0 ? "":$('#poitype').val();
		}

		// ajax翻页请求
		$.ajax({
			type : "POST",
			url : path + "/common/fapoirelation/",
			data : "fa_category=" + fa_category + "&fa_type=" + fa_type
					+ "&poi_type=" + poi_type,
			success : function(data) {
				if (data.code == 100) {
					var scriptTemplate = Handlebars.compile($('#taskTemplate').text());
					var wrapper = {
						items : eval(data.restring),
						path : path
					};
					var rs = scriptTemplate(wrapper);
					$("#fatype_tab").html(rs);
					$("#fatype_number").html(data.description);

					if (callback) {
						callback();
					}
				}
				amplify.publish("fatype.loaded" , name);
			}
		});
	}
	
	function addFapoiRelation(){
		var trHTML = "<tr><td></td><td><input type='text' id='fa_category' autofocus></td>" +
				"<td><input type='text' id='fa_type'></td>" +
				"<td><input type='text' id='poi_type'></td>" +
				"<td><input type='text' id='poi_typecode'></td>" +
				"<td><input type='button' value='新增' onclick='commitNew(this);'></td></tr>"
		$($("#fatype_tab").find('tbody tr:eq(0)')).before(trHTML);
	}
	
	function commitNew(e){
		var data = {
				fa_category : $($(e).closest('tr')).find('input#fa_category').val(),
				fa_type :  $($(e).closest('tr')).find('input#fa_type').val(),
				poi_type : $($(e).closest('tr')).find('input#poi_type').val(),
				poi_typecode : $($(e).closest('tr')).find('input#poi_typecode').val()	
			}
		if(confirm("确定新增?")){
			// ajax请求
			$.ajax({
				type : "POST",
				url : path + "/common/newFapoiRelation",
				data : "fapoiRelation="+JSON.stringify(data),
				success : function(data){
					if(data.code == 100) {
						alert("新增成功！");
					}
				}
			});
		}
		
	}

	return {
		initFatypeEvent : initFatypeEvent,
		getFapoiRelation: getFapoiRelation,
		addFapoiRelation: addFapoiRelation
	}
});
