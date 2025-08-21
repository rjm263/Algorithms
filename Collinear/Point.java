/* *****************************************************************************
 *  Synopsis: API for immutable 2D point data type. Defines following features:
 *      -- draw(): draws point to standard output
 *      -- drawTo(): draws a line segment from current point to argument point
 *      -- slopeTo(): returns slope to argument point
 *      -- compareTo(): compares according to y coordinate; x breaks ties
 *      -- includes comparator ordering points according to above
 **************************************************************************** */

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Point implements Comparable<Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Draws this point to standard draw.
    public void draw() {
        StdDraw.point(x, y);
    }


    // Draws the line segment between this point and the specified point to standard draw.
    public void drawTo(Point that) {
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    // Return the slope of point that to point this.
    public double slopeTo(Point that) {
        if (this.x == that.x && this.y == that.y) return Double.NEGATIVE_INFINITY;
        if (this.x == that.x) return Double.POSITIVE_INFINITY;
        if (this.y == that.y) return 0.0;
        else return (double) (that.y - this.y) / (that.x - this.x);
    }

    // Return 0 of equal, -1 is this < that, and 1 otherwise; x breaks ties.
    public int compareTo(Point that) {
        if (this.y < that.y) return -1;
        if (this.y == that.y) {
            if (this.x < that.x) return -1;
            else if (this.x == that.x) return 0;
            else return 1;
        }
        return 1;
    }

    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
        return new Comparator<Point>() {
            public int compare(Point p1, Point p2) {
                return Double.compare(slopeTo(p1), slopeTo(p2));
            }
        };
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }


    // Unit tests the Point data type.
    public static void main(String[] args) {

        Point[] points = new Point[3];
        points[0] = new Point(2, 3);
        points[1] = new Point(2, 0);
        points[2] = new Point(2, 1);

        // Arrays.sort(points);
        Point prev = null;
        for (Point p : points) {
            if (p == null) throw new IllegalArgumentException("Some points are not assigned!");
            if (prev != null && prev.compareTo(p) == 0)
                throw new IllegalArgumentException("Some points are duplicates of others!");
            prev = p;
        }
        if (points[0].compareTo(points[0]) != 0) StdOut.println("Not equal!");
        for (Point p : points) {
            StdOut.println(p.toString());
        }
    }
}
