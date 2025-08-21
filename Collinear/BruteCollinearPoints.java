/* *****************************************************************************
 *  Synopsis: in a set of points, finds all line segments containing precisely four points.
 *            Assumes that there are no duplicates in the set. Brute force method.
 **************************************************************************** */

import edu.princeton.cs.algs4.Stack;

import java.util.Arrays;

public class BruteCollinearPoints {

    private Stack<LineSegment> segList = new Stack<>();
    private int counter = 0;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
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

        for (int i = 0; i < n - 3; i++) {
            for (int j = i + 1; j < n - 2; j++) {
                for (int k = j + 1; k < n - 1; k++) {
                    for (int l = k + 1; l < n; l++) {
                        Point p1 = copy[i];
                        Point p2 = copy[j];
                        Point p3 = copy[k];
                        Point p4 = copy[l];
                        if (Double.compare(p1.slopeTo(p2), p1.slopeTo(p3)) == 0
                                && Double.compare(p1.slopeTo(p3), p1.slopeTo(p4)) == 0) {
                            LineSegment seg = new LineSegment(p1, p4);
                            segList.push(seg);
                            counter++;
                        }
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
