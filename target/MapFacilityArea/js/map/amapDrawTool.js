/**
 * 画图工具
 */
define(function($) {
        amplify.subscribe( "task.begin.edit" , function(data) {
        	console.log("显示绘图工具和参考地图 ");
            $("#map_satellite_button").css("display", "block");
        });
        amplify.subscribe( "task.end.edit" ,function(data) {
        	$("#map_satellite_button").css("display", "none");
        });
        console.log("加载map/amapDrawTool,并且订阅task.begin.edit和task.end.edit使之显示隐藏绘图和参考地图");
}(jQuery))