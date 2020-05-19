package com.jon.cotbeacon.service;

import com.jon.cotbeacon.cot.CursorOnTarget;
import com.jon.cotbeacon.utils.Constants;

class Point {
    double lat;
    double lon;

    Point(final double lat, final double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    Point add(Offset offset) {
        return new Point(
                (this.lat + offset.dlat) % (2.0 * Math.PI),
                (this.lon + offset.dlon) % (2.0 * Math.PI)
        );
    }

    static class Offset {
        double dlat;
        double dlon;

        Offset(double dlat, double dlon) {
            this.dlat = dlat;
            this.dlon = dlon;
        }

        Offset add(Offset that) {
            return new Offset(this.dlat + that.dlat, this.dlon + that.dlon);
        }
    }
}