/**
 * 编辑Autonavi的已有数据
 */
define(["../common/pointPath"] ,function(createPoint) {
	
	amplify.subscribe("task.begin.edit", function(data) {
		console.log("订阅“task.begin.edit”，实现关联功能");
		console.log("taskstatus 0:未领取，1:已领取,2:已保存,3:已提交,4:已质检================"+data.taskstatus);
		if( data.taskstatus == 0 ) {
			console.log(" 加载已有数据 ...此任务项还没有领取，应该不可能出现 ");
	        searchRef( data.city + data.searchKey );
		} 
    });
    
	function searchRef( searchKey ){
	    $.ajax({
		    type: "post",
		    url : path + "/polygon/search/autonavi",
		    dataType: 'json',
		    data: 'searchKey=' + searchKey,
		    error: function(XMLHttpRequest, error, errorThrown){  
		    	console.log(error);
		    },
		    success: function(data){
               console.log(" autonavi search : " + searchKey);
               attachPolygon( data );
	        }
        });
    }
    
    function attachPolygon( data ) {
    	polygonEdit = new AMap.Polygon({
		    map: mapObj,
            path: createPoint( data ),
		    strokeColor: "blue",
		    strokeOpacity: 0.9,
		    strokeWeight: 3,
		    fillColor: "#f5deb3",
		    fillOpacity: 0.1,
		    zIndex: 1
	    });
    }
});