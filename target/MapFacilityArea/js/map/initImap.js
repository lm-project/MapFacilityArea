define(function(){
	var imap = new OpenLayers.Map("divImgMap", {
		numZoomLevels : 19
	});
	var baseLayer = new OpenLayers.Layer.WMS('IMG-001',
			'http://10.13.4.39:8080/twms/wms', {
				layers : 'IMG-001'
			}, {
				isBaseLayer : true
			});

	imap.addLayers([baseLayer]);
	imap.setCenter(new OpenLayers.LonLat(116.404,39.91));
	imap.zoomTo(12);
	
	$("#imgZoom").html(imap.getZoom());
	imap.events.register('zoomend',imap,function(){
		$("#imgZoom").html(imap.getZoom());
	});
	return imap;
	
});
/*
MapCompare.IMaper.InitIMap=function(){
	var imap = new OpenLayers.Map("divImgMap", {
		numZoomLevels : 19
	});
	var baseLayer = new OpenLayers.Layer.WMS('IMG-001',
			'http://10.13.4.39:8080/twms/wms', {
				layers : 'IMG-001'
			}, {
				isBaseLayer : true
			});

	imap.addLayers([baseLayer]);
	imap.setCenter(new OpenLayers.LonLat(116.404,39.91));
	imap.zoomTo(12);
	MapCompare.imapObj = imap;
	
	$("#imgZoom").html(MapCompare.imapObj.getZoom());
	imap.events.register('zoomend',MapCompare.imapObj,function(){
		$("#imgZoom").html(MapCompare.imapObj.getZoom());
	});
	
	
	
}();*/