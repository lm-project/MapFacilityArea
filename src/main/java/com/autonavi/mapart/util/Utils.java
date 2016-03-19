package com.autonavi.mapart.util;

import org.apache.log4j.Logger;

import com.vividsolutions.jts.algorithm.Angle;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.operation.distance.DistanceOp;

public class Utils {
	private static Logger log  = Logger.getLogger("Utils");

	/**
	 * 检测多边形是否符合标准
	 * @param polygon
	 * @return
	 * @author shen.tao
	 */
	public static String checkPolygon(String geom){
		WKTReader wkt = new WKTReader();
		try{
			/**计算出多边形所有的边（线段）数，按每2个点谋算为一个线段
			 * 注意：可能会有多边形内部有孔的情况（多边形内部有区域不属于多边形）这里需要
			 * 修改对应的逻辑来获取多边形准确的线段数
			 * 当前获取geom串的格式为：116.340706,39.985958;116.342755,39.98609;116.344077,39.986133; ... ;116.340706,39.985958
			 */
			log.debug("geom ====:"+geom);
			String[] geomArr = geom.split(";");
			if(!geomArr[0].equals(geomArr[geomArr.length-1])){
				geom = geom+";"+geomArr[0];
			}
			StringBuilder geomStr = new StringBuilder("POLYGON((");
			geomStr.append(geom.replaceAll(",", " ").replaceAll(";", ",")).append("))");
			Polygon polygon = (Polygon) wkt.read(geomStr.toString());
			Coordinate[] cos = polygon.getCoordinates();
			LineString[] lis = new LineString[cos.length-1];
			for(int i=0;i<cos.length-1;){
				lis[i] = (LineString)wkt.read("LINESTRING("+cos[i].x+" "+cos[i].y+","+cos[++i].x+" "+cos[i].y+")");
				System.out.println(i+"--->"+lis[i-1].toString());
			}
			String checkOk = Constants.MSG_100;
			if(isPolygonCross(lis)) {
				checkOk += Constants.MSG_201;
			}
			if(!isPolygonPoiLineDistanceOk(lis)) {
				checkOk += Constants.MSG_202;
			}
			if(!isPolygonPoiDistanceOK(cos)) {
				checkOk += Constants.MSG_203;
			}  
			if(!isPolygonIncludedAngleOK(lis)){
				checkOk += Constants.MSG_204;
			}  
			return checkOk;
		}catch(Exception e){
			e.printStackTrace();
			log.error("检测多边形异常 ："+e.getMessage());
			return Constants.MSG_000;
		}
	}
	
	/**
	 * 检测多边形是否有自交叉的情况（多边形的边相互交叉）
	 * @param lis
	 * @return
	 * @author shen.tao
	 */
	private static boolean isPolygonCross(LineString[] lis){
		try{
			for(int i=0;i<lis.length-1;i++){
				for(int j=i+1;j<lis.length;j++){
					//System.out.println((icount++)+" -> Deal line:"+i+" , line:"+j);
					if(lis[i].crosses(lis[j])){
						System.out.println("--------------------->crosses :: line{"+i+","+lis[i]+"},line{"+j+","+lis[j]+"}");
						return true;
					}
				}			
			}
		}catch(Exception e){
			log.error("检测多边形自交叉异常 ："+e.getMessage());	
			return true;
		}
		return false;
	}
	
	/**
	 * 检测多边形任意两个形状点（顶点）间的距离是否大于0.015秒
	 * @param cos
	 * @return
	 * @author shen.tao
	 */
	private static boolean isPolygonPoiDistanceOK(Coordinate[] cos){
		try{
			for(int i=0;i<cos.length-2;i++){
				for(int j=i+1;j<cos.length-1;j++){
					double tmpDis = cos[i].distance(cos[j])*3600;
					//System.out.println((icount++)+" -> Deal point:"+i+" , point:"+j+"  "+String.format("%.3f",tmpDis));
					if(tmpDis < 0.015){
						System.out.println("--------------------->distance("+tmpDis+")<0.015 :: point{"+i+",("+cos[i].x+" "+cos[i].y+")},point{"+j+",("+cos[j].x+" "+cos[j].y+")}");
						return false;
					}
				}
			}
		}catch(Exception e){
			log.error("检测多边形两点间距异常 ："+e.getMessage());	
			return false;
		}
		return true;
	}
	
