package com.autonavi.mapart.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.autonavi.mapart.entity.Facilityachieve;
import com.autonavi.mapart.entity.Qaresult;

public class ExecuteMultiPolygon {
	private static final String s2 = "\\d+[.]?\\d+";
	private static final Pattern  pattern=Pattern.compile(s2);  
	private static Matcher  ma= null;  
	private static Log log = LogFactory.getLog(ExecuteMultiPolygon.class);
	
	public static String execute(String bounds){
		int i = 0;
		double[] d = new double[4];
		ma = pattern.matcher(bounds);
		while(ma.find()){  
			d[i++] = Double.valueOf(ma.group())*3600;  
	    }
        if( d.length < 4 ) {
            return "";
        }
		return "POLYGON(("+d[0]+" "+d[3]+","+d[2]+" "+d[3]+","+d[2]+" "+d[1]+","+d[0]+" "+d[1]+","+d[0]+" "+d[3]+"))";
	}
	
	public static List<Facilityachieve>  execute(List<Facilityachieve> geoms) {
		List<Facilityachieve> p = new ArrayList<Facilityachieve>();
		for (int i = 0; i < geoms.size(); i++) {
			Facilityachieve fac = geoms.get(i);
			String lng_lat = fac.getLng_lat();
			String lngLatStr = (lng_lat==null || !StringUtils.isNotBlank(lng_lat)) ? 
									getSecondGeometry( fac.getGeom(),false) : lng_lat;
			fac.setGeom(lngLatStr);
			p.add(fac);
		}
		return p;
	}

	public static List<Qaresult>  tranformQa(List<Qaresult> geoms) {
		List<Qaresult> p = new ArrayList<Qaresult>();
		for (int i = 0; i < geoms.size(); i++) {
			Qaresult fac = geoms.get(i);
			String polygon = fac.getQamark();
			fac.setQamark(getSecondGeometry(polygon,true));
			p.add(fac);
		}
		return p;
	}
    
	public static String getSecondGeometry(String polygon, boolean isLine) {
		StringBuffer secondGeom = new StringBuffer();
		log.info("转换polygon："+polygon);
		if (isLine) {
			ma = pattern.matcher( polygon );
		} else {
			ma = pattern.matcher(polygon.substring(0, polygon.lastIndexOf(",")));
		}
		while(ma.find()){  
			secondGeom.append(Double.valueOf(ma.group())/3600+",") ;  
		}
		return secondGeom.toString();
	}
}
