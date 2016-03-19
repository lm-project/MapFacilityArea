define(function($) {
	var google = null, baidu = null, qq = null,img = null;// 叠加地图参考对象
	console.log("加载map/refernceLayer,订阅map.google，map.baidu，map.qq，map.img");
	(function() {
		amplify.subscribe('map.google',function() {
			initGoogle();
		});
		
		amplify.subscribe('map.baidu',function() {
			initBD();
		});
		
		amplify.subscribe('map.qq',function() {
			initQQ();
		});
		amplify.subscribe('map.img',function() {
			initImage();
		});
		amplify.subscribe('map.remove',function() {
			removeLayers();
		});
	}());
	
	function removeLayers(){
		if (baidu != null) {
			baidu.setMap(null);
		}
		if (qq != null) {
			qq.setMap(null);
		}
		if (img != null) {
			img.setMap(null);
		}
		if (google != null) {
			google.setMap(null);
		}
	}
	function initGoogle() {
		removeLayers();
		google = new AMap.TileLayer(
				{
					tileUrl : 'http://mt{1,2,3,0}.google.cn/vt/lyrs=m@142&hl=zh-CN&gl=cn&x=[x]&y=[y]&z=[z]&s=Galil'
				});
		google.setMap(mapObj);
	}
	var baseLayer = null;
	function initBD() {
		removeLayers();
		var b_h = [ 0, 0, 1, 3, 6, 12, 24, 49, 98, 197, 395, 790,
				1581, 3163, 6327, 12654, 25308, 50617 ];
		var g_h = [ 0, 1, 3, 6, 13, 26, 52, 105, 210, 421, 843,
				1685, 3372, 6744, 13489, 26978, 53957, 107917 ];
		var b_l = [ 0, 0, 0, 1, 2, 4, 9, 18, 36, 73, 147, 294, 589,
				1178, 2356, 4712, 9425, 18851 ];
		var g_l = [ 0, 0, 1, 2, 6, 12, 24, 48, 97, 194, 387, 776,
				1551, 3103, 6207, 12416, 24832, 49661 ];
		baidu = new AMap.TileLayer( {
			getTileUrl : function(x, y, z) { // 自定义取图规则
				var s = [ 1, 2, 3 ];
				var t = [];
				t.push('http://online'+ s[Math.floor(Math.random() * s.length)]
						+ '.map.bdimg.com/tile/?qt=tile');
				t.push('x=' + (x - g_h[z] + b_h[z]));
				t.push('y=' + (-y + b_l[z] + g_l[z]));
				t.push('z=' + (z + 1));
				t.push('styles=pl');
				return t.join('&');
			}
		});
		baidu.setMap(mapObj);
	}
		
	function initQQ() {
		removeLayers();
		var scopes = new Array(0, 0, 0, 0, 0, 3, 0, 3, 0, 3, 0, 3,
				0, 7, 0, 7, 0, 15, 0, 15, 0, 31, 0, 31, 0, 63, 4,
				59, 0, 127, 12, 115, 0, 225, 28, 227, 356, 455,
				150, 259, 720, 899, 320, 469, 1440, 1799, 650, 929,
				2880, 3589, 1200, 2069, 5760, 7179, 2550, 3709,
				11520, 14349, 5100, 7999, 23060, 28689, 10710,
				15429, 46120, 57369, 20290, 29849, 89990, 124729,
				41430, 60689, 184228, 229827, 84169, 128886);
		qq = new AMap.TileLayer( {
			getTileUrl : function(x, y, z) { // 自定义取图规则
				var s = [ 1, 2, 3 ];
				var t = 'http://p'
						+ s[Math.floor(Math.random() * s.length)]
						+ '.map.gtimg.com/maptilesv2/';
				var f = z * 4;
				var i = scopes[f++];
				var j = scopes[f++];
				var l = scopes[f++];
				var scope = scopes[f];
				if (x >= i && y <= j && y >= l && y <= scope) {
					y = Math.pow(2, z) - 1 - y;
					var tileNo = z + '/' + Math.floor(x / 16) + '/'
							+ Math.floor(y / 16) + '/' + x + '_'
							+ y + '.png';
					t += tileNo;
				}
				return t;
			}
		});

		qq.setMap(mapObj);
	}
	
	function initImage(){
		removeLayers();
		
		var width = mapObj.getSize().getWidth();
		var height = mapObj.getSize().getHeight();
		var boundes = mapObj.getBounds();
		var southWestLngLat = boundes.getSouthWest();
		var northEastLngLat = boundes.getNorthEast();
		console.log("高德地图容器像素大小："+width+","+height);
		console.log("高德地图视野范围："+southWestLngLat+","+northEastLngLat);
		console.log('http://10.13.4.39:8080/twms/wms?LAYERS=basic&SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&STYLES=&FORMAT=image%2Fjpeg&SRS=EPSG%3A4326&BBOX='+southWestLngLat+","+northEastLngLat+'&WIDTH='+width+'&HEIGHT='+height);
		var pex1 = lngLatToContainer({lnglat:southWestLngLat,level:mapObj.getZoom()});
		var pex2 = lngLatToContainer({lnglat:northEastLngLat,level:mapObj.getZoom()});
		width = pex1.getX() - pex2.getX() ;
		height = pex1.getY() - pex2.getY() ;
		img = new AMap.TileLayer({
				tileUrl: 'http://10.13.4.39:8080/twms/wms?LAYERS=basic&SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&STYLES=&FORMAT=image%2Fjpeg&SRS=EPSG%3A4326&BBOX=116.27094355946,39.922819343128,116.4446219576,40.00405808977&WIDTH='+width+'&HEIGHT='+height,
		});
		img.setMap(mapObj);
//		img = new OpenLayers.Map("map", {
//			numZoomLevels : 19
//		});
//		
//		baseLayer = new OpenLayers.Layer.WMS('IMG-001',
//				'http://10.13.4.39:8080/twms/wms', {
//					layers : 'IMG-001'
//				}, {
//					isBaseLayer : true
//		});
//		img.addLayers([baseLayer]);
//		img.setCenter(new OpenLayers.LonLat(mapObj.getCenter()));
//		img.zoomTo(mapObj.getZoom());
	}
}(jQuery));