	/**
	 * 检测多边形相邻两边夹角是否大于1度
	 * 取相邻两条线段的三个顶点进行角度计算
	 * @param lis
	 * @return
	 * @author shen.tao
	 */
	private static boolean isPolygonIncludedAngleOK(LineString[] lis){
		try{
			double tmpAngle;
			for(int i=0;i<lis.length-1;){
				tmpAngle = Angle.toDegrees(Angle.angleBetween(lis[i].getCoordinates()[0], lis[i].getCoordinates()[1], lis[++i].getCoordinates()[1]));
				//System.out.println((icount++)+" ::"+lis[i-1].getCoordinates()[0].x+","+lis[i-1].getCoordinates()[1].x+" - "+lis[i].getCoordinates()[0].x+","+lis[i].getCoordinates()[1].x+" -> "+tmpAngle);
				if(tmpAngle < 1){
					System.out.println("--------------------->angle("+tmpAngle+")<1 :: line{"+i+","+lis[i-1]+"},line{"+(i-1)+","+lis[i]+"}");
					return false;
				}
			}
			//最后一条线段与第一条线段进行计算
			tmpAngle = Angle.toDegrees(Angle.angleBetween(lis[lis.length-1].getCoordinates()[0], lis[lis.length-1].getCoordinates()[1], lis[0].getCoordinates()[1]));
			//System.out.println((icount++)+" ::"+lis[lis.length-1].getCoordinates()[0].x+","+lis[lis.length-1].getCoordinates()[1].x+" - "+lis[0].getCoordinates()[0].x+","+lis[0].getCoordinates()[1].x+" -> "+tmpAngle);
			if(tmpAngle < 1){
				System.out.println("--------------------->angle("+tmpAngle+")<1 :: line{"+(lis.length-1)+","+lis[lis.length-1]+"},line{"+0+","+lis[0]+"}");
				return false;
			}
		}catch(Exception e){
			log.error("检测多边形夹角异常 ："+e.getMessage());	
			return false;
		}
		return true;
	}

	/**
	 * 检测多边形形状点到其它形状点构成线段的最短距离是否大于0.015秒，
	 * 若垂直距离点不在线段上，则合理不用过滤，否则需再判断距离值。
	 * 业务逻辑：先计算点P到线段X（由点A和点B构成）的最近距离点P2，点P2的位置有三种可能：等于点A、
	 * 等于点B和垂直于X的点C，经过判断构成夹角的另一点P3，这样计算出由P、P2和P3构成的夹角，若不等于90度
	 * 则表示合理，否则即计算点P到X的垂直距离是否大于0.015秒
	 * @param geom
	 * @return
	 */
	private static boolean isPolygonPoiLineDistanceOk(LineString[] lis){
		try{
			double tmpAngle;
			//每个点会影响两条线段，这两条线段不用计算
			for(int i=0;i<lis.length;i++){
				for(int j=0;j<lis.length;j++){
					//过滤掉与选取的点相关的两条边线段
					if(j==((i-1)<0?lis.length-1:(i-1)) || j==i) continue;
					//System.out.println((icount++)+" :: point:"+i+" line:"+j);
					Point curPoi = new GeometryFactory().createPoint(lis[i].getCoordinates()[0]);		
					DistanceOp dop = new DistanceOp(curPoi, lis[j]);
					Coordinate tmpPoi = new Coordinate(dop.nearestPoints()[1].x,dop.nearestPoints()[1].y);
					Coordinate othPoi = lis[j].getCoordinates()[0];
					if(tmpPoi.equals2D(othPoi)){
						othPoi = lis[j].getCoordinates()[1];
					}
					tmpAngle = Angle.toDegrees(Angle.angleBetween(lis[i].getCoordinates()[0], tmpPoi, othPoi));
					//System.out.println("--->"+curPoi.getX()+","+curPoi.getY()+"   "+tmpPoi.x+","+tmpPoi.y+"   "+othPoi.x+","+othPoi.y+"  angle="+tmpAngle);
					if(Math.floor(tmpAngle) == 90){
						if((dop.distance()*3600) < 0.015){
							System.out.println("--------------------->angle=90&dis("+(dop.distance()*3600)+")<0.015 :: point{"+i+","+curPoi+"},line{"+j+","+lis[j]+"}");
							return false;
						}
					}
				}
			}
		}catch(Exception e){
			log.error("检测多边形点到边异常 ："+e.getMessage());	
			return false;
		}
		return true;
	}
	
