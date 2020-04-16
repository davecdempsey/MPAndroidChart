
package com.github.mikephil.charting.data.filter;

import android.annotation.TargetApi;
import android.os.Build;

import java.util.Arrays;

/**
 * Implemented according to Wiki-Pseudocode {@link}
 * http://en.wikipedia.org/wiki/Ramer�Douglas�Peucker_algorithm
 *
 * @author Philipp Baldauf & Phliipp Jahoda
 */
public class Approximator {

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public double[] reduceWithDouglasPeucker(double[] points, double tolerance) {

        int greatestIndex = 0;
        double greatestDistance = 0;

        Line line = new Line(points[0], points[1], points[points.length - 2], points[points.length - 1]);

        for (int i = 2; i < points.length - 2; i += 2) {

            double distance = line.distance(points[i], points[i + 1]);

            if (distance > greatestDistance) {
                greatestDistance = distance;
                greatestIndex = i;
            }
        }

        if (greatestDistance > tolerance) {

            double[] reduced1 = reduceWithDouglasPeucker(Arrays.copyOfRange(points, 0, greatestIndex + 2), tolerance);
            double[] reduced2 = reduceWithDouglasPeucker(Arrays.copyOfRange(points, greatestIndex, points.length),
                    tolerance);

            double[] result1 = reduced1;
            double[] result2 = Arrays.copyOfRange(reduced2, 2, reduced2.length);

            return concat(result1, result2);
        } else {
            return line.getPoints();
        }
    }

    /**
     * Combine arrays.
     *
     * @param arrays
     * @return
     */
    double[] concat(double[]... arrays) {
        int length = 0;
        for (double[] array : arrays) {
            length += array.length;
        }
        double[] result = new double[length];
        int pos = 0;
        for (double[] array : arrays) {
            for (double element : array) {
                result[pos] = element;
                pos++;
            }
        }
        return result;
    }

    private class Line {

        private double[] points;

        private double sxey;
        private double exsy;

        private double dx;
        private double dy;

        private double length;

        public Line(double x1, double y1, double x2, double y2) {
            dx = x1 - x2;
            dy = y1 - y2;
            sxey = x1 * y2;
            exsy = x2 * y1;
            length =  Math.sqrt(dx * dx + dy * dy);

            points = new double[]{x1, y1, x2, y2};
        }

        public double distance(double x, double y) {
            return Math.abs(dy * x - dx * y + sxey - exsy) / length;
        }

        public double[] getPoints() {
            return points;
        }
    }
}
