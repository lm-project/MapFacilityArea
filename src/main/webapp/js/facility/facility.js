function Fck(gid, fa, fa_id, name_chn, mesh, poi_id, disp_class, fa_flag, area_flag, polygon, taskitem_id, fa_type, precision, sources, proname, fatype_id) {
		console.log("加载facility/facility，创建facility对象（）");
		this.gid = gid;
		this.fa = fa ;
		this.fa_id = fa_id;
		this.name_chn =name_chn;
		this.mesh = mesh;
		this.poi_id = poi_id;
		this.disp_class = disp_class ;
		this.fa_flag = fa_flag ;
		this.area_flag = area_flag;
		this.polygon = polygon;
		this.taskitem_id = taskitem_id;
		this.fa_type = fa_type;
		this.precision = precision;
		this.sources = sources;
		this.proname = proname;
		this.fatype_id = fatype_id;
}