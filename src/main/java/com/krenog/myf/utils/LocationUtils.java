package com.krenog.myf.utils;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public class LocationUtils {
    public static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();//static заранее

    public static Point buildPoint(Float latitude, Float longitude) {
        Coordinate c1 = new Coordinate(latitude, longitude);
        return GEOMETRY_FACTORY.createPoint(c1);
    }
}
