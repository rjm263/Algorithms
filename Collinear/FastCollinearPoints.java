/* *****************************************************************************
 *  Synopsis: given a set of points, finds all line segments containing four or more points.
 *            Uses sorting by slope order; much faster than brute force.
 **************************************************************************** */

import edu.princeton.cs.algs4.Stack;

import java.util.Arrays;

public class FastCollinearPoints {

    private Stack<LineSegment> segList = new Stack<>();
    private int counter = 0;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("The array of points is empty!");
        for (Point p : points) {
            if (p == null) throw new IllegalArgumentException("Some points are not assigned!");
        }
        Point prev = null;

        Point[] copy = points.clone();

        Arrays.sort(copy);
        for (Point p : copy) {
            if (prev != null && prev.compareTo(p) == 0)
                throw new IllegalArgumentException("There are duplicate points!");
            prev = p;
        }

        int n = copy.length;

        // iterate through all origin points
        for (int i = 0; i < n; i++) {
            Point p0 = copy[i];
            Point[] sorted = copy.clone();
            Arrays.sort(sorted, p0.slopeOrder());

            // iterate through ALL other points (also ones below p0)
            int j = 1;
            while (j < n) {
                Stack<Point> collStack = new Stack<>();
                double slopeOld = p0.slopeTo(sorted[j]);
                do {
                    collStack.push(sorted[j++]);
                } while (j < n && Double.compare(p0.slopeTo(sorted[j]), slopeOld) == 0);

                if (collStack.size() >= 3) {
                    collStack.push(p0);
                    int size = collStack.size();
                    Point[] collinear = new Point[size];
                    for (int k = 0; k < size; k++) {
                        Point point = collStack.pop();
                        collinear[k] = point;
                    }
                    Arrays.sort(collinear);
                    if (p0.compareTo(collinear[0]) == 0) {
                        segList.push(new LineSegment(p0, collinear[collinear.length - 1]));
                        counter++;
                    }
                }
            }

        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return counter;
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] segments = new LineSegment[counter];

        // copy segList in order to avoid mutilation
        Stack<LineSegment> temp = new Stack<>();
        for (LineSegment s : segList) temp.push(s);

        for (int i = 0; i < counter; i++) {
            LineSegment segment = temp.pop();
            segments[i] = segment;
        }
        return segments;
    }
}
