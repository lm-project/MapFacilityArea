package com.autonavi.mapart.util;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Polygon;

public class InteriorPointAreaCustom {

	private static double avg(double a, double b) {
		return (a + b) / 2.0;
	}

	private GeometryFactory factory;
	private Coordinate interiorPoint = null;
	private double maxWidth = 0.0;
	private String xyType="Y";

	public InteriorPointAreaCustom(Geometry g, String type) {
		factory = g.getFactory();
		xyType = type;
		add(g);
	}

	public Coordinate getInteriorPoint() {
		return interiorPoint;
	}
	
	private void add(Geometry geom) {
		if (geom instanceof Polygon) {
			addPolygon(geom);
		} else if (geom instanceof GeometryCollection) {
			GeometryCollection gc = (GeometryCollection) geom;
			for (int i = 0; i < gc.getNumGeometries(); i++) {
				add(gc.getGeometryN(i));
			}
		}
	}

	public void addPolygon(Geometry geometry) {
		LineString bisector;
		if ("Y".equals(xyType)) {
			bisector = horizontalBisector(geometry);
		} else {
			bisector = verticalBisector(geometry);
		}
		Geometry intersections = bisector.intersection(geometry);
		Geometry widestIntersection = widestGeometry(intersections);

		double width;
		if ("Y".equals(xyType)) {
			width = widestIntersection.getEnvelopeInternal().getWidth();
		} else {
			width = widestIntersection.getEnvelopeInternal().getHeight();
		}
		if (interiorPoint == null || width > maxWidth) {
			interiorPoint = centre(widestIntersection.getEnvelopeInternal());
			maxWidth = width;
		}
	}

	protected Geometry widestGeometry(Geometry geometry) {
		if (!(geometry instanceof GeometryCollection)) {
			return geometry;
		}
		return widestGeometry((GeometryCollection) geometry);
	}

	private Geometry widestGeometry(GeometryCollection gc) {
		if (gc.isEmpty()) {
			return gc;
		}
		Geometry widestGeometry = gc.getGeometryN(0);
		for (int i = 1; i < gc.getNumGeometries(); i++) { // Start at 1
			Geometry tmpGeometry = gc.getGeometryN(i);
			double w1, w2;
			if ("Y".equals(xyType)) {
				w1 = gc.getGeometryN(i).getEnvelopeInternal().getWidth();
				w2 = widestGeometry.getEnvelopeInternal().getWidth();
			} else {
				w1 = gc.getGeometryN(i).getEnvelopeInternal().getHeight();
				w2 = widestGeometry.getEnvelopeInternal().getHeight();
			}
			if (w1 > w2) {
				widestGeometry = tmpGeometry;
			}
		}
		return widestGeometry;
	}

	protected LineString verticalBisector(Geometry geometry) {
		Envelope envelope = geometry.getEnvelopeInternal();
		// Assert: for areas, minx <> maxx
		double avgX = avg(envelope.getMinX(), envelope.getMaxX());
		return factory.createLineString(new Coordinate[] {
				new Coordinate(avgX, envelope.getMinY()),
				new Coordinate(avgX, envelope.getMaxY()) });
	}

	protected LineString horizontalBisector(Geometry geometry) {
		Envelope envelope = geometry.getEnvelopeInternal();
		// Assert: for areas, minx <> maxx
		double avgY = avg(envelope.getMinY(), envelope.getMaxY());
		return factory.createLineString(new Coordinate[] {
				new Coordinate(envelope.getMinX(), avgY),
				new Coordinate(envelope.getMaxX(), avgY) });
	}

	public Coordinate centre(Envelope envelope) {
		return new Coordinate(avg(envelope.getMinX(), envelope.getMaxX()), avg(
				envelope.getMinY(), envelope.getMaxY()));
	}

}