	/**
	 * 计算区域多边形面积
	 * 说明：凸包(convexhull)是指一个最小凸多边形，满足节点集中的点或在多边形边上或在其内)
	 * 一般来说凸包所覆盖的区域要大于多边形的实际面积可参见http://www.cnblogs.com/yxsylyh/archive/2010/05/03/UsingConvexHull.html
	 * 文章中通过代码生成的凸包，若按实际连接上所有的点，则实际面板小于凸包面板
	 * 所以计算多边形面积要用实际多边形对象来计算，不能用凸包；
	 * 对于计算中心（质心）需要用实际多边形对象还是用凸包来计算目前不明确，等实际确认？
	 * @param geom
	 * @return
	 */
	public static double caculateArea(String geom){
		//计算设施区域多边形面积
		try {
			MultiPolygon mPolygon = getMultiPolygon(geom);
			if(null == mPolygon){
				return 0.0;
			}
			log.debug("多边形面积=("+mPolygon.getArea()+")");
			return  Double.parseDouble(String.format("%.6f", mPolygon.getArea()*26.5*26.5));
		} catch (Exception e) {
			log.error("计算多边形面积异常："+e.getMessage());
			return 0.0;
		}
	}
	
	/**
	 * 计算中心点坐标
	 * @return 
	 */
	public static String caculateCenter(String geom){
		log.debug("计算中心点坐标。。。。。。。。。。");
		double[] centroId = new double[2];
		try {
			MultiPolygon mPolygon = getMultiPolygon(geom);
			//计算设施区域多边形中心点坐标
			centroId = new double[]{mPolygon.getCentroid().getX(),mPolygon.getCentroid().getY()};
			log.debug("多边形质心=("+centroId[0]+","+centroId[1]+")");
			//计算中心点坐标是否在区域多边形内
			Point centroPoint = getCentroidInPolygon(centroId);
			log.debug("质心是否在多边形内=("+mPolygon.contains(centroPoint)+")");
			if(!mPolygon.contains(centroPoint)){
				//中心点不在区域多边形内的处理方法
				centroId = getSpecialPolygonCentroid(mPolygon, centroPoint);
			}  
		} catch (Exception e) {
			log.error("计算中心点坐标的异常：  "+e.getMessage());
		}
		
		return String.format("%.6f", centroId[0]/3600)+","+String.format("%.6f", centroId[1]/3600);
	}
	/**
	 * 获取多边形对象
	 * @param geom
	 * @return
	 */
	private static MultiPolygon getMultiPolygon(String geom)throws Exception{
		WKTReader wReader     = new WKTReader();
		MultiPolygon mPolygon = (MultiPolygon)wReader.read(geom);
		return mPolygon;
	}
	
	/**
	 * 计算几何中心点
	 * 在多边形中：是规则多边形
	 * 不在多边形中：是特殊多边形，例如“广”、“L”形多边形
	 * @return
	 */
	private static Point getCentroidInPolygon(double[] centroId)throws Exception{
		Coordinate coordinate = new Coordinate(centroId[0],centroId[1]);
		return new GeometryFactory().createPoint(coordinate);
	}
	
