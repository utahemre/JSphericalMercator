/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.com.yeo.jsphericalmercator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.ArrayUtils;

/**
 *
 * @author yeozkaya
 */
public class SphericalMercator {

    double EPSLN = 1.0e-10;
    double D2R = Math.PI / 180;
    double R2D = 180 / Math.PI;
    // 3857 properties.
    double A = 6378137.0;
    double MAXEXTENT = 20037508.342789244;

    int size = 256;

    static Map<Integer, SphericalMercatorCache> cache = new HashMap();

    public SphericalMercator(int optionsSize) {

        if (cache.get(size) == null) {
            this.size = optionsSize;
            long innerSize = optionsSize;

            SphericalMercatorCache c = new SphericalMercatorCache();

            for (int d = 0; d < 30; d++) {
                c.getBc()[d] = (double)innerSize / 360;
                c.getCc()[d] = (double)innerSize / (2 * Math.PI);
                c.getZc()[d] = (innerSize / 2);
                c.getAc()[d] = (innerSize);
                innerSize *= 2;
            }
            cache.put(size, c);

        }
    }

    public double[] bbox(int x, int y, int zoom, boolean tms_style, String srs) {

        if (tms_style) {
            y = (int) ((Math.pow(2, zoom) - 1) - y);
        }

        double[] ll = {x * size, (+y + 1) * size};

        double[] ur = {(+x + 1) * size, y * size};

        double[] bbox = ArrayUtils.addAll(ll(ll, zoom), ll(ur, zoom));

        // If web mercator requested reproject to 3857.
        if (srs.equals("3857")) {
            return convert(bbox, "3857");
        } else {
            return bbox;
        }
    }

    public double[] convert(double[] bbox, String to) {
        if (to.equals("3857")) {
            return ArrayUtils.addAll(forward(Arrays.copyOfRange(bbox, 0, 2)), forward(Arrays.copyOfRange(bbox, 2, 4)));
        } else {
            return ArrayUtils.addAll(inverse(Arrays.copyOfRange(bbox, 0, 2)), inverse(Arrays.copyOfRange(bbox, 2, 4)));
        }
    }

    public double[] forward(double[] ll) {

        double[] xy = {
            A * ll[0] * D2R,
            A * Math.log(Math.tan((Math.PI * 0.25) + (0.5 * ll[1] * D2R)))
        };

        if (xy[0] > MAXEXTENT) {
            xy[0] = MAXEXTENT;
        }

        if (xy[0] < -MAXEXTENT) {
            xy[0] = -MAXEXTENT;
        }

        if (xy[1] > MAXEXTENT) {
            xy[1] = MAXEXTENT;
        }

        if (xy[1] < -MAXEXTENT) {
            xy[1] = -MAXEXTENT;
        }
        return xy;

    }

    public double[] inverse(double[] xy) {
        double[] lonlat = {
            (xy[0] * R2D / A),
            ((Math.PI * 0.5) - 2.0 * Math.atan(Math.exp(-xy[1] / A))) * R2D
        };
        return lonlat;
    }

    public double[] ll(double[] px, int zoom) {
        
        double g = (px[1] - cache.get(size).getZc()[zoom]) / -(cache.get(size).getCc()[zoom]);
        double lon = (px[0] - cache.get(size).getZc()[zoom]) / cache.get(size).getBc()[zoom];
        double lat = R2D * (2 * Math.atan(Math.exp(g)) - 0.5 * Math.PI);
        return new double[]{lon, lat};

    }

}
