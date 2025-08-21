/* *****************************************************************************
 *  Synopsis: Kd-tree that stores 2D points. Contains all methods of PointSET,
 *            but is much faster.
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

import java.awt.Color;
import java.util.Comparator;

public class KdTree {

    private class Node {

        private final Point2D point;
        private final int level;
        private int count;
        private Node left, right;
        private final RectHV rect;

        public Node(Point2D p, RectHV rect, int level, int c) {
            this.point = p;
            this.level = level;
            this.count = c;
            this.rect = rect;
        }
    }

    private Node root;

    // new comparator for comparison wrt x-coordinate
    private Comparator<Point2D> byX = new Comparator<Point2D>() {
        public int compare(Point2D o1, Point2D o2) {
            if (Double.compare(o1.x(), o2.x()) < 0) return -1;
            if (Double.compare(o1.x(), o2.x()) == 0) {
                return Double.compare(o1.y(), o2.y());
            }
            return 1;
        }
    };

    // construct an empty set of points
    public KdTree() {
        root = null;
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (x == null) return 0;
        return x.count;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("No input provided!");
        root = insert(root, p, new RectHV(0, 0, 1, 1), 0);
    }

    private Node insert(Node node, Point2D p, RectHV rect, int level) {
        if (node == null) return new Node(p, rect, level, 1);
        if (p.equals(node.point)) return node;

        if (node.level % 2 == 1) {
            int cmp = p.compareTo(node.point);
            if (cmp < 0) {
                if (node.left == null) {
                    node.left = insert(node.left, p,
                                       new RectHV(rect.xmin(), rect.ymin(), rect.xmax(),
                                                  node.point.y()), level + 1);
                }
                else node.left = insert(node.left, p, node.left.rect, level + 1);
            }
            if (cmp > 0) {
                if (node.right == null) {
                    node.right = insert(node.right, p,
                                        new RectHV(rect.xmin(), node.point.y(), rect.xmax(),
                                                   rect.ymax()), level + 1);
                }
                else node.right = insert(node.right, p, node.right.rect, level + 1);
            }
            node.count = size(node.left) + size(node.right) + 1;
            return node;
        }
        else {
            int cmp = byX.compare(p, node.point);
            if (cmp < 0) {
                if (node.left == null) {
                    node.left = insert(node.left, p,
                                       new RectHV(rect.xmin(), rect.ymin(), node.point.x(),
                                                  rect.ymax()), level + 1);
                }
                else node.left = insert(node.left, p, node.left.rect, level + 1);
            }
            if (cmp > 0) {
                if (node.right == null) {
                    node.right = insert(node.right, p,
                                        new RectHV(node.point.x(), rect.ymin(), rect.xmax(),
                                                   rect.ymax()), level + 1);
                }
                else node.right = insert(node.right, p, node.right.rect, level + 1);
            }
            node.count = size(node.left) + size(node.right) + 1;
            return node;
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("No input provided!");
        return contains(root, p);
    }

    private boolean contains(Node node, Point2D p) {
        if (node == null) return false;

        if (node.level % 2 == 1) {
            int cmp = p.compareTo(node.point);
            if (cmp < 0) return contains(node.left, p);
            else if (cmp > 0) return contains(node.right, p);
            else return true;
        }
        else {
            int cmp = byX.compare(p, node.point);
            if (cmp < 0) return contains(node.left, p);
            else if (cmp > 0) return contains(node.right, p);
            else return true;
        }
    }

    // draw all points to standard draw
    public void draw() {
        draw(root);
    }

    private void draw(Node node) {
        if (node == null) return;

        Point2D p = node.point;
        RectHV rect = node.rect;
        StdDraw.setPenColor(Color.black);
        p.draw();
        if (node.level % 2 == 0) {
            StdDraw.setPenColor(Color.red);
            StdDraw.line(p.x(), rect.ymin(), p.x(), rect.ymax());
        }
        else {
            StdDraw.setPenColor(Color.blue);
            StdDraw.line(rect.xmin(), p.y(), rect.xmax(), p.y());
        }

        draw(node.left);
        draw(node.right);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("No input provided!");

        Queue<Point2D> q = new Queue<>();
        range(root, rect, q);
        return q;
    }

    private void range(Node node, RectHV rect, Queue<Point2D> q) {
        if (node == null) return;

        if (!rect.intersects(node.rect)) return;
        if (rect.contains(node.point)) q.enqueue(node.point);

        if (node.left != null && rect.intersects(node.left.rect)) range(node.left, rect, q);
        if (node.right != null && rect.intersects(node.right.rect)) range(node.right, rect, q);
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("No input provided!");
        if (root == null) return null;
        return nearest(root, p, root.point);
    }

    private Point2D nearest(Node node, Point2D p, Point2D champ) {
        if (node == null) return champ;

        if (p.distanceSquaredTo(champ) < node.rect.distanceSquaredTo(p)) return champ;
        if (p.distanceSquaredTo(node.point) < p.distanceSquaredTo(champ)) champ = node.point;
        if (node.level % 2 == 1) {
            if (p.compareTo(node.point) <= 0) {
                champ = nearest(node.left, p, champ);
                if (node.right != null
                        && node.right.rect.distanceSquaredTo(p) < p.distanceSquaredTo(champ))
                    champ = nearest(node.right, p, champ);
            }
            if (p.compareTo(node.point) > 0) {
                champ = nearest(node.right, p, champ);
                if (node.left != null && node.left.rect.distanceSquaredTo(p) < p.distanceSquaredTo(
                        champ))
                    champ = nearest(node.left, p, champ);
            }
        }
        else {
            if (byX.compare(p, node.point) <= 0) {
                champ = nearest(node.left, p, champ);
                if (node.right != null
                        && node.right.rect.distanceSquaredTo(p) < p.distanceSquaredTo(champ))
                    champ = nearest(node.right, p, champ);
            }
            if (byX.compare(p, node.point) > 0) {
                champ = nearest(node.right, p, champ);
                if (node.left != null && node.left.rect.distanceSquaredTo(p) < p.distanceSquaredTo(
                        champ))
                    champ = nearest(node.left, p, champ);
            }
        }
        return champ;
    }

    // unit testing of the methods
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        PointSET brute = new PointSET();
        KdTree kdtree = new KdTree();

        StdOut.println("Building...");
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
            brute.insert(p);
        }

        for (int n = 1; n < 8; n++) {
            int N = (int) Math.pow(10, n);
            Point2D[] rdPts = new Point2D[N];
            for (int i = 0; i < N; i++) {
                double x = StdRandom.uniformDouble(0, 1);
                double y = StdRandom.uniformDouble(0, 1);
                Point2D p = new Point2D(x, y);
                rdPts[i] = p;
            }

            StdOut.println("Testing N = " + N + "...");
            Stopwatch sw = new Stopwatch();
            for (int i = 0; i < N; i++) {
                kdtree.nearest(rdPts[i]);
            }
            double kt = sw.elapsedTime();

            // Stopwatch sw1 = new Stopwatch();
            // for (int i = 0; i < N; i++) {
            //     brute.nearest(rdPts[i]);
            // }
            // double bt = sw1.elapsedTime();

            // StdOut.println("Brute force: " + bt);
            StdOut.println("KdTree: " + kt);
        }
    }
}