	/**
	 * 计算特殊多边形的几何中心点
	 * 取点方法：将其中心点坐标移至距离最近的多边形面内3米位置
	 * @return
	 */
	private static double[] getSpecialPolygonCentroid(MultiPolygon mPolygon,Point centroPoint){
		//1,算出多边形与质心最近距离点，用于确证最终中心点的方向
		DistanceOp dop = new DistanceOp(mPolygon, centroPoint);
		log.debug("多边形与质心最近距离=("+dop.distance()+")");
		log.debug("多边形与质心最近距离点=("+dop.nearestPoints()[0].x+","+dop.nearestPoints()[0].y+")");//最近点
		
		//2,算出X轴向和Y轴向的平分中心点 
		InteriorPointAreaCustom xCentroPoint = new InteriorPointAreaCustom(mPolygon,"X");
		log.debug("多边形X轴平分中心点=("+xCentroPoint.getInteriorPoint().x+","+xCentroPoint.getInteriorPoint().y+")");
		InteriorPointAreaCustom yCentroPoint = new InteriorPointAreaCustom(mPolygon,"Y");
		log.debug("多边形Y轴平分中心点=("+yCentroPoint.getInteriorPoint().x+","+yCentroPoint.getInteriorPoint().y+")");
	
		//3,算出质心与最近点坐标差的绝对值，结果值大的代表偏差大，即代表取最终中心点的方向
		double xTemp = Math.abs(dop.nearestPoints()[1].x-dop.nearestPoints()[0].x);
		double yTemp = Math.abs(dop.nearestPoints()[1].y-dop.nearestPoints()[0].y);
		log.debug("X轴向相减绝对值("+xTemp+") <= Y轴向相减绝对值("+yTemp+") ? "+(xTemp<yTemp));
		if(xTemp <= yTemp){
			log.debug("最终中心点取X轴向=("+xCentroPoint.getInteriorPoint().x+","+xCentroPoint.getInteriorPoint().y+")");
			return new double[]{xCentroPoint.getInteriorPoint().x,xCentroPoint.getInteriorPoint().y};
		}else{
			log.debug("最终中心点取Y轴向=("+yCentroPoint.getInteriorPoint().x+","+yCentroPoint.getInteriorPoint().y+")");
			return new double[]{yCentroPoint.getInteriorPoint().x,yCentroPoint.getInteriorPoint().y};
		}
	}
	
	/**
	 * 计算多边形的质心，对于形状规则的物体质心就是其几何中心
	 * @return
	 */
	private static double[] getPolygonCentroid(MultiPolygon mPolygon)throws Exception{
		//1,直接根据多边形对象获取质心
		double X = mPolygon.getCentroid().getX();
		double Y = mPolygon.getCentroid().getY();
		log.debug("区域多边形实际中心:("+X+","+Y+")");
		//2,根据多边形得到凸包对象再获取质心
		//Geometry conHull = mPolygon.convexHull();
		//double XX = conHull.getCentroid().getX();
		//double YY = conHull.getCentroid().getY();
		//System.out.println("区域多边形凸包中心:("+XX+","+YY+")");
		return new double[]{X,Y};
	}
	
//	public static void completeFacilityInfo(int type,Facilityachieve fac){
//	try{
//		//设置condition和release_status值(删除，修改，新增都属于发布状态的新增状态)
//		fac.setCondition(type);
//		fac.setRelease_status("11");		
//		//调用X平台接口获取x_id值
//		String meshPoi = fac.getMesh();
//		long poiId     = fac.getPoi_id();
//		fac.setX_id(getXid(meshPoi, poiId));
//		
//		
//		//计算设施区域多边形面积
//		MultiPolygon mPolygon = getMultiPolygon(fac.getGeom());
//		if(null == mPolygon){
//			System.out.println("根据多边形节点获取面对象失败");
//			fac.setArea(-1);
//			fac.setX_coord(0);
//			fac.setY_coord(0);
//			return;
//		}
//		fac.setArea(mPolygon.getArea());
//		System.out.println("多边形面积=("+fac.getArea()+")");
//		//计算设施区域多边形中心点坐标
//		double[] centroId = new double[]{mPolygon.getCentroid().getX(),mPolygon.getCentroid().getY()};
//		System.out.println("多边形质心=("+centroId[0]+","+centroId[1]+")");
//		//计算中心点坐标是否在区域多边形内
//		Point centroPoint = getCentroidInPolygon(centroId);
//		if(!mPolygon.contains(centroPoint)){
//			//中心点不在区域多边形内的处理方法
//			System.out.println("质心是否在多边形内=("+mPolygon.contains(centroPoint)+")");
//			centroId = getSpecialPolygonCentroid(mPolygon, centroPoint);
//		}  
//		fac.setX_coord(centroId[0]);
//		fac.setY_coord(centroId[1]);
//	}catch(Exception e){
//		e.printStackTrace();
//	}
}
