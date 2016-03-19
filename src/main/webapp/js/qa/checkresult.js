define(['../common/polygonList'],function( PolygonList ) {
	var CheckResult = {
		polylines : PolygonList,
		remark : '',
		task_item_id : '',
		toString : function() {
			return {
				remark : this.remark,
				task_item_id : this.task_item_id,
				path : function() {
					var array = CheckResult.polylines.getAll();
					var rs = [];
					for (var i = 0; i < array.length; i++) {
						if ($.inArray(array[i].getPath().toString(), rs) < 0) {
							rs.push(array[i].getPath().toString());
						}
					}
					return rs;
				}()
			}
		}
	};

	amplify.subscribe('task.begin.edit', function(data) {
		CheckResult.task_item_id = data.taskitemid;
	});
	return CheckResult;
})