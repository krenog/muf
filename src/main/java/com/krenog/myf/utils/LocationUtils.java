package com.krenog.myf.utils;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.util.GeometricShapeFactory;

public class LocationUtils {
    public static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();//static заранее

    public static Point buildPoint(Float latitude, Float longitude) {
        Coordinate c1 = new Coordinate(latitude, longitude);
        Point point = GEOMETRY_FACTORY.createPoint(c1);
        return point;
    }

    public static Polygon buildPolygon(Float latitude, Float longitude,Float delta) {
        GeometricShapeFactory shapeFactory = new GeometricShapeFactory();
        shapeFactory.setNumPoints(32);
        shapeFactory.setCentre(new Coordinate(latitude, longitude));
        shapeFactory.setSize(delta);
        Polygon polygon = shapeFactory.createCircle();
        return polygon;
    }
}
