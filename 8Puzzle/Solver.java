/* *****************************************************************************
 *  Synopsis: Solver for the 8Puzzle (https://www.8puzzle.com/8_puzzle_problem.html).
 *  Dependencies: -- Board.java
 **************************************************************************** */

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {

    private int count = 0;
    private Node finalNode = null;
    private boolean sol = true;

    // create helper class that stores board, #moves and parent board
    private class Node {

        private Board board;
        private int moves;
        private Node prev;
        private int priority;

        public Node(Board board, int moves, Node prev) {
            this.board = board;
            this.moves = moves;
            this.prev = prev;
            this.priority = board.manhattan() + moves;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("No initial board provided!");

        Comparator<Node> byPriority = new Comparator<Node>() {
            public int compare(Node o1, Node o2) {
                return Integer.compare(o1.priority, o2.priority);
            }
        };

        MinPQ<Node> pq = new MinPQ<>(byPriority);
        MinPQ<Node> tq = new MinPQ<>(byPriority);
        pq.insert(new Node(initial, 0, null));
        Node next = pq.delMin();

        // check if twin can be solved instead
        Board twin = initial.twin();
        tq.insert(new Node(twin, 0, null));
        Node nextTwin = tq.delMin();

        // iterate over both initial and twin to avoid infinite loop
        while (!next.board.isGoal() && !nextTwin.board.isGoal()) {

            for (Board b : next.board.neighbors()) {
                if (next.prev == null || !b.equals(next.prev.board)) {
                    pq.insert(new Node(b, next.moves + 1, next));
                }
            }
            next = pq.delMin();

            for (Board t : nextTwin.board.neighbors()) {
                if (nextTwin.prev == null || !t.equals(nextTwin.prev.board)) {
                    tq.insert(new Node(t, nextTwin.moves + 1, nextTwin));
                }
            }
            nextTwin = tq.delMin();
        }
        if (next.board.isGoal()) {
            sol = true;
            finalNode = next;
        }
        else {
            sol = false;
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return sol;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!this.isSolvable()) return -1;
        return finalNode.moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!this.isSolvable()) return null;
        Stack<Board> path = new Stack<>();
        Node next = finalNode;
        path.push(next.board);
        while (next.prev != null) {
            path.push(next.prev.board);
            next = next.prev;
        }
        return path;
    }

    // test client
    public static void main(String[] args) {
        int[][] tiles = { { 1, 2, 3 }, { 4, 0, 6 }, { 7, 5, 8 } };
        Board puzzle = new Board(tiles);
        Solver solver = new Solver(puzzle);

        StdOut.printf(puzzle.toString());
        if (!solver.isSolvable()) StdOut.println("The puzzle cannot be solved!");
        else {
            StdOut.println("The puzzle can be solved in " + solver.moves() + " moves!");
            for (Board b : solver.solution()) StdOut.printf(b.toString());
        }
    }

}
