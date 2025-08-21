/* *****************************************************************************
 *  Synopsis: constructs a set of 2D points.
 *      -- contains(): does the set contain the point?
 *      -- insert(): inserts a point
 *      -- range(): all points inside a rectangle
 *      -- nearest(): nearest neighbour to a point
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

public class PointSET {

    private SET<Point2D> set;

    // construct an empty set of points
    public PointSET() {
        this.set = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return this.set.isEmpty();
    }

    // number of points in the set
    public int size() {
        return this.set.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("No input provided!");

        this.set.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("No input provided!");

        return this.set.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : this.set) p.draw();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("No input provided!");

        Queue<Point2D> q = new Queue<>();
        for (Point2D p : this.set) {
            if (rect.contains(p)) q.enqueue(p);
        }
        return q;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("No input provided!");
        if (this.isEmpty()) return null;

        double dist = Double.POSITIVE_INFINITY;
        Point2D champ = null;
        for (Point2D s : this.set) {
            if (s.distanceSquaredTo(p) < dist) {
                dist = s.distanceSquaredTo(p);
                champ = s;
            }
        }
        return champ;
    }

    // unit testing of the methods
    public static void main(String[] args) {
        PointSET set = new PointSET();
        set.insert(new Point2D(0, 0));
        set.insert(new Point2D(1, 0));
        set.insert(new Point2D(1, 1));
        set.insert(new Point2D(0, 2));
        set.insert(new Point2D(3, 0));
        set.insert(new Point2D(2, 7));

        StdOut.println("The set is of size " + set.size());
        StdOut.println("Does the set contain (0,1)? --> " + set.contains(new Point2D(0, 1)));
        StdOut.println("Does the set contain (1,0)? --> " + set.contains(new Point2D(1, 0)));

        Point2D point = new Point2D(0.6, 8);
        StdOut.println(
                "The point closest to " + point.toString() + " is " + set.nearest(point)
                                                                         .toString());

        RectHV rect = new RectHV(0, 0, 1, 1);
        StdOut.println("The following points are inside the rectangle " + rect.toString());
        for (Point2D p : set.range(rect)) StdOut.println(p.toString());
    }

}